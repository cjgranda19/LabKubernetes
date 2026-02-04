package ec.edu.espe.zone_core.zone_core.services.impl;

import ec.edu.espe.zone_core.zone_core.dto.ZoneRequestDto;
import ec.edu.espe.zone_core.zone_core.dto.ZoneResponseDto;
import ec.edu.espe.zone_core.zone_core.messaging.NotificationProducer;
import ec.edu.espe.zone_core.zone_core.model.Zone;
import ec.edu.espe.zone_core.zone_core.repositories.ZoneRepository;
import ec.edu.espe.zone_core.zone_core.services.ZoneService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ZoneServicesImpl implements ZoneService {

    private final ZoneRepository zoneRepository;
    private final NotificationProducer  notificationProducer;

    public ZoneServicesImpl(ZoneRepository zoneRepository, NotificationProducer notificationProducer) {
        this.zoneRepository = zoneRepository;
        this.notificationProducer = notificationProducer;
    }


    @Override
    public ZoneResponseDto createZone(ZoneRequestDto dto) {

        Zone zone = Zone.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .capacity(dto.getCapacity())
                .type(dto.getType())
                .isActive(dto.getIsActive())
                .build();

        Zone saved = zoneRepository.save(zone);
        notificationProducer.sendZoneCreate(saved.getId(), saved.getName());
        return convertToResponseDto(saved);
    }

    @Override
    public ZoneResponseDto updateZone(UUID id, ZoneRequestDto dto) {
        Zone existingZone = zoneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zone not found"));

        existingZone.setName(dto.getName());
        existingZone.setDescription(dto.getDescription());
        existingZone.setCapacity(dto.getCapacity());
        existingZone.setType(dto.getType());
        existingZone.setIsActive(dto.getIsActive());

        Zone updated = zoneRepository.save(existingZone);
        notificationProducer.sendZoneUpdated(updated.getId(), updated.getName());
        return convertToResponseDto(updated);
    }

    @Override
    public List<ZoneResponseDto> getZones() {
        return zoneRepository.findAll().stream()
                .map(this::convertToResponseDto)
                .toList();
    }

    @Override
    public void deleteZone(UUID id) {
        Zone existingZone = zoneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zone not found"));

        String zoneName = existingZone.getName();
        zoneRepository.delete(existingZone);
        notificationProducer.sendZoneDeleted(id, zoneName);
    }

    @Override
    public ZoneResponseDto getZoneId(UUID id) {
        Zone zone = zoneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zone not found"));

        return convertToResponseDto(zone);
    }



    private ZoneResponseDto convertToResponseDto(Zone zone) {
        return ZoneResponseDto.builder()
                .id(zone.getId())
                .name(zone.getName())
                .description(zone.getDescription())
                .capacity(zone.getCapacity())
                .type(zone.getType())
                .isActive(zone.getIsActive())
                .build();
    }
}
