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
exports.initializeDatabase = exports.AppDataSource = void 0;
const typeorm_1 = require("typeorm");
const Ticket_entity_1 = require("../entities/Ticket.entity");
const dotenv = __importStar(require("dotenv"));
dotenv.config(); // Cargar variables de entorno
/**
 * Configuración de la conexión a la base de datos PostgreSQL usando TypeORM.
 * Esta configuración utiliza variables de entorno para mayor seguridad y flexibilidad.
 */
exports.AppDataSource = new typeorm_1.DataSource({
    type: "postgres", // Tipo de base de datos
    host: process.env.DB_HOST, // Dirección del servidor de base de datos
    port: parseInt(process.env.DB_PORT || "5432"), // Puerto de conexión
    username: process.env.DB_USERNAME, // Usuario de la base de datos
    password: process.env.DB_PASSWORD, // Contraseña del usuario
    database: process.env.DB_NAME, // Nombre de la base de datos
    synchronize: true, // Sincronizar esquemas automáticamente (solo para desarrollo)
    logging: false, // Desactivar logs de SQL para producción
    entities: [Ticket_entity_1.Ticket], // Entidades a mapear
    subscribers: [],
    migrations: [],
});
/**
 * Inicializa la conexión a la base de datos.
 * Debe ser llamado al inicio de la aplicación.
 * @returns Promise que se resuelve cuando la conexión está lista
 */
const initializeDatabase = async () => {
    try {
        await exports.AppDataSource.initialize();
        console.log("Base de datos conectada exitosamente");
    }
    catch (error) {
        console.error("Error al conectar a la base de datos:", error);
        process.exit(1); // Terminar aplicación si no hay conexión a BD
    }
};
exports.initializeDatabase = initializeDatabase;
