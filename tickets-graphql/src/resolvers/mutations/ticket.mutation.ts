import { TicketService } from '../../services/ticket.service';

// Instancia del servicio de tickets (singleton)
const ticketService = new TicketService();

/**
 * Resolvers para las mutaciones (Mutations) GraphQL.
 * Cada función corresponde a una mutación definida en el schema.
 */
export const ticketMutations = {
  /**
   * Resuelve la mutación 'emitirTicket' - Crea un nuevo ticket
   */
  emitirTicket: async (
    _: any,
    args: { personaIdentificacion: string; vehiculoPlaca: string; zonaId?: string }
  ) => {
    try {
      return await ticketService.emitirTicket(
        args.personaIdentificacion,
        args.vehiculoPlaca,
        args.zonaId
      );
    } catch (error: unknown) {
      console.error('Error detallado al emitir ticket:', error);
      const message = error instanceof Error ? error.message : JSON.stringify(error);
      throw new Error(`Error al emitir ticket: ${message}`);
    }
  },

  /**
   * Resuelve la mutación 'cerrarTicket' - Cierra un ticket existente
   */
  cerrarTicket: async (_: any, args: { ticketId: string }) => {
    try {
      return await ticketService.cerrarTicket(args.ticketId);
    } catch (error: unknown) {
      const message = error instanceof Error ? error.message : String(error);
      throw new Error(`Error al cerrar ticket: ${message}`);
    }
  },

  /**
   * Resuelve la mutación 'anularTicket' - Anula un ticket existente
   */
  anularTicket: async (_: any, args: { ticketId: string }) => {
    try {
      return await ticketService.anularTicket(args.ticketId);
    } catch (error: unknown) {
      const message = error instanceof Error ? error.message : String(error);
      throw new Error(`Error al anular ticket: ${message}`);
    }
  },
};