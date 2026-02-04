import { ticketQueries } from './queries/ticket.query';
import { ticketMutations } from './mutations/ticket.mutation';

/**
 * Resolvers combinados para el servidor GraphQL.
 * Agrupa todas las consultas y mutaciones en un solo objeto.
 */
export const resolvers = {
  Query: {
    ...ticketQueries,
  },
  Mutation: {
    ...ticketMutations,
  },
  Ticket: {
    // Resolver de campo: mapea fechaHoraIngreso a fechaIngreso
    fechaIngreso: (parent: any) => parent.fechaHoraIngreso?.toISOString() || null,
    // Resolver de campo: mapea fechaHoraSalida a fechaSalida
    fechaSalida: (parent: any) => parent.fechaHoraSalida?.toISOString() || null,
  },
};