package ec.edu.espe.ms_clientes.model;

import ec.edu.espe.ms_clientes.util.ValidadorIdentificaciones;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ec.edu.espe.ms_clientes.model.Persona;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "persona_juridica")
@DiscriminatorValue("JURIDICA")
@PrimaryKeyJoinColumn(name = "persona_id")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class PersonaJuridica extends Persona{
    @Column(unique = true, nullable = false)
    private String razonSocial;

    @Column(nullable = false)
    private LocalDate fechaConstitucion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActividadEconomica actividadEconomica;

    @Column(nullable = false)
    private String representanteLegal;

    @Override
    public boolean validarIdentificacion() {
        return ValidadorIdentificaciones.validarRucPersonaJuridica(this.getIdentificacion());
    }
}
