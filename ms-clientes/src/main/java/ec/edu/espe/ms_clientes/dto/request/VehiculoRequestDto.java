package ec.edu.espe.ms_clientes.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.UUID;

@Data
public class VehiculoRequestDto {
    @NotBlank(message = "La placa es obligatoria")
    @Size(min = 6, max = 7, message = "La placa debe tener entre 6 y 7 caracteres")
    private String placa;

    @NotNull(message = "El cilindraje es obligatorio")
    @Min(value = 0, message = "El cilindraje minimo es mayor a 0 cc")
    @Max(value = 5000, message = "El cilindraje es excesivo")
    private Integer cilindraje;

    @NotBlank(message = "La marca es obligatoria")
    private String marca;

    @NotBlank(message = "El modelo es obligatorio")
    private String modelo;

    @NotBlank(message = "El color es obligatorio")
    private String color;

    @NotNull(message = "El año de fabricacion es obligatorio")
    @Min(value = 1900, message = "Año inválido")
    @Max(value = 2025, message = "El año no puede ser superior al actual")
    private Integer anioFabricacion;

    @NotNull(message = "El id del propietario es obligatorio")
    private UUID idPropietario; // solo se pide el id del propietario, no todos los datos
}
