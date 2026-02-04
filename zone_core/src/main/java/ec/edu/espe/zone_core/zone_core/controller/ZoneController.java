package ec.edu.espe.zone_core.zone_core.controller;

import ec.edu.espe.zone_core.zone_core.dto.ZoneRequestDto;
import ec.edu.espe.zone_core.zone_core.dto.ZoneResponseDto;
import ec.edu.espe.zone_core.zone_core.services.ZoneService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/zones")
public class ZoneController {

    private final ZoneService zoneService;


    public ZoneController(ZoneService zoneService) {
        this.zoneService = zoneService;
    }


    @PostMapping
    public ResponseEntity<ZoneResponseDto> createZone(@RequestBody ZoneRequestDto dto) {
        return ResponseEntity.ok(zoneService.createZone(dto));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ZoneResponseDto> updateZone(
            @PathVariable UUID id,
            @RequestBody ZoneRequestDto dto) {

        return ResponseEntity.ok(zoneService.updateZone(id, dto));
    }


    @GetMapping
    public ResponseEntity<List<ZoneResponseDto>> getAllZones() {
        return ResponseEntity.ok(zoneService.getZones());
    }


    @GetMapping("/{id}")
    public ResponseEntity<ZoneResponseDto> getZoneById(@PathVariable UUID id) {
        return ResponseEntity.ok(zoneService.getZoneId(id));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteZone(@PathVariable UUID id) {
        zoneService.deleteZone(id);
        return ResponseEntity.noContent().build();
    }
}
