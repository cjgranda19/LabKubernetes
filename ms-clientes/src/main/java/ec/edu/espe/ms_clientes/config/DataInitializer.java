package ec.edu.espe.ms_clientes.config;

import ec.edu.espe.ms_clientes.model.*;
import ec.edu.espe.ms_clientes.model.repository.PersonaRepository;
import ec.edu.espe.ms_clientes.model.repository.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final PersonaRepository personaRepository;
    private final VehiculoRepository vehiculoRepository;

    @Override
    public void run(String... args) throws Exception {
        if (personaRepository.count() == 0) {
            System.out.println("--- INICIANDO CARGA DE DATOS SEED ---");

            // Crear Persona Natural
            PersonaNatural persona = new PersonaNatural();
            persona.setIdentificacion("1315110534");
            persona.setNombre("Erick");
            persona.setApellido("Moreira");
            persona.setFechaNacimiento(LocalDate.of(2002, 8, 12));
            persona.setGenero(Genero.MASCULINO);
            persona.setEmail("erick@gmail.com");
            persona.setTelefono("0979961278");
            persona.setDireccion("La Colina");
            persona.setActivo(true);

            Persona personaGuardada = personaRepository.save(persona);

            // Crear Auto Familiar
            AutoFamiliar auto = new AutoFamiliar();
            auto.setPlaca("PBA-1234");
            auto.setMarca("Honda");
            auto.setModelo("Civic");
            auto.setColor("Negro");
            auto.setAnioFabricacion(1998);
            auto.setCilindraje(1700);

            auto.setTipo(TipoAuto.SEDAN);
            auto.setCombustible(TipoCombustible.GASOLINA);
            auto.setCapacidadMaletero(450);
            auto.setOcupantes(5);

            auto.setActivo(true);
            auto.setPropietario(personaGuardada);

            vehiculoRepository.save(auto);

            // --- DATO 2
            PersonaNatural persona2 = new PersonaNatural();
            persona2.setIdentificacion("0920020002");
            persona2.setNombre("Maria");
            persona2.setApellido("Rodriguez");
            persona2.setFechaNacimiento(LocalDate.of(1998, 3, 15));
            persona2.setGenero(Genero.FEMENINO);
            persona2.setEmail("maria.rod@email.com");
            persona2.setTelefono("0988776655");
            persona2.setDireccion("Av. 9 de Octubre, Guayaquil");
            persona2.setActivo(true);
            Persona p2Saved = personaRepository.save(persona2);

            AutoFamiliar auto2 = new AutoFamiliar();
            auto2.setPlaca("GCA-5555");
            auto2.setMarca("Chevrolet");
            auto2.setModelo("Tracker");
            auto2.setColor("Rojo");
            auto2.setAnioFabricacion(2024);
            auto2.setCilindraje(1200); // Turbo
            auto2.setTipo(TipoAuto.SUV);
            auto2.setCombustible(TipoCombustible.GASOLINA);
            auto2.setCapacidadMaletero(390);
            auto2.setOcupantes(5);
            auto2.setActivo(true);
            auto2.setPropietario(p2Saved);
            vehiculoRepository.save(auto2);

            // --- DATO 3
            PersonaNatural persona3 = new PersonaNatural();
            persona3.setIdentificacion("1310030003");
            persona3.setNombre("Jorge");
            persona3.setApellido("Mendez");
            persona3.setFechaNacimiento(LocalDate.of(1990, 8, 22));
            persona3.setGenero(Genero.MASCULINO);
            persona3.setEmail("jorge.mendez@email.com");
            persona3.setTelefono("0991122334");
            persona3.setDireccion("Calle 13, Manta");
            persona3.setActivo(true);
            Persona p3Saved = personaRepository.save(persona3);

            AutoFamiliar auto3 = new AutoFamiliar();
            auto3.setPlaca("MBA-9090");
            auto3.setMarca("Kia");
            auto3.setModelo("Rio");
            auto3.setColor("Blanco");
            auto3.setAnioFabricacion(2020);
            auto3.setCilindraje(1400);
            auto3.setTipo(TipoAuto.HATCHBACK);
            auto3.setCombustible(TipoCombustible.GASOLINA);
            auto3.setCapacidadMaletero(325);
            auto3.setOcupantes(5);
            auto3.setActivo(true);
            auto3.setPropietario(p3Saved);
            vehiculoRepository.save(auto3);

            // --- DATO 4
            PersonaNatural persona4 = new PersonaNatural();
            persona4.setIdentificacion("1710040004");
            persona4.setNombre("Ana");
            persona4.setApellido("Sarmiento");
            persona4.setFechaNacimiento(LocalDate.of(1985, 12, 10));
            persona4.setGenero(Genero.FEMENINO);
            persona4.setEmail("ana.sarmiento@email.com");
            persona4.setTelefono("0999888777");
            persona4.setDireccion("Cumbayá, Quito");
            persona4.setActivo(true);
            Persona p4Saved = personaRepository.save(persona4);

            AutoFamiliar auto4 = new AutoFamiliar();
            auto4.setPlaca("PCQ-1010");
            auto4.setMarca("BMW");
            auto4.setModelo("Serie 3");
            auto4.setColor("Negro");
            auto4.setAnioFabricacion(2023);
            auto4.setCilindraje(2000);
            auto4.setTipo(TipoAuto.SEDAN);
            auto4.setCombustible(TipoCombustible.HIBRIDO);
            auto4.setCapacidadMaletero(480);
            auto4.setOcupantes(5);
            auto4.setActivo(true);
            auto4.setPropietario(p4Saved);
            vehiculoRepository.save(auto4);

            // --- DATO 5
            PersonaNatural persona5 = new PersonaNatural();
            persona5.setIdentificacion("1750050005");
            persona5.setNombre("Luis");
            persona5.setApellido("Vargas");
            persona5.setFechaNacimiento(LocalDate.of(1992, 6, 5));
            persona5.setGenero(Genero.MASCULINO);
            persona5.setEmail("luis.vargas@email.com");
            persona5.setTelefono("0981231234");
            persona5.setDireccion("El Condado, Quito");
            persona5.setActivo(true);
            Persona p5Saved = personaRepository.save(persona5);

            AutoFamiliar auto5 = new AutoFamiliar();
            auto5.setPlaca("PBA-4321");
            auto5.setMarca("Toyota");
            auto5.setModelo("Fortuner");
            auto5.setColor("Gris");
            auto5.setAnioFabricacion(2022);
            auto5.setCilindraje(2700);
            auto5.setTipo(TipoAuto.SUV);
            auto5.setCombustible(TipoCombustible.DIESEL);
            auto5.setCapacidadMaletero(700);
            auto5.setOcupantes(7);
            auto5.setActivo(true);
            auto5.setPropietario(p5Saved);
            vehiculoRepository.save(auto5);

            // --- DATO 6
            PersonaJuridica empresa = new PersonaJuridica();
            // Datos heredados de Persona
            empresa.setIdentificacion("1790016919001"); // RUC Válido (3er dígito 9)
            empresa.setNombre("Tech Solutions"); // Nombre Comercial
            empresa.setEmail("contacto@techsolutions.com");
            empresa.setTelefono("022555000");
            empresa.setDireccion("Av. Eloy Alfaro y República");
            empresa.setActivo(true);

            // Datos especificos de Persona Jurídica
            empresa.setRazonSocial("Technology Solutions S.A.");
            empresa.setRepresentanteLegal("Roberto Gerente");
            empresa.setFechaConstitucion(LocalDate.of(2015, 1, 20));
            // ¡OJO! Asegúrate que "SERVICIOS" exista en tu Enum ActividadEconomica.
            // Si no, usa COMERCIO u otro que tengas definido.
            empresa.setActividadEconomica(ActividadEconomica.OTRO);

            Persona pEmpresaSaved = personaRepository.save(empresa);

            // Vehículo de la empresa
            AutoFamiliar autoEmpresa = new AutoFamiliar();
            autoEmpresa.setPlaca("PEQ-9999");
            autoEmpresa.setMarca("Ford");
            autoEmpresa.setModelo("Explorer");
            autoEmpresa.setColor("Azul Corporativo");
            autoEmpresa.setAnioFabricacion(2023);
            autoEmpresa.setCilindraje(3500);
            autoEmpresa.setTipo(TipoAuto.SUV);
            autoEmpresa.setCombustible(TipoCombustible.GASOLINA);
            autoEmpresa.setCapacidadMaletero(500);
            autoEmpresa.setOcupantes(7);
            autoEmpresa.setActivo(true);
            autoEmpresa.setPropietario(pEmpresaSaved);

            vehiculoRepository.save(autoEmpresa);

            System.out.println("--- 6 CLIENTES Y VEHICULOS CARGADOS TOTALMENTE ---");
        }
    }
}
