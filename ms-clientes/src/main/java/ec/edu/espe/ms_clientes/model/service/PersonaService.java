package ec.edu.espe.ms_clientes.model.service;

import ec.edu.espe.ms_clientes.dto.request.*;
import ec.edu.espe.ms_clientes.dto.response.PersonaResponseDto;
import ec.edu.espe.ms_clientes.dto.response.VehiculoResponseDto;

import java.util.List;
import java.util.UUID;

public interface PersonaService {
    PersonaResponseDto createPersonaNatural(PersonaNaturalRequestDto dto);
    PersonaResponseDto createPersonaJuridica(PersonaJuridicaRequestDto dto);
    //No se devuelve la Persona, solo el Dto, esto protege la entidad y la base de datos

    PersonaResponseDto updatePersonaNatural(UUID id, PersonaRequestDto dto);
    PersonaResponseDto updatePersonaJuridica(UUID id, PersonaJuridicaRequestDto dto);

    void deletePersona(UUID id);

    List<PersonaResponseDto> findAllPerson();

    List<PersonaResponseDto> findAllPersonByName(String name);

    PersonaResponseDto findByIdentificacion(String identificacion);
    VehiculoResponseDto findVehiculoByPlaca(String placa);
    List<VehiculoResponseDto> findVehiculosByPropietario(UUID personaId);

    VehiculoResponseDto createAutoFamiliar(AutoFamiliarRequestDto dto);
    VehiculoResponseDto createMoto(MotoRequestDto dto);
    List<VehiculoResponseDto> findAllVehiculos();

}

//El Service es la capa que contiene la logica de negocio, coordina todo y toma decisiones, se comunica con el repository.
