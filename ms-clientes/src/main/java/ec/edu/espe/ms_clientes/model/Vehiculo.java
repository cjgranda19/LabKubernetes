package ec.edu.espe.ms_clientes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "vehiculo")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_vehiculo", discriminatorType = DiscriminatorType.STRING)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class Vehiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private Integer cilindraje;


    @Column(nullable = false, unique = true)
    private String placa;

    @Column(nullable = false)
    private String marca;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private String modelo;

    @Column(nullable = false)
    private Integer anioFabricacion;

    @ManyToOne(fetch = FetchType.LAZY) //Muchos vehiculos pueden pertenecer a una persona
    @JoinColumn(name = "id_persona") // FetchaType.LAZY para cargar los datos del dueño solo cuando se necesite y no cada que se consulte un vehiculo
    private Persona propietario;

    @Column(nullable = false)
    private boolean activo;

    @Column
    private LocalDateTime fechaCreacion; //registro en la bd

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        //this.activo = false; //Por defecto la persona se crea inactiva
    }
}
