"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.ZoneService = void 0;
const fetchClient_1 = require("../utils/fetchClient");
/**
 * Servicio para interactuar con el microservicio de Zonas y Espacios.
 * Proporciona métodos para obtener información sobre zonas y espacios disponibles.
 */
class ZoneService {
    /**
     * Constructor que inicializa la URL base del microservicio desde variables de entorno.
     */
    constructor() {
        /**
         * Obtiene todas las zonas disponibles del microservicio.
         * @returns Promise con array de zonas
         */
        this.getAllZones = async () => {
            const url = `${this.baseUrl}/api/zones`;
            return await fetchClient_1.FetchClient.get(url);
        };
        /**
         * Obtiene una zona específica por su ID.
         * @param zoneId - ID de la zona a buscar
         * @returns Promise con la zona encontrada
         */
        this.getZoneById = async (zoneId) => {
            const url = `${this.baseUrl}/api/zones/${zoneId}`;
            return await fetchClient_1.FetchClient.get(url);
        };
        /**
         * Obtiene todos los espacios disponibles (no ocupados ni reservados).
         * @returns Promise con array de espacios disponibles
         */
        this.getAvailableSpaces = async () => {
            const url = `${this.baseUrl}/api/spaces/available`;
            return await fetchClient_1.FetchClient.get(url);
        };
        /**
         * Obtiene un espacio específico por su código.
         * @param code - Código del espacio a buscar
         * @returns Promise con el espacio encontrado
         */
        this.getSpaceByCode = async (code) => {
            const url = `${this.baseUrl}/api/spaces`;
            const allSpaces = await fetchClient_1.FetchClient.get(url);
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
        this.updateSpaceStatus = async (spaceId, status) => {
            const url = `${this.baseUrl}/api/spaces/${spaceId}/status?status=${status}`;
            return await fetchClient_1.FetchClient.patch(url, {});
        };
        /**
         * Obtiene espacios por zona específica.
         * @param zoneId - ID de la zona
         * @returns Promise con array de espacios de la zona
         */
        this.getSpacesByZone = async (zoneId) => {
            const url = `${this.baseUrl}/api/spaces/zone/${zoneId}`;
            return await fetchClient_1.FetchClient.get(url);
        };
        this.baseUrl = process.env.ZONE_SERVICE_URL || 'http://localhost:8080';
        if (!this.baseUrl) {
            throw new Error('ZONE_SERVICE_URL no está definida en las variables de entorno');
        }
    }
}
exports.ZoneService = ZoneService;
