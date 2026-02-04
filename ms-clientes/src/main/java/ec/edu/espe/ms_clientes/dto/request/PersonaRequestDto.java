package ec.edu.espe.ms_clientes.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

//Aqui van las validaciones de las entradas
@Data
public class PersonaRequestDto {
    @NotBlank(message = "No se permiten campos vacíos") // no admite valores en blanco
    @Size(min = 10, max = 13, message = "La identificación debe tener entre 10 (CC) y 13 caracteres (RUC)")
    @Pattern(regexp = "\\d+", message = "La identificación debe contener solo números")
    private String identificacion;
    private String nombre;
    private String email;

    @Size(max = 100, message = "Se permiten solo 100 caracteres")
    private String direccion;

    @Pattern(regexp = "[0-9+\\-]+", message = "El teléfono contiene caracteres inválidos")
    private String telefono;
}
