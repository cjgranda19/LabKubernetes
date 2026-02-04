package ec.edu.espe.zone_core.zone_core.config;

import ec.edu.espe.zone_core.zone_core.model.SapaceStatus;
import ec.edu.espe.zone_core.zone_core.model.Spaces;
import ec.edu.espe.zone_core.zone_core.model.TypeZone;
import ec.edu.espe.zone_core.zone_core.model.Zone;
import ec.edu.espe.zone_core.zone_core.repositories.SpaceRepository;
import ec.edu.espe.zone_core.zone_core.repositories.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ZoneRepository zoneRepository;
    private final SpaceRepository spaceRepository;

    @Override
    public void run(String... args) throws Exception {
        if (zoneRepository.count() == 0) {
            System.out.println("--- CREANDO ZONAS Y ESPACIOS ---");

            // 1. Zona General
            Zone general = Zone.builder()
                    .name("Zona General")
                    .description("Parqueo principal")
                    .capacity(50)
                    .type(TypeZone.EXTERNAL)
                    .isActive(true)
                    .build();
            Zone savedGeneral = zoneRepository.save(general);

            // 2. Zona VIP
            Zone vip = Zone.builder()
                    .name("Zona VIP")
                    .description("Cerca de la entrada")
                    .capacity(10)
                    .type(TypeZone.INTERNAL)
                    .isActive(true)
                    .build();
            Zone savedVip = zoneRepository.save(vip);

            // 3. Crear Espacios en General (A01 - A05)
            for (int i = 1; i <= 5; i++) {
                Spaces space = Spaces.builder()
                        .name("Espacio A-0" + i)
                        .code("A-0" + i)
                        .status(SapaceStatus.AVAILABLE)
                        .description("Espacio estándar")
                        .priority(1)
                        .zone(savedGeneral)
                        .build();
                spaceRepository.save(space);
            }

            // 4. Crear Espacios en VIP (V01 - V02)
            for (int i = 1; i <= 2; i++) {
                Spaces space = Spaces.builder()
                        .name("Espacio VIP-0" + i)
                        .code("V-0" + i)
                        .status(SapaceStatus.AVAILABLE)
                        .description("Espacio techado")
                        .priority(0)
                        .zone(savedVip)
                        .build();
                spaceRepository.save(space);
            }

            System.out.println("--- ZONAS Y ESPACIOS LISTOS ---");
        }
    }
}
