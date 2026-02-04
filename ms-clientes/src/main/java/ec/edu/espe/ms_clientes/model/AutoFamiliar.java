package ec.edu.espe.ms_clientes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "auto_familiar")
@EqualsAndHashCode(callSuper = false) // Para que no tome en cuenta los atributos de la clase padre Vehiculo
@DiscriminatorValue("AUTO FAMILIAR")
@PrimaryKeyJoinColumn(name = "vehiculo_id")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder // ver que hace esto
@Data
public class AutoFamiliar extends Vehiculo {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAuto tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCombustible combustible;

    @Column(nullable = false)
    private Integer capacidadMaletero; // en Litros

    @Column(nullable = false)
    private Integer ocupantes;
}
