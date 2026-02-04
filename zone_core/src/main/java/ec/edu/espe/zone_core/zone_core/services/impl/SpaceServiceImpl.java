package ec.edu.espe.zone_core.zone_core.services.impl;

import ec.edu.espe.zone_core.zone_core.dto.SpacesRequestDto;
import ec.edu.espe.zone_core.zone_core.dto.SpacesResponseDto;
import ec.edu.espe.zone_core.zone_core.messaging.NotificationProducer;
import ec.edu.espe.zone_core.zone_core.model.SapaceStatus;
import ec.edu.espe.zone_core.zone_core.model.Spaces;
import ec.edu.espe.zone_core.zone_core.model.Zone;
import ec.edu.espe.zone_core.zone_core.repositories.SpaceRepository;
import ec.edu.espe.zone_core.zone_core.repositories.ZoneRepository;
import ec.edu.espe.zone_core.zone_core.services.SpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SpaceServiceImpl implements SpaceService {

    @Autowired
    private SpaceRepository spaceRepository;
    @Autowired
    private NotificationProducer  notificationProducer;

    @Autowired
    private ZoneRepository zoneRepository;

    @Override
    public SpacesResponseDto createSpace(SpacesRequestDto dto) {

        Zone zone = zoneRepository.findById(dto.getIdZone())
                .orElseThrow(() -> new RuntimeException("Zona no encontrada"));


        Spaces space = Spaces.builder()
                .name(dto.getName())
                .code(dto.getCode())
                .status(dto.getStatus())
                .description(dto.getDescription())
                .priority(dto.getPriority())
                .zone(zone)
                .build();

        Spaces saved = spaceRepository.save(space);

        notificationProducer.sendSpaceCreated(saved.getId(),
                saved.getCode(),
                saved.getZone().getName());

        return convertToResponseDto(saved);
    }

    @Override
    public SpacesResponseDto updateSpace(UUID id, SpacesRequestDto dto) {
        Spaces existing = spaceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Space not found"));

        // actualizar campos
        existing.setName(dto.getName());
        existing.setCode(dto.getCode());
        existing.setDescription(dto.getDescription());
        existing.setPriority(dto.getPriority());
        existing.setStatus(dto.getStatus());
        Zone zone = zoneRepository.findById(dto.getIdZone())
                .orElseThrow(() -> new RuntimeException("Zona no encontrada"));
        existing.setZone(zone);


        Spaces saved = spaceRepository.save(existing);

        notificationProducer.sendSpaceUpdated(
                saved.getId(),
                saved.getName(),
                saved.getZone().getName()
        );

        return convertToResponseDto(saved);
    }

    @Override
    public SpacesResponseDto getSpace(UUID id, UUID zoneId) {

        Spaces space = spaceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Space not found"));

        if (!space.getZone().getId().equals(zoneId)) {
            throw new RuntimeException("Space does not belong to the given zone");
        }

        return convertToResponseDto(space);
    }

    @Override
    public void deleteSpace(UUID spaceId) {
        Spaces space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new RuntimeException("Space not found"));

        String spaceName = space.getName();
        String zoneName = space.getZone().getName();
        spaceRepository.delete(space);
        notificationProducer.sendSpaceDeleted(spaceId, spaceName, zoneName);
    }

    @Override
    public List<SpacesResponseDto> getSpaces() {

        return spaceRepository.findAll()
                .stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }


    private SpacesResponseDto convertToResponseDto(Spaces space) {
        return SpacesResponseDto.builder()
                .id(space.getId())
                .name(space.getName())
                .code(space.getCode())
                .status(space.getStatus())
                .description(space.getDescription())
                .priority(space.getPriority())
                .idZone(space.getZone().getId())

                .build();
    }

    @Override
    public List<SpacesResponseDto> getSpacesByZone(UUID zoneId) {
        return spaceRepository.findByZoneId(zoneId).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SpacesResponseDto> getAvailableSpaces() {
        // Asumo que tu Enum se llama SapaceStatus y tiene el valor AVAILABLE
        return spaceRepository.findByStatus(SapaceStatus.AVAILABLE).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public SpacesResponseDto updateSpaceStatus(UUID id, String newStatus) {
        Spaces space = spaceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Espacio no encontrado"));

        try {
            // Convertimos el String (OCCUPIED/AVAILABLE) al Enum
            space.setStatus(SapaceStatus.valueOf(newStatus));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Estado inválido: " + newStatus);
        }

        Spaces saved = spaceRepository.save(space);

        // Notificamos el cambio de estado
        notificationProducer.sendSpaceUpdated(saved.getId(), saved.getName(), saved.getZone().getName());

        return convertToResponseDto(saved);
    }
}
