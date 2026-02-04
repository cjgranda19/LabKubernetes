package ec.edu.espe.ms_clientes.dto.mapper;

import ec.edu.espe.ms_clientes.dto.request.PersonaJuridicaRequestDto;
import ec.edu.espe.ms_clientes.dto.request.PersonaNaturalRequestDto;
import ec.edu.espe.ms_clientes.dto.response.PersonaResponseDto;
import ec.edu.espe.ms_clientes.model.*;
import org.springframework.stereotype.Component;

@Component
public class PersonaMapper {

    public PersonaNatural toEntity(PersonaNaturalRequestDto dto){
        if(dto == null){
            return null;
        }
        return PersonaNatural.builder()
                .identificacion(dto.getIdentificacion())
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .email(dto.getEmail())
                .telefono(dto.getTelefono())
                .direccion(dto.getDireccion())
                .fechaNacimiento(dto.getFechaNacimiento())
                .genero(Genero.valueOf(dto.getGenero()))
                .build();
    }


    public PersonaJuridica toEntityJuridica(PersonaJuridicaRequestDto persona){
        if(persona == null){
            return null;
        }
        return PersonaJuridica.builder()
                .identificacion(persona.getIdentificacion())
                .nombre(persona.getNombre())
                .razonSocial(persona.getRazonSocial())
                .email(persona.getEmail())
                .telefono(persona.getTelefono())
                .direccion(persona.getDireccion())
                .representanteLegal(persona.getRepresentanteLegal())
                .fechaConstitucion(persona.getFechaConstitucion())
                .actividadEconomica(ActividadEconomica.valueOf(persona.getActividadEconomica())) // Convertir String a Enum, valueOf busca el enum por nombre
                .build();

    }

    public PersonaResponseDto toDto(Persona persona){
        if(persona == null){
            return null;
        }

        return PersonaResponseDto.builder()
                .id(persona.getId())
                .identificacion(persona.getIdentificacion())
                .nombre(persona.getNombre())
                .email(persona.getEmail())
                .telefono(persona.getTelefono())
                .direccion(persona.getDireccion())
                .activo(persona.isActivo())
                .fechaCreacion(persona.getFechaCreacion())
                .tipoPersona(determinarTipo(persona))
                .build();
    }

    private String determinarTipo(Persona p) {
        if (p instanceof PersonaNatural) {
            return "Natural";
        } else if (p instanceof PersonaJuridica) {
            return "Juridica";
        } else {
            return null;
        }
    }
}
