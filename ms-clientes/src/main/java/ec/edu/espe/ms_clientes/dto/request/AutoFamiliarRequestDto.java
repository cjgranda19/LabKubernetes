package ec.edu.espe.ms_clientes.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AutoFamiliarRequestDto extends VehiculoRequestDto {
    @Pattern(regexp = "SEDAN|SUV|HATCHBACK|CAMIONETA|DEPORTIVO|MINIVAN|OTRO|", message = "Tipo de auto invalido")
    private String tipo;

    @Pattern(regexp = "GASOLINA|DIESEL|ELECTRICO|HIBRIDO", message = "Tipo de combustible invalido")
    private String combustible;

    @NotNull(message = "La capacidad del maletero es obligatoria")
    @Min(value = 0, message = "La capacidad no puede ser negativa")
    private Integer capacidadMaletero;

    @NotNull(message = "El número de ocupantes es obligatorio")
    @Min(value = 1, message = "Debe tener al menos 1 ocupante")
    @Max(value = 10, message = "Excede el limite de ocupantes")
    private Integer ocupantes;
}
