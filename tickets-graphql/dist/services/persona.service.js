"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.PersonaService = void 0;
const fetchClient_1 = require("../utils/fetchClient");
/**
 * Servicio para interactuar con el microservicio de Personas y Vehículos.
 * Proporciona métodos para buscar personas por identificación y vehículos por placa.
 */
class PersonaService {
    /**
     * Constructor que inicializa la URL base del microservicio desde variables de entorno.
     */
    constructor() {
        /**
         * Busca una persona por su número de identificación.
         * @param identificacion - Número de cédula o RUC
         * @returns Promise con la persona encontrada
         */
        this.getPersonaByIdentificacion = async (identificacion) => {
            const url = `${this.baseUrl}/api/personas/identificacion/${identificacion}`;
            return await fetchClient_1.FetchClient.get(url);
        };
        /**
         * Busca un vehículo por su número de placa.
         * @param placa - Número de placa del vehículo
         * @returns Promise con el vehículo encontrado
         */
        this.getVehiculoByPlaca = async (placa) => {
            const url = `${this.baseUrl}/api/vehiculos/placa/${placa}`;
            return await fetchClient_1.FetchClient.get(url);
        };
        /**
         * Verifica si un vehículo pertenece a una persona específica.
         * @param personaId - ID de la persona
         * @param vehiculoId - ID del vehículo
         * @returns Promise con booleano indicando si pertenece
         */
        this.validateVehiculoBelongsToPersona = async (personaId, vehiculoId) => {
            const url = `${this.baseUrl}/api/vehiculos/propietario/${personaId}`;
            const vehiculos = await fetchClient_1.FetchClient.get(url);
            return vehiculos.some(v => v.id === vehiculoId);
        };
        /**
         * Obtiene todos los vehículos de una persona específica.
         * @param personaId - ID de la persona
         * @returns Promise con array de vehículos de la persona
         */
        this.getVehiculosByPersona = async (personaId) => {
            const url = `${this.baseUrl}/api/vehiculos/propietario/${personaId}`;
            return await fetchClient_1.FetchClient.get(url);
        };
        this.baseUrl = process.env.PERSONA_SERVICE_URL || 'http://localhost:8081';
        if (!this.baseUrl) {
            throw new Error('PERSONA_SERVICE_URL no está definida en las variables de entorno');
        }
    }
}
exports.PersonaService = PersonaService;
