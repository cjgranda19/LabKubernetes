export interface ZoneResponse {
    id: string;
    name: string;
    description: string;
    capacity: number;
    type: string;
    isActive: boolean;
}

export interface SpaceResponse {
    id: string;
    code: string;
    status: string;
    isReserved: boolean;
    zoneId: string;
    zoneName: string;
    priority: number;
}

export interface PersonaResponse{
    id: string;
    identification: string;
    nombre: string;
    email: string;
    telefono: string;
    tipoPersona: string;
    fechaCreacion: String;
    activo: boolean;
}

export interface VehiculoResponse{
    id: string;
    placa: string;
    marca: string;
    modelo: string;
    color: string;
    anioFabricacion: number;
    tipoVehiculo: string;
    propietario: string;
    activo: boolean;
}