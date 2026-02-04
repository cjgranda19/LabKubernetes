import { ApolloServer } from '@apollo/server';
import { startStandaloneServer } from '@apollo/server/standalone';
import { typeDefs } from './typeDefs/schema';
import { resolvers } from './resolvers';
import { initializeDatabase } from './utils/database';
import { RabbitMQService } from './services/rabbitmq.service';
import * as dotenv from 'dotenv';

// Instancia global de RabbitMQ
let rabbitMQService: RabbitMQService;

/**
 * Función principal que inicia el servidor GraphQL.
 * Configura Apollo Server, inicializa la base de datos y arranca el servidor.
 */
const startServer = async (): Promise<void> => {
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
  await initializeDatabase();

  // 3.5. Inicializar conexión a RabbitMQ
  console.log('🔄 Conectando a RabbitMQ...');
  try {
    rabbitMQService = new RabbitMQService();
    await rabbitMQService.connect();
  } catch (error) {
    console.warn('⚠️ No se pudo conectar a RabbitMQ. Las notificaciones no estarán disponibles.', error);
  }

  // 4. Configurar Apollo Server
  const server = new ApolloServer({
    typeDefs,
    resolvers,
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
  const { url } = await startStandaloneServer(server, {
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

/**
 * Limpieza al cerrar el servidor
 */
process.on('SIGINT', async () => {
  console.log('\n🛑 Cerrando servidor...');
  if (rabbitMQService) {
    await rabbitMQService.close();
  }
  process.exit(0);
});

process.on('SIGTERM', async () => {
  console.log('\n🛑 Cerrando servidor...');
  if (rabbitMQService) {
    await rabbitMQService.close();
  }
  process.exit(0);
});

// Iniciar el servidor
startServer().catch((error) => {
  console.error('❌ Error fatal al iniciar el servidor:', error);
  process.exit(1);
});