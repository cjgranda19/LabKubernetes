"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.resolvers = void 0;
const ticket_query_1 = require("./queries/ticket.query");
const ticket_mutation_1 = require("./mutations/ticket.mutation");
/**
 * Resolvers combinados para el servidor GraphQL.
 * Agrupa todas las consultas y mutaciones en un solo objeto.
 */
exports.resolvers = {
    Query: {
        ...ticket_query_1.ticketQueries,
    },
    Mutation: {
        ...ticket_mutation_1.ticketMutations,
    },
};
