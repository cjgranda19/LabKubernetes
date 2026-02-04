"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
var _a;
Object.defineProperty(exports, "__esModule", { value: true });
exports.FetchClient = void 0;
const node_fetch_1 = __importDefault(require("node-fetch"));
/**
 * Cliente HTTP reutilizable basado en Fetch para consumir APIs REST.
 * Proporciona métodos para realizar peticiones HTTP con manejo de errores.
 */
class FetchClient {
}
exports.FetchClient = FetchClient;
_a = FetchClient;
/**
 * Realiza una petición GET a la URL especificada.
 * @param url - URL del endpoint
 * @param options - Opciones adicionales de la petición
 * @returns Promise con los datos de la respuesta
 */
FetchClient.get = async (url, options = {}) => {
    const response = await _a.fetchWithErrorHandling(url, {
        method: "GET",
        ...options,
    });
    return response;
};
/**
 * Realiza una petición POST a la URL especificada.
 * @param url - URL del endpoint
 * @param body - Datos a enviar en el cuerpo de la petición
 * @param options - Opciones adicionales de la petición
 * @returns Promise con los datos de la respuesta
 */
FetchClient.post = async (url, body, options = {}) => {
    const response = await _a.fetchWithErrorHandling(url, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            ...options.headers,
        },
        body: JSON.stringify(body),
        ...options,
    });
    return response;
};
/**
 * Realiza una petición PUT a la URL especificada.
 * @param url - URL del endpoint
 * @param body - Datos a enviar en el cuerpo de la petición
 * @param options - Opciones adicionales de la petición
 * @returns Promise con los datos de la respuesta
 */
FetchClient.put = async (url, body, options = {}) => {
    const response = await _a.fetchWithErrorHandling(url, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            ...options.headers,
        },
        body: JSON.stringify(body),
        ...options,
    });
    return response;
};
/**
 * Realiza una petición PATCH a la URL especificada.
 * @param url - URL del endpoint
 * @param body - Datos a enviar en el cuerpo de la petición
 * @param options - Opciones adicionales de la petición
 * @returns Promise con los datos de la respuesta
 */
FetchClient.patch = async (url, body, options = {}) => {
    const response = await _a.fetchWithErrorHandling(url, {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json",
            ...options.headers,
        },
        body: JSON.stringify(body),
        ...options,
    });
    return response;
};
/**
 * Realiza una petición DELETE a la URL especificada.
 * @param url - URL del endpoint
 * @param options - Opciones adicionales de la petición
 * @returns Promise con los datos de la respuesta
 */
FetchClient.delete = async (url, options = {}) => {
    const response = await _a.fetchWithErrorHandling(url, {
        method: "DELETE",
        ...options,
    });
    return response;
};
/**
 * Método interno para manejar peticiones fetch con manejo de errores.
 * @param url - URL del endpoint
 * @param options - Opciones de la petición
 * @returns Promise con los datos de la respuesta
 * @throws Error si la petición falla
 */
FetchClient.fetchWithErrorHandling = async (url, options = {}) => {
    let response;
    try {
        // Realizar la petición fetch
        response = await (0, node_fetch_1.default)(url, {
            ...options,
            headers: {
                "Content-Type": "application/json",
                ...options.headers,
            },
        });
    }
    catch (error) {
        // Error de conexión
        const message = error instanceof Error ? error.message : String(error);
        throw {
            message: `Error de conexión: ${message}`,
            status: 500,
        };
    }
    // Verificar si la respuesta fue exitosa
    if (!response.ok) {
        let errorMessage;
        try {
            // Intentar obtener mensaje de error del cuerpo de la respuesta
            const errorData = await response.json();
            errorMessage =
                errorData.message || errorData.error || response.statusText;
        }
        catch {
            // Si no se puede parsear JSON, usar el texto de estado
            errorMessage = response.statusText;
        }
        throw {
            message: errorMessage,
            status: response.status,
        };
    }
    // Intentar parsear la respuesta como JSON
    try {
        // Para respuestas DELETE sin contenido, retornar un objeto vacío
        if (response.status === 204) {
            return {};
        }
        const data = await response.json();
        return data;
    }
    catch (error) {
        // Si no hay contenido JSON, retornar objeto vacío
        return {};
    }
};
