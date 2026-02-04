package ec.edu.espe.ms_clientes.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true) // Comparar tambien los campos de la clase padre
public class PersonaJuridicaRequestDto extends PersonaRequestDto {
    @NotBlank(message = "La razón social es obligatoria")
    private String razonSocial;

    @NotNull(message = "La fecha de constitución es obligatoria")
    @Past(message = "La fecha de constitución debe ser una fecha pasada")
    private LocalDate fechaConstitucion;

    @Pattern(regexp = "AGRICULTURA|GANADERIA|MINERIA|PESCA|PETROLEO|OTRO",message = "La actividad económica no es válida (AGRICULTURA, GANADERIA, OTRO...)")
    private String actividadEconomica;

    @NotBlank(message = "El representante legal es obligatorio")
    private String representanteLegal;
}
