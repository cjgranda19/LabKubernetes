"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.ticketQueries = void 0;
const ticket_service_1 = require("../../services/ticket.service");
// Instancia del servicio de tickets (singleton)
const ticketService = new ticket_service_1.TicketService();
/**
 * Resolvers para las consultas (Queries) GraphQL.
 * Cada función corresponde a una consulta definida en el schema.
 */
exports.ticketQueries = {
    /**
     * Resuelve la consulta 'tickets' - Obtiene todos los tickets
     */
    tickets: async () => {
        try {
            return await ticketService.getAllTickets();
        }
        catch (error) {
            const message = error instanceof Error ? error.message : String(error);
            throw new Error(`Error al obtener tickets: ${message}`);
        }
    },
    /**
     * Resuelve la consulta 'ticket' - Obtiene un ticket por código
     */
    ticket: async (_, args) => {
        try {
            return await ticketService.getTicketByCodigo(args.codigoTicket);
        }
        catch (error) {
            const message = error instanceof Error ? error.message : String(error);
            throw new Error(`Error al obtener ticket: ${message}`);
        }
    },
    /**
     * Resuelve la consulta 'ticketsByPersona' - Obtiene tickets por identificación
     */
    ticketsByPersona: async (_, args) => {
        try {
            return await ticketService.getTicketsByPersona(args.identificacion);
        }
        catch (error) {
            const message = error instanceof Error ? error.message : String(error);
            throw new Error(`Error al obtener tickets de la persona: ${message}`);
        }
    },
    /**
     * Resuelve la consulta 'ticketsByVehiculo' - Obtiene tickets por placa
     */
    ticketsByVehiculo: async (_, args) => {
        try {
            return await ticketService.getTicketsByVehiculo(args.placa);
        }
        catch (error) {
            const message = error instanceof Error ? error.message : String(error);
            throw new Error(`Error al obtener tickets del vehículo: ${message}`);
        }
    },
    /**
     * Resuelve la consulta 'ticketsActivos' - Obtiene solo tickets activos
     */
    ticketsActivos: async () => {
        try {
            return await ticketService.getActiveTickets();
        }
        catch (error) {
            const message = error instanceof Error ? error.message : String(error);
            throw new Error(`Error al obtener tickets activos: ${message}`);
        }
    },
    /**
     * Resuelve la consulta 'estadisticasTickets' - Obtiene estadísticas
     */
    estadisticasTickets: async () => {
        try {
            const allTickets = await ticketService.getAllTickets();
            const activos = await ticketService.getActiveTickets();
            const cerrados = allTickets.filter(t => t.estado === 'CERRADO');
            const anulados = allTickets.filter(t => t.estado === 'ANULADO');
            // Calcular tiempo promedio de estacionamiento
            const ticketsConTiempo = cerrados.filter(t => t.tiempoEstacionado);
            const promedio = ticketsConTiempo.length > 0
                ? ticketsConTiempo.reduce((sum, t) => sum + (t.tiempoEstacionado || 0), 0) / ticketsConTiempo.length
                : 0;
            return {
                total: allTickets.length,
                activos: activos.length,
                cerrados: cerrados.length,
                anulados: anulados.length,
                promedioTiempoMinutos: promedio,
            };
        }
        catch (error) {
            const message = error instanceof Error ? error.message : String(error);
            throw new Error(`Error al obtener estadísticas: ${message}`);
        }
    },
};
