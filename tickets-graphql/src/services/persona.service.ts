import { FetchClient } from '../utils/fetchClient';

/**
 * Interfaz para la respuesta de una Persona del microservicio
 */
interface PersonaResponse {
  id: string;
  identificacion: string;
  nombre: string;
  email: string;
  telefono: string;
  tipoPersona: string;
  fechaCreacion: string;
  activo: boolean;
}

/**
 * Interfaz para la respuesta de un Vehículo del microservicio
 */
interface VehiculoResponse {
  id: string;
  placa: string;
  marca: string;
  modelo: string;
  color: string;
  anioFabricacion: number;
  tipoVehiculo: string;
  propietarioId: string;
  propietarioNombre: string;
  fechaRegistro: string;
  impuesto: number;
  activo: boolean;
}

/**
 * Servicio para interactuar con el microservicio de Personas y Vehículos.
 * Proporciona métodos para buscar personas por identificación y vehículos por placa.
 */
export class PersonaService {
  private baseUrl: string;

  /**
   * Constructor que inicializa la URL base del microservicio desde variables de entorno.
   */
  constructor() {
    this.baseUrl = process.env.PERSONA_SERVICE_URL || 'http://localhost:8081';
    if (!this.baseUrl) {
      throw new Error('PERSONA_SERVICE_URL no está definida en las variables de entorno');
    }
  }

  /**
   * Busca una persona por su número de identificación.
   * @param identificacion - Número de cédula o RUC
   * @returns Promise con la persona encontrada
   */
  getPersonaByIdentificacion = async (identificacion: string): Promise<PersonaResponse> => {
    const url = `${this.baseUrl}/api/personas/identificacion/${identificacion}`;
    return await FetchClient.get<PersonaResponse>(url);
  };

  /**
   * Busca un vehículo por su número de placa.
   * @param placa - Número de placa del vehículo
   * @returns Promise con el vehículo encontrado
   */
  getVehiculoByPlaca = async (placa: string): Promise<VehiculoResponse> => {
    const url = `${this.baseUrl}/api/vehiculos/placa/${placa}`;
    return await FetchClient.get<VehiculoResponse>(url);
  };

  /**
   * Verifica si un vehículo pertenece a una persona específica.
   * @param personaId - ID de la persona
   * @param vehiculoId - ID del vehículo
   * @returns Promise con booleano indicando si pertenece
   */
  validateVehiculoBelongsToPersona = async (personaId: string, vehiculoId: string): Promise<boolean> => {
    const url = `${this.baseUrl}/api/vehiculos/propietario/${personaId}`;
    const vehiculos = await FetchClient.get<VehiculoResponse[]>(url);
    
    return vehiculos.some(v => v.id === vehiculoId);
  };

  /**
   * Obtiene todos los vehículos de una persona específica.
   * @param personaId - ID de la persona
   * @returns Promise con array de vehículos de la persona
   */
  getVehiculosByPersona = async (personaId: string): Promise<VehiculoResponse[]> => {
    const url = `${this.baseUrl}/api/vehiculos/propietario/${personaId}`;
    return await FetchClient.get<VehiculoResponse[]>(url);
  };
}