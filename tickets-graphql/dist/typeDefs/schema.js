"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.typeDefs = void 0;
/**
 * Definiciones de tipos GraphQL para el servicio de Tickets.
 * Utiliza la sintaxis SDL (Schema Definition Language) de GraphQL.
 */
exports.typeDefs = `#graphql
  # Tipo Ticket: Representa un ticket de estacionamiento en el sistema
  type Ticket {
    id: ID!                     # Identificador único del ticket
    codigoTicket: String!       # Código único del ticket (ej: TICK-001)
    personaIdentificacion: String!  # Identificación de la persona (cédula/RUC)
    personaNombre: String!      # Nombre completo de la persona
    vehiculoPlaca: String!      # Placa del vehículo
    vehiculoMarca: String!      # Marca del vehículo
    vehiculoModelo: String!     # Modelo del vehículo
    zonaNombre: String!         # Nombre de la zona de estacionamiento
    espacioCodigo: String!      # Código del espacio asignado
    fechaIngreso: String!       # Fecha y hora de ingreso (ISO string)
    fechaSalida: String         # Fecha y hora de salida (ISO string, null si activo)
    estado: String!             # Estado: ACTIVO, CERRADO, ANULADO
    tiempoEstacionadoMinutos: Int  # Tiempo total estacionado en minutos
    createdAt: String!          # Fecha de creación del registro
    updatedAt: String!          # Fecha de última actualización
  }

  # Tipo para estadísticas de tickets
  type TicketStats {
    total: Int!                 # Total de tickets
    activos: Int!               # Tickets activos
    cerrados: Int!              # Tickets cerrados
    anulados: Int!              # Tickets anulados
    promedioTiempoMinutos: Float  # Tiempo promedio de estacionamiento
  }

  # Consultas disponibles en el servicio GraphQL
  type Query {
    # Obtiene todos los tickets del sistema
    tickets: [Ticket!]!
    
    # Obtiene un ticket específico por su código
    ticket(codigoTicket: String!): Ticket
    
    # Obtiene tickets por identificación de persona
    ticketsByPersona(identificacion: String!): [Ticket!]!
    
    # Obtiene tickets por placa de vehículo
    ticketsByVehiculo(placa: String!): [Ticket!]!
    
    # Obtiene solo los tickets activos
    ticketsActivos: [Ticket!]!
    
    # Obtiene estadísticas de tickets
    estadisticasTickets: TicketStats!
  }

  # Mutaciones disponibles (operaciones que modifican datos)
  type Mutation {
    # Emite un nuevo ticket de estacionamiento
    emitirTicket(
      personaIdentificacion: String!  # Identificación de la persona
      vehiculoPlaca: String!          # Placa del vehículo
      zonaId: String                  # ID de la zona preferida (opcional)
    ): Ticket!
    
    # Cierra un ticket (registra la salida)
    cerrarTicket(ticketId: String!): Ticket!
    
    # Anula un ticket (para casos de error o cancelación)
    anularTicket(ticketId: String!): Ticket!
  }
`;
