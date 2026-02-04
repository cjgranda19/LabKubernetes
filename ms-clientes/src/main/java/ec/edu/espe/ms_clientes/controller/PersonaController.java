package ec.edu.espe.ms_clientes.controller;

import ec.edu.espe.ms_clientes.dto.request.PersonaJuridicaRequestDto;
import ec.edu.espe.ms_clientes.dto.request.PersonaNaturalRequestDto;
import ec.edu.espe.ms_clientes.dto.response.PersonaResponseDto;
import ec.edu.espe.ms_clientes.model.Persona;
import ec.edu.espe.ms_clientes.model.service.PersonaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personas")
@RequiredArgsConstructor
public class PersonaController {
    // En SpringBoot el controller define las direcciones WEB donde el API escucha, solo recibe peticiones y las delega al Service

    private final PersonaService personaService;
    @GetMapping
    public ResponseEntity<List<PersonaResponseDto>> findAll() {
        return ResponseEntity.ok(personaService.findAllPerson());
    }

    @GetMapping("/identificacion/{identificacion}")
    public ResponseEntity<PersonaResponseDto> findByIdentificacion(@PathVariable String identificacion) {
        return ResponseEntity.ok(personaService.findByIdentificacion(identificacion));
    }

    @PostMapping("/natural")
    public ResponseEntity<PersonaResponseDto> createPersonaNatural(@RequestBody PersonaNaturalRequestDto personaDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(personaService.createPersonaNatural(personaDto));
    }

    @PostMapping("/juridica")
    public ResponseEntity<PersonaResponseDto> createPersonaJuridica(@RequestBody PersonaJuridicaRequestDto personaDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(personaService.createPersonaJuridica(personaDto));
    }

}
