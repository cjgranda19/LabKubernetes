import { AppDataSource } from '../utils/database';
import { Ticket } from '../entities/Ticket.entity';
import { ZoneService } from './zone.service';
import { PersonaService } from './persona.service';
import { RabbitMQService } from './rabbitmq.service';
import { v4 as uuidv4 } from 'uuid';

/**
 * Servicio principal para la gestión de Tickets de estacionamiento.
 * Coordina la interacción entre los microservicios y la base de datos local.
 */
export class TicketService {
  private ticketRepository = AppDataSource.getRepository(Ticket);
  private zoneService = new ZoneService();
  private personaService = new PersonaService();
  private rabbitMQService = new RabbitMQService();

  /**
   * Genera un código único para un nuevo ticket.
   * @returns Código de ticket único
   */
  private generateTicketCode = (): string => {
    const randomId = uuidv4().split('-')[0].toUpperCase();
    const timestamp = Date.now().toString().slice(-6);
    return `TICK-${timestamp}-${randomId}`;
  };

  /**
   * Emite un nuevo ticket de estacionamiento.
   * Valida la persona, el vehículo y asigna un espacio disponible.
   * @param personaIdentificacion - Identificación de la persona
   * @param vehiculoPlaca - Placa del vehículo
   * @param zonaId - ID de la zona preferida (opcional)
   * @returns Promise con el ticket creado
   */
  emitirTicket = async (
    personaIdentificacion: string,
    vehiculoPlaca: string,
    zonaId?: string
  ): Promise<Ticket> => {
    try {
      // 1. Validar que la persona existe y está activa
      console.log('🔍 Paso 1: Buscando persona:', personaIdentificacion);
      const persona = await this.personaService.getPersonaByIdentificacion(personaIdentificacion);
      console.log('✅ Persona encontrada:', persona);
      if (!persona.activo) {
        throw new Error('La persona no está activa en el sistema');
      }

      // 2. Validar que el vehículo existe y está activo
      console.log('🔍 Paso 2: Buscando vehículo:', vehiculoPlaca);
      const vehiculo = await this.personaService.getVehiculoByPlaca(vehiculoPlaca);
      console.log('✅ Vehículo encontrado:', vehiculo);
      if (!vehiculo.activo) {
        throw new Error('El vehículo no está activo en el sistema');
      }

      // 3. Validar que el vehículo pertenece a la persona
      console.log('🔍 Paso 3: Validando propiedad del vehículo');
      const pertenece = await this.personaService.validateVehiculoBelongsToPersona(persona.id, vehiculo.id);
      console.log('✅ Validación de propiedad:', pertenece);
      if (!pertenece) {
        throw new Error('El vehículo no pertenece a la persona especificada');
      }

      // 4. Obtener un espacio disponible
      console.log('🔍 Paso 4: Buscando espacio disponible');
      let espacio;
      if (zonaId) {
        // Buscar espacios en zona específica
        const espaciosZona = await this.zoneService.getSpacesByZone(zonaId);
        console.log('✅ Espacios en zona:', espaciosZona);
        const disponible = espaciosZona.find(
          e => e.status === 'AVAILABLE' && !e.isReserved
        );

        if (!disponible) {
          throw new Error(`No hay espacios disponibles en la zona ${zonaId}`);
        }
        espacio = disponible;
      } else {
        // Buscar cualquier espacio disponible
        const espaciosDisponibles = await this.zoneService.getAvailableSpaces();
        console.log('✅ Espacios disponibles:', espaciosDisponibles);
        if (espaciosDisponibles.length === 0) {
          throw new Error('No hay espacios disponibles en ninguna zona');
        }

        // Seleccionar el espacio con mayor prioridad (menor número)
        espaciosDisponibles.sort((a, b) => a.priority - b.priority);
        espacio = espaciosDisponibles[0];
      }
      console.log('✅ Espacio seleccionado:', espacio);

      // 5. Obtener información de la zona del espacio
      console.log('🔍 Paso 5: Obteniendo información de zona:', espacio.idZone);
      const zona = await this.zoneService.getZoneById(espacio.idZone);
      console.log('✅ Zona obtenida:', zona);

      // 6. Actualizar el espacio como OCUPADO en el microservicio
      console.log('🔍 Paso 6: Actualizando estado del espacio a OCCUPIED');
      await this.zoneService.updateSpaceStatus(espacio.id, 'OCCUPIED');
      console.log('✅ Estado actualizado');

      // 7. Crear el ticket en la base de datos local
      console.log('🔍 Paso 7: Creando ticket en BD local');
      const ticket = new Ticket();
      ticket.codigoTicket = this.generateTicketCode();
      ticket.personaIdentificacion = persona.identificacion;
      ticket.personaNombre = persona.nombre;
      ticket.vehiculoPlaca = vehiculo.placa;
      ticket.vehiculoMarca = vehiculo.marca;
      ticket.vehiculoModelo = vehiculo.modelo;
      ticket.zonaNombre = zona.name;
      ticket.espacioCodigo = espacio.code;
      ticket.fechaHoraIngreso = new Date();
      ticket.estado = 'ACTIVO';

      // 8. Guardar el ticket en la base de datos
      console.log('🔍 Paso 8: Guardando ticket');
      const savedTicket = await this.ticketRepository.save(ticket);
      console.log('✅ Ticket guardado:', savedTicket.id);
      
      // 9. Enviar notificación
      try {
        console.log('🔍 Paso 9: Enviando notificación a RabbitMQ');
        await this.rabbitMQService.sendTicketEmitido(
          savedTicket.id,
          savedTicket.personaNombre,
          savedTicket.vehiculoPlaca,
          savedTicket.zonaNombre
        );
        console.log('✅ Notificación enviada');
      } catch (error) {
        console.error('⚠️ Error al enviar notificación de ticket emitido:', error);
      }
      
      console.log('🎉 Ticket emitido exitosamente:', savedTicket.codigoTicket);
      return savedTicket;
    } catch (error) {
      console.error('❌ Error en emitirTicket:', error);
      throw error;
    }
  };

  /**
   * Cierra un ticket de estacionamiento (registra la salida).
   * Calcula el tiempo estacionado y libera el espacio.
   * @param ticketId - ID del ticket a cerrar
   * @returns Promise con el ticket actualizado
   */
  cerrarTicket = async (ticketId: string): Promise<Ticket> => {
    // 1. Buscar el ticket
    const ticket = await this.ticketRepository.findOne({
      where: { id: ticketId },
    });

    if (!ticket) {
      throw new Error('Ticket no encontrado');
    }

    if (ticket.estado !== 'ACTIVO') {
      throw new Error('El ticket ya está cerrado o anulado');
    }

    // 2. Calcular tiempo estacionado
    const fechaSalida = new Date();
    const tiempoEstacionadoMs = fechaSalida.getTime() - ticket.fechaHoraIngreso.getTime();
    const tiempoEstacionadoMinutos = Math.floor(tiempoEstacionadoMs / (1000 * 60));

    // 3. Buscar el espacio para liberarlo
    try {
      const espacio = await this.zoneService.getSpaceByCode(ticket.espacioCodigo);
      await this.zoneService.updateSpaceStatus(espacio.id, 'AVAILABLE');
    } catch (error: unknown) {
      const message = error instanceof Error ? error.message : String(error);
      console.error('Error al liberar espacio:', message);
      // Continuar con el cierre del ticket aunque falle la liberación del espacio
    }

    // 4. Actualizar el ticket
    ticket.fechaHoraSalida = fechaSalida;
    ticket.tiempoEstacionado = tiempoEstacionadoMinutos;
    ticket.estado = 'CERRADO';

    const updatedTicket = await this.ticketRepository.save(ticket);
    
    // 5. Enviar notificación
    try {
      await this.rabbitMQService.sendTicketCerrado(
        updatedTicket.id,
        updatedTicket.personaNombre,
        updatedTicket.vehiculoPlaca
      );
    } catch (error) {
      console.error('Error al enviar notificación de ticket cerrado:', error);
    }
    
    return updatedTicket;
  };

  /**
   * Obtiene todos los tickets del sistema.
   * @returns Promise con array de todos los tickets
   */
  getAllTickets = async (): Promise<Ticket[]> => {
    return await this.ticketRepository.find({
      order: { fechaHoraIngreso: 'DESC' },
    });
  };

  /**
   * Obtiene un ticket específico por su código.
   * @param codigoTicket - Código del ticket a buscar
   * @returns Promise con el ticket encontrado
   */
  getTicketByCodigo = async (codigoTicket: string): Promise<Ticket> => {
    const ticket = await this.ticketRepository.findOne({
      where: { codigoTicket },
    });

    if (!ticket) {
      throw new Error(`Ticket con código ${codigoTicket} no encontrado`);
    }

    return ticket;
  };

  /**
   * Obtiene todos los tickets de una persona específica.
   * @param personaIdentificacion - Identificación de la persona
   * @returns Promise con array de tickets de la persona
   */
  getTicketsByPersona = async (personaIdentificacion: string): Promise<Ticket[]> => {
    return await this.ticketRepository.find({
      where: { personaIdentificacion },
      order: { fechaHoraIngreso: 'DESC' },
    });
  };

  /**
   * Obtiene todos los tickets de un vehículo específico.
   * @param vehiculoPlaca - Placa del vehículo
   * @returns Promise con array de tickets del vehículo
   */
  getTicketsByVehiculo = async (vehiculoPlaca: string): Promise<Ticket[]> => {
    return await this.ticketRepository.find({
      where: { vehiculoPlaca },
      order: { fechaHoraIngreso: 'DESC' },
    });
  };

  /**
   * Obtiene tickets activos (no cerrados).
   * @returns Promise con array de tickets activos
   */
  getActiveTickets = async (): Promise<Ticket[]> => {
    return await this.ticketRepository.find({
      where: { estado: 'ACTIVO' },
      order: { fechaHoraIngreso: 'DESC' },
    });
  };

  /**
   * Anula un ticket (para casos de error o cancelación).
   * @param ticketId - ID del ticket a anular
   * @returns Promise con el ticket anulado
   */
  anularTicket = async (ticketId: string): Promise<Ticket> => {
    const ticket = await this.ticketRepository.findOne({
      where: { id: ticketId },
    });

    if (!ticket) {
      throw new Error('Ticket no encontrado');
    }

    if (ticket.estado !== 'ACTIVO') {
      throw new Error('Solo se pueden anular tickets activos');
    }

    // Liberar el espacio si está ocupado
    if (ticket.estado === 'ACTIVO') {
      try {
        const espacio = await this.zoneService.getSpaceByCode(ticket.espacioCodigo);
        await this.zoneService.updateSpaceStatus(espacio.id, 'AVAILABLE');
      } catch (error: unknown) {
        const message = error instanceof Error ? error.message : String(error);
        console.error('Error al liberar espacio:', message);
      }
    }

    ticket.estado = 'ANULADO';
    ticket.fechaHoraSalida = new Date();

    const anulatedTicket = await this.ticketRepository.save(ticket);
    
    // Enviar notificación
    try {
      await this.rabbitMQService.sendTicketAnulado(
        anulatedTicket.id,
        anulatedTicket.personaNombre,
        anulatedTicket.vehiculoPlaca
      );
    } catch (error) {
      console.error('Error al enviar notificación de ticket anulado:', error);
    }
    
    return anulatedTicket;
  };
}
