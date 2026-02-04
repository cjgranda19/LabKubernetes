package ec.edu.espe.ms_clientes.controller;

import ec.edu.espe.ms_clientes.dto.request.AutoFamiliarRequestDto;
import ec.edu.espe.ms_clientes.dto.request.MotoRequestDto;
import ec.edu.espe.ms_clientes.dto.response.VehiculoResponseDto;
import ec.edu.espe.ms_clientes.model.service.PersonaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vehiculos") // <--- Coincide con Ticket Service
@RequiredArgsConstructor
public class VehiculoController {
    private final PersonaService personaService;

    @GetMapping
    public ResponseEntity<List<VehiculoResponseDto>> findAll() {
        return ResponseEntity.ok(personaService.findAllVehiculos());
    }

    @GetMapping("/placa/{placa}")
    public ResponseEntity<VehiculoResponseDto> findByPlaca(@PathVariable String placa) {
        return ResponseEntity.ok(personaService.findVehiculoByPlaca(placa));
    }

    @GetMapping("/propietario/{personaId}")
    public ResponseEntity<List<VehiculoResponseDto>> findByPropietario(@PathVariable UUID personaId) {
        return ResponseEntity.ok(personaService.findVehiculosByPropietario(personaId));
    }

    @PostMapping("/auto-familiar")
    public ResponseEntity<VehiculoResponseDto> createAutoFamiliar(@Valid @RequestBody AutoFamiliarRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(personaService.createAutoFamiliar(dto));
    }

    @PostMapping("/moto")
    public ResponseEntity<VehiculoResponseDto> createMoto(@Valid @RequestBody MotoRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(personaService.createMoto(dto));
    }

}
