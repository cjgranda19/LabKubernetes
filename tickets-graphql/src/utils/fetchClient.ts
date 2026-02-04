import fetch, { RequestInit, Response } from "node-fetch";

/**
 * Interfaz para la respuesta de error de la API
 */
interface ApiError {
  message: string;
  status?: number;
}

/**
 * Cliente HTTP reutilizable basado en Fetch para consumir APIs REST.
 * Proporciona métodos para realizar peticiones HTTP con manejo de errores.
 */
export class FetchClient {
  /**
   * Realiza una petición GET a la URL especificada.
   * @param url - URL del endpoint
   * @param options - Opciones adicionales de la petición
   * @returns Promise con los datos de la respuesta
   */
  static get = async <T = any>(
    url: string,
    options: RequestInit = {},
  ): Promise<T> => {
    const response = await this.fetchWithErrorHandling(url, {
      method: "GET",
      ...options,
    });
    return response as T;
  };

  /**
   * Realiza una petición POST a la URL especificada.
   * @param url - URL del endpoint
   * @param body - Datos a enviar en el cuerpo de la petición
   * @param options - Opciones adicionales de la petición
   * @returns Promise con los datos de la respuesta
   */
  static post = async <T = any>(
    url: string,
    body: any,
    options: RequestInit = {},
  ): Promise<T> => {
    const response = await this.fetchWithErrorHandling(url, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        ...options.headers,
      },
      body: JSON.stringify(body),
      ...options,
    });
    return response as T;
  };

  /**
   * Realiza una petición PUT a la URL especificada.
   * @param url - URL del endpoint
   * @param body - Datos a enviar en el cuerpo de la petición
   * @param options - Opciones adicionales de la petición
   * @returns Promise con los datos de la respuesta
   */
  static put = async <T = any>(
    url: string,
    body: any,
    options: RequestInit = {},
  ): Promise<T> => {
    const response = await this.fetchWithErrorHandling(url, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        ...options.headers,
      },
      body: JSON.stringify(body),
      ...options,
    });
    return response as T;
  };

  /**
   * Realiza una petición PATCH a la URL especificada.
   * @param url - URL del endpoint
   * @param body - Datos a enviar en el cuerpo de la petición (null para no enviar body)
   * @param options - Opciones adicionales de la petición
   * @returns Promise con los datos de la respuesta
   */
  static patch = async <T = any>(
    url: string,
    body: any,
    options: RequestInit = {},
  ): Promise<T> => {
    const requestOptions: RequestInit = {
      method: "PATCH",
      ...options,
    };
    
    if (body !== null && body !== undefined) {
      requestOptions.headers = {
        "Content-Type": "application/json",
        ...options.headers,
      };
      requestOptions.body = JSON.stringify(body);
    }
    
    const response = await this.fetchWithErrorHandling(url, requestOptions);
    return response as T;
  };

  /**
   * Realiza una petición DELETE a la URL especificada.
   * @param url - URL del endpoint
   * @param options - Opciones adicionales de la petición
   * @returns Promise con los datos de la respuesta
   */
  static delete = async <T = any>(
    url: string,
    options: RequestInit = {},
  ): Promise<T> => {
    const response = await this.fetchWithErrorHandling(url, {
      method: "DELETE",
      ...options,
    });
    return response as T;
  };

  /**
   * Método interno para manejar peticiones fetch con manejo de errores.
   * @param url - URL del endpoint
   * @param options - Opciones de la petición
   * @returns Promise con los datos de la respuesta
   * @throws Error si la petición falla
   */
  private static fetchWithErrorHandling = async (
    url: string,
    options: RequestInit = {},
  ): Promise<any> => {
    let response: Response;

    try {
      // Realizar la petición fetch
      response = await fetch(url, {
        ...options,
        headers: {
          "Content-Type": "application/json",
          ...options.headers,
        },
      });
    } catch (error: unknown) {
      // Error de conexión
      const message =
        error instanceof Error ? error.message : String(error);

      throw {
        message: `Error de conexión: ${message}`,
        status: 500,
      } as ApiError;
    }

    // Verificar si la respuesta fue exitosa
    if (!response.ok) {
      let errorMessage: string;

      try {
        // Intentar obtener mensaje de error del cuerpo de la respuesta
        const errorData = await response.json();
        errorMessage =
          errorData.message || errorData.error || response.statusText;
      } catch {
        // Si no se puede parsear JSON, usar el texto de estado
        errorMessage = response.statusText;
      }

      throw {
        message: errorMessage,
        status: response.status,
      } as ApiError;
    }

    // Intentar parsear la respuesta como JSON
    try {
      // Para respuestas DELETE sin contenido, retornar un objeto vacío
      if (response.status === 204) {
        return {};
      }

      const data = await response.json();
      return data;
    } catch (error: unknown) {
      // Si no hay contenido JSON, retornar objeto vacío
      return {};
    }
  };
}