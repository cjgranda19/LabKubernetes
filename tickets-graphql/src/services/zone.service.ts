import { FetchClient } from '../utils/fetchClient';

/**
 * Interfaz para la respuesta de una Zona del microservicio
 */
interface ZoneResponse {
  id: string;
  name: string;
  description: string;
  capacity: number;
  type: string;
  isActive: boolean;
}

/**
 * Interfaz para la respuesta de un Espacio del microservicio
 */
interface SpaceResponse {
  id: string;
  code: string;
  status: string;
  isReserved: boolean;
  idZone: string;  // Backend devuelve 'idZone', no 'zoneId'
  zoneName?: string;
  priority: number;
}

/**
 * Servicio para interactuar con el microservicio de Zonas y Espacios.
 * Proporciona métodos para obtener información sobre zonas y espacios disponibles.
 */
export class ZoneService {
  private baseUrl: string;

  /**
   * Constructor que inicializa la URL base del microservicio desde variables de entorno.
   */
  constructor() {
    this.baseUrl = process.env.ZONE_SERVICE_URL || 'http://localhost:8080';
    if (!this.baseUrl) {
      throw new Error('ZONE_SERVICE_URL no está definida en las variables de entorno');
    }
  }

  /**
   * Obtiene todas las zonas disponibles del microservicio.
   * @returns Promise con array de zonas
   */
  getAllZones = async (): Promise<ZoneResponse[]> => {
    const url = `${this.baseUrl}/api/zones`;
    return await FetchClient.get<ZoneResponse[]>(url);
  };

  /**
   * Obtiene una zona específica por su ID.
   * @param zoneId - ID de la zona a buscar
   * @returns Promise con la zona encontrada
   */
  getZoneById = async (zoneId: string): Promise<ZoneResponse> => {
    const url = `${this.baseUrl}/api/zones/${zoneId}`;
    return await FetchClient.get<ZoneResponse>(url);
  };

  /**
   * Obtiene todos los espacios disponibles (no ocupados ni reservados).
   * @returns Promise con array de espacios disponibles
   */
  getAvailableSpaces = async (): Promise<SpaceResponse[]> => {
    const url = `${this.baseUrl}/api/spaces/available`;
    return await FetchClient.get<SpaceResponse[]>(url);
  };

  /**
   * Obtiene un espacio específico por su código.
   * @param code - Código del espacio a buscar
   * @returns Promise con el espacio encontrado
   */
  getSpaceByCode = async (code: string): Promise<SpaceResponse> => {
    const url = `${this.baseUrl}/api/spaces`;
    const allSpaces = await FetchClient.get<SpaceResponse[]>(url);
    
    const space = allSpaces.find(s => s.code === code);
    if (!space) {
      throw new Error(`Espacio con código ${code} no encontrado`);
    }
    
    return space;
  };

  /**
   * Actualiza el estado de un espacio (por ejemplo, marcarlo como ocupado).
   * @param spaceId - ID del espacio a actualizar
   * @param status - Nuevo estado (AVAILABLE, OCCUPIED, MAINTENANCE)
   * @returns Promise con el espacio actualizado
   */
  updateSpaceStatus = async (spaceId: string, status: string): Promise<SpaceResponse> => {
    const url = `${this.baseUrl}/api/spaces/${spaceId}/status?status=${status}`;
    return await FetchClient.patch<SpaceResponse>(url, null);
  };

  /**
   * Obtiene espacios por zona específica.
   * @param zoneId - ID de la zona
   * @returns Promise con array de espacios de la zona
   */
  getSpacesByZone = async (zoneId: string): Promise<SpaceResponse[]> => {
    const url = `${this.baseUrl}/api/spaces/zone/${zoneId}`;
    return await FetchClient.get<SpaceResponse[]>(url);
  };
}