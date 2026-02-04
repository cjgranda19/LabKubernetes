package ec.edu.espe.ms_clientes;

import ec.edu.espe.ms_clientes.model.ActividadEconomica;
import ec.edu.espe.ms_clientes.model.Genero;
import ec.edu.espe.ms_clientes.model.PersonaJuridica;
import ec.edu.espe.ms_clientes.model.PersonaNatural;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.UUID;


//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}) // Apago la configuracion de la base de datos por ahora, para poder ejecutar en consola
@SpringBootApplication
public class MsClientesApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsClientesApplication.class, args);
    }


/*
    @Bean
    CommandLineRunner ejecutarPruebas() {
        return args -> {
            String cedulaPrueba = "1315110534";
            PersonaNatural personaNatural = new PersonaNatural();
            personaNatural.setId(UUID.randomUUID());
            personaNatural.setNombre("Erick");
            personaNatural.setApellido("Moreira");
            personaNatural.setIdentificacion(cedulaPrueba);
            personaNatural.setTelefono("0979961278");
            personaNatural.setDireccion("La ESPE");
            personaNatural.setEmail("epmoreira2@espe.edu.ec");
            personaNatural.setFechaNacimiento(LocalDate.of(2002, 8, 12));

            personaNatural.setGenero(Genero.MASCULINO);

            System.out.println("Género: " + personaNatural.getGenero());
            boolean esCedulaValida = personaNatural.validarIdentificacion();
            System.out.println("Cédula válida: " + esCedulaValida);


            String rucPrueba = "1790016919001"; // RUC del supermaxi
            PersonaJuridica personaJuridica = new PersonaJuridica();
            personaJuridica.setId(UUID.randomUUID());
            personaJuridica.setNombre("CORPORACION FAVORITA S.A.");
            personaJuridica.setRazonSocial("CORPORACION FAVORITA S.A.");
            personaJuridica.setIdentificacion(rucPrueba);
            personaJuridica.setTelefono("0998765432");
            personaJuridica.setDireccion("Av. Amazonas");
            personaJuridica.setEmail("favorita@gmail.com");
            personaJuridica.setRepresentanteLegal("Sr. Gerente General");
            personaJuridica.setFechaConstitucion(LocalDate.of(1952, 5, 8));

            personaJuridica.setActividadEconomica(ActividadEconomica.OTRO);

            boolean esRucValido = personaJuridica.validarIdentificacion();
            System.out.println("Actividad: " + personaJuridica.getActividadEconomica());
            System.out.println("RUC Válido: " + esRucValido);

        };
    }
*/
}