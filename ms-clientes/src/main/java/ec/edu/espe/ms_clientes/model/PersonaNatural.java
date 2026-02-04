package ec.edu.espe.ms_clientes.model;

import ec.edu.espe.ms_clientes.util.ValidadorIdentificaciones;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "persona_natural")
@DiscriminatorValue("NATURAL")
@PrimaryKeyJoinColumn(name = "persona_id")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class PersonaNatural extends Persona {

    @Column(nullable = false)
    private String apellido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Genero genero;

    @Column(nullable = false)
    private LocalDate fechaNacimiento;


    @Override
    public boolean validarIdentificacion() {
        return ValidadorIdentificaciones.validarCedula(this.getIdentificacion());
    }
}
