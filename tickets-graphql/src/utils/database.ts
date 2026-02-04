import { DataSource } from "typeorm";
import { Ticket } from "../entities/Ticket.entity";
import * as dotenv from "dotenv";

dotenv.config(); // Cargar variables de entorno

/**
 * Configuración de la conexión a la base de datos PostgreSQL usando TypeORM.
 * Esta configuración utiliza variables de entorno para mayor seguridad y flexibilidad.
 */
export const AppDataSource = new DataSource({
  type: "postgres", // Tipo de base de datos
  host: process.env.DB_HOST, // Dirección del servidor de base de datos
  port: parseInt(process.env.DB_PORT || "5432"), // Puerto de conexión
  username: process.env.DB_USERNAME, // Usuario de la base de datos
  password: process.env.DB_PASSWORD, // Contraseña del usuario
  database: process.env.DB_NAME, // Nombre de la base de datos
  synchronize: true, // Sincronizar esquemas automáticamente (solo para desarrollo)
  logging: false, // Desactivar logs de SQL para producción
  entities: [Ticket], // Entidades a mapear
  subscribers: [],
  migrations: [],
});

/**
 * Inicializa la conexión a la base de datos.
 * Debe ser llamado al inicio de la aplicación.
 * @returns Promise que se resuelve cuando la conexión está lista
 */
export const initializeDatabase = async (): Promise<void> => {
  try {
    await AppDataSource.initialize();
    console.log("Base de datos conectada exitosamente");
  } catch (error) {
    console.error("Error al conectar a la base de datos:", error);
    process.exit(1); // Terminar aplicación si no hay conexión a BD
  }
};