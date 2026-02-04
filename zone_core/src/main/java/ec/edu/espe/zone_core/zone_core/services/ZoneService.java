package ec.edu.espe.zone_core.zone_core.services;

import ec.edu.espe.zone_core.zone_core.dto.ZoneRequestDto;
import ec.edu.espe.zone_core.zone_core.dto.ZoneResponseDto;
import ec.edu.espe.zone_core.zone_core.model.Zone;

import java.util.List;
import java.util.UUID;

public interface ZoneService {

    ZoneResponseDto createZone(ZoneRequestDto dto);
    ZoneResponseDto updateZone(UUID id, ZoneRequestDto dto);
    List<ZoneResponseDto> getZones();
    void deleteZone(UUID id);
    ZoneResponseDto getZoneId(UUID id);

}
