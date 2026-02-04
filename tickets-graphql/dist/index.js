"use strict";
var __createBinding = (this && this.__createBinding) || (Object.create ? (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    var desc = Object.getOwnPropertyDescriptor(m, k);
    if (!desc || ("get" in desc ? !m.__esModule : desc.writable || desc.configurable)) {
      desc = { enumerable: true, get: function() { return m[k]; } };
    }
    Object.defineProperty(o, k2, desc);
}) : (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
}));
var __setModuleDefault = (this && this.__setModuleDefault) || (Object.create ? (function(o, v) {
    Object.defineProperty(o, "default", { enumerable: true, value: v });
}) : function(o, v) {
    o["default"] = v;
});
var __importStar = (this && this.__importStar) || (function () {
    var ownKeys = function(o) {
        ownKeys = Object.getOwnPropertyNames || function (o) {
            var ar = [];
            for (var k in o) if (Object.prototype.hasOwnProperty.call(o, k)) ar[ar.length] = k;
            return ar;
        };
        return ownKeys(o);
    };
    return function (mod) {
        if (mod && mod.__esModule) return mod;
        var result = {};
        if (mod != null) for (var k = ownKeys(mod), i = 0; i < k.length; i++) if (k[i] !== "default") __createBinding(result, mod, k[i]);
        __setModuleDefault(result, mod);
        return result;
    };
})();
Object.defineProperty(exports, "__esModule", { value: true });
const server_1 = require("@apollo/server");
const standalone_1 = require("@apollo/server/standalone");
const schema_1 = require("./typeDefs/schema");
const resolvers_1 = require("./resolvers");
const database_1 = require("./utils/database");
const dotenv = __importStar(require("dotenv"));
/**
 * Función principal que inicia el servidor GraphQL.
 * Configura Apollo Server, inicializa la base de datos y arranca el servidor.
 */
const startServer = async () => {
    // 1. Cargar variables de entorno
    dotenv.config();
    // 2. Validar variables de entorno requeridas
    const requiredEnvVars = ['ZONE_SERVICE_URL', 'PERSONA_SERVICE_URL'];
    for (const envVar of requiredEnvVars) {
        if (!process.env[envVar]) {
            throw new Error(`Variable de entorno requerida no encontrada: ${envVar}`);
        }
    }
    // 3. Inicializar conexión a la base de datos
    console.log('🔄 Inicializando conexión a la base de datos...');
    await (0, database_1.initializeDatabase)();
    // 4. Configurar Apollo Server
    const server = new server_1.ApolloServer({
        typeDefs: schema_1.typeDefs,
        resolvers: resolvers_1.resolvers,
        introspection: process.env.NODE_ENV !== 'production', // Habilitar introspection en desarrollo
        formatError: (error) => {
            // Formatear errores para respuesta más clara
            console.error('GraphQL Error:', error);
            return {
                message: error.message,
                extensions: {
                    code: error.extensions?.code || 'INTERNAL_SERVER_ERROR',
                },
            };
        },
    });
    // 5. Iniciar servidor en modo standalone
    const port = parseInt(process.env.PORT || '4000');
    const { url } = await (0, standalone_1.startStandaloneServer)(server, {
        listen: { port },
        context: async ({ req }) => ({
            // Aquí se puede agregar autenticación/autorización
            token: req.headers.authorization || '',
        }),
    });
    // 6. Mensaje de inicio exitoso
    console.log(`
🚀 Servidor GraphQL iniciado exitosamente!
📡 URL del servidor: ${url}
🗄️  Base de datos: ${process.env.DB_NAME}
🔌 Microservicio Zonas: ${process.env.ZONE_SERVICE_URL}
👥 Microservicio Personas: ${process.env.PERSONA_SERVICE_URL}

📚 Endpoints disponibles:
   - GraphQL API: ${url}
   - Playground GraphQL: ${url} (disponible en desarrollo)

📝 Consultas de ejemplo:
   query {
     tickets {
       codigoTicket
       personaNombre
       vehiculoPlaca
       estado
     }
   }

   mutation {
     emitirTicket(
       personaIdentificacion: "1712345678"
       vehiculoPlaca: "ABC-123"
     ) {
       codigoTicket
       espacioCodigo
     }
   }
  `);
};
/**
 * Manejo de errores no capturados
 */
process.on('unhandledRejection', (error) => {
    console.error('❌ Error no manejado:', error);
    process.exit(1);
});
process.on('uncaughtException', (error) => {
    console.error('❌ Excepción no capturada:', error);
    process.exit(1);
});
// Iniciar el servidor
startServer().catch((error) => {
    console.error('❌ Error fatal al iniciar el servidor:', error);
    process.exit(1);
});
