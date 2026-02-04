package ec.edu.espe.ms_clientes.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class VehiculoResponseDto {
    private UUID id;
    private String placa;
    private String marca;
    private String modelo;
    private String color;
    private Integer anioFabricacion;
    private String tipoVehiculo;
    private UUID propietarioId;
    private String propietarioNombre;
    private boolean activo;
}
