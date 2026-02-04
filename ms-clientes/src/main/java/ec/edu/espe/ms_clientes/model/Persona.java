package ec.edu.espe.ms_clientes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "persona")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_persona", discriminatorType = DiscriminatorType.STRING)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //@UuidGenerator
    private UUID id;

    @Column(unique = true)
    private String identificacion;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String telefono;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private boolean activo;

    @Column
    private LocalDateTime fechaCreacion; //registro en la bd

    public abstract boolean validarIdentificacion();

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        //this.activo = false; //Por defecto la persona se crea inactiva
    }
}

//DENTRO DEL MAIN CREAR UNA INSTANCIA DE PERSONA NATURAL Y OTRA DE PERSONA JURIDICA Y VALIDAR LA CEDULA Y EL RUC RESPECTIVAMENTE, ADEMAS DE DEJARME INGRESAR LOS GENEROS DEFINIDOS EN EL ENUM.
//REALIZAR LAS PRUEBAS EN CONSOLA NOMAS.