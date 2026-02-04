"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.ticketMutations = void 0;
const ticket_service_1 = require("../../services/ticket.service");
// Instancia del servicio de tickets (singleton)
const ticketService = new ticket_service_1.TicketService();
/**
 * Resolvers para las mutaciones (Mutations) GraphQL.
 * Cada función corresponde a una mutación definida en el schema.
 */
exports.ticketMutations = {
    /**
     * Resuelve la mutación 'emitirTicket' - Crea un nuevo ticket
     */
    emitirTicket: async (_, args) => {
        try {
            return await ticketService.emitirTicket(args.personaIdentificacion, args.vehiculoPlaca, args.zonaId);
        }
        catch (error) {
            const message = error instanceof Error ? error.message : String(error);
            throw new Error(`Error al emitir ticket: ${message}`);
        }
    },
    /**
     * Resuelve la mutación 'cerrarTicket' - Cierra un ticket existente
     */
    cerrarTicket: async (_, args) => {
        try {
            return await ticketService.cerrarTicket(args.ticketId);
        }
        catch (error) {
            const message = error instanceof Error ? error.message : String(error);
            throw new Error(`Error al cerrar ticket: ${message}`);
        }
    },
    /**
     * Resuelve la mutación 'anularTicket' - Anula un ticket existente
     */
    anularTicket: async (_, args) => {
        try {
            return await ticketService.anularTicket(args.ticketId);
        }
        catch (error) {
            const message = error instanceof Error ? error.message : String(error);
            throw new Error(`Error al anular ticket: ${message}`);
        }
    },
};
