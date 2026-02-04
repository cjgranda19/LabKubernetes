package ec.edu.espe.ms_clientes.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MotoRequestDto extends VehiculoRequestDto {
    @Pattern(regexp = "SCOOTER|DEPORTIVA|CHOPPER|CRUCERO|NAKED|OTRO", message = "Tipo de moto invalido")
    private String tipo;

    @NotNull(message = "Debe especificar si tiene casco o no")
    private Boolean tieneCasco;
}
