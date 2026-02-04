package ec.edu.espe.ms_clientes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "moto")
@EqualsAndHashCode(callSuper = false)
@DiscriminatorValue("MOTO")
@PrimaryKeyJoinColumn(name = "vehiculo_id")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder 
@Data
public class Moto extends Vehiculo {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMoto tipo;

    @Column(nullable = false)
    private boolean tieneCasco;
}
