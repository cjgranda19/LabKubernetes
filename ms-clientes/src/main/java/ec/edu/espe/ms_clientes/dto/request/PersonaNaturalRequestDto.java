package ec.edu.espe.ms_clientes.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class PersonaNaturalRequestDto extends PersonaRequestDto{
    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 30, message = "El apellido debe tener entre 2 y 30 caracteres")
    private String apellido;

    @Pattern(regexp = "MASCULINO|FEMENINO|OTRO", message = "El género debe ser (Femenino), (Masculino) u(Otro)")
    private String genero;

    @Past(message = "La fecha de nacimiento debe ser una fecha menor a la actual")
    private LocalDate fechaNacimiento;
}
