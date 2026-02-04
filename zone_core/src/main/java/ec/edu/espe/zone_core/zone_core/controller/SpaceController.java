package ec.edu.espe.zone_core.zone_core.controller;

import ec.edu.espe.zone_core.zone_core.dto.SpacesRequestDto;
import ec.edu.espe.zone_core.zone_core.dto.SpacesResponseDto;
import ec.edu.espe.zone_core.zone_core.services.SpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/spaces")
public class SpaceController {

    @Autowired
    private SpaceService spaceService;

    @GetMapping
    public ResponseEntity<List<SpacesResponseDto>> findAllSpaces() {
        return ResponseEntity.ok(spaceService.getSpaces());
    }

    @PostMapping
    public ResponseEntity<SpacesResponseDto> createSpace(@RequestBody SpacesRequestDto spaceRequestDto) {
        return ResponseEntity.ok(spaceService.createSpace(spaceRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpacesResponseDto> updateSpace(@PathVariable UUID id, @RequestBody SpacesRequestDto spaceRequestDto) {
        return ResponseEntity.ok(spaceService.updateSpace(id, spaceRequestDto));
    }


    @GetMapping("/{id}/zone/{zoneId}")
    public ResponseEntity<SpacesResponseDto> getSpaceByIdAndZone(
            @PathVariable UUID id,
            @PathVariable UUID zoneId
    ) {
        return ResponseEntity.ok(spaceService.getSpace(id, zoneId));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpace(@PathVariable UUID id) {
        spaceService.deleteSpace(id);
        return ResponseEntity.noContent().build();
    }

    // 1. Endpoint para disponibles (Ticket Service lo busca)
    @GetMapping("/available")
    public ResponseEntity<List<SpacesResponseDto>> getAvailableSpaces() {
        return ResponseEntity.ok(spaceService.getAvailableSpaces());
    }

    // 2. Endpoint para espacios de una zona (Ticket Service lo busca)
    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<List<SpacesResponseDto>> getSpacesByZone(@PathVariable UUID zoneId) {
        return ResponseEntity.ok(spaceService.getSpacesByZone(zoneId));
    }

    // 3. Endpoint PATCH para cambiar estado (Ticket Service lo usa al emitir ticket)
    // URL: /api/spaces/{id}/status?status=OCCUPIED
    @PatchMapping("/{id}/status")
    public ResponseEntity<SpacesResponseDto> updateStatus(
            @PathVariable UUID id,
            @RequestParam String status) {
        return ResponseEntity.ok(spaceService.updateSpaceStatus(id, status));
    }


}
