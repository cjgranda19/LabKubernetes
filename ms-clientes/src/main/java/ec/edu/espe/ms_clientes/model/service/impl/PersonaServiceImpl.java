package ec.edu.espe.ms_clientes.model.service.impl;

import ec.edu.espe.ms_clientes.dto.mapper.PersonaMapper;
import ec.edu.espe.ms_clientes.dto.request.*;
import ec.edu.espe.ms_clientes.dto.response.PersonaResponseDto;
import ec.edu.espe.ms_clientes.dto.response.VehiculoResponseDto;
import ec.edu.espe.ms_clientes.messaging.NotificationProducer;
import ec.edu.espe.ms_clientes.model.*;
import ec.edu.espe.ms_clientes.model.repository.PersonaRepository;
import ec.edu.espe.ms_clientes.model.repository.VehiculoRepository;
import ec.edu.espe.ms_clientes.model.service.PersonaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PersonaServiceImpl implements PersonaService {

    private final PersonaRepository personaRepository;
    private final VehiculoRepository vehiculoRepository;
    private final PersonaMapper personaMapper;
    private final NotificationProducer notificationProducer;

    @Override
    @Transactional
    public PersonaResponseDto createPersonaNatural(PersonaNaturalRequestDto dto) {
        if(personaRepository.existsByIdentificacion(dto.getIdentificacion())){
            throw new RuntimeException("Ya existe una persona con la identificacion " + dto.getIdentificacion());
        }

        PersonaNatural personaNatural = personaMapper.toEntity(dto);
        if(!personaNatural.validarIdentificacion()){
            throw new RuntimeException("Identificacion incorrecta");
        }

        Persona guardado = personaRepository.save(personaNatural);
        
        // Enviar notificación
        notificationProducer.sendPersonaCreated(
            guardado.getId(),
            guardado.getIdentificacion(),
            guardado.getNombre()
        );
        
        return personaMapper.toDto(guardado);
    }

    @Override
    public PersonaResponseDto createPersonaJuridica(PersonaJuridicaRequestDto dto) {
        if(personaRepository.existsByIdentificacion(dto.getIdentificacion())){
            throw new RuntimeException("Ya existe una persona con la identificacion " + dto.getIdentificacion());
        }

        PersonaJuridica personaJuridica = personaMapper.toEntityJuridica(dto);
        if(!personaJuridica.validarIdentificacion()){
            throw new RuntimeException("Identificacion incorrecta");
        }

        Persona guardado = personaRepository.save(personaJuridica);
        
        // Enviar notificación
        notificationProducer.sendPersonaCreated(
            guardado.getId(),
            guardado.getIdentificacion(),
            guardado.getNombre()
        );
        
        return personaMapper.toDto(guardado);
    }

    @Override
    public PersonaResponseDto updatePersonaNatural(UUID id, PersonaRequestDto dto) {
        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));
        
        if(!(persona instanceof PersonaNatural)){
            throw new RuntimeException("La persona no es de tipo natural");
        }
        
        persona.setNombre(dto.getNombre());
        persona.setEmail(dto.getEmail());
        persona.setTelefono(dto.getTelefono());
        persona.setDireccion(dto.getDireccion());
        
        Persona actualizado = personaRepository.save(persona);
        
        // Enviar notificación
        notificationProducer.sendPersonaUpdated(
            actualizado.getId(),
            actualizado.getIdentificacion(),
            actualizado.getNombre()
        );
        
        return personaMapper.toDto(actualizado);
    }

    @Override
    public PersonaResponseDto updatePersonaJuridica(UUID id, PersonaJuridicaRequestDto dto) {
        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));
        
        if(!(persona instanceof PersonaJuridica)){
            throw new RuntimeException("La persona no es de tipo jurídica");
        }
        
        PersonaJuridica personaJuridica = (PersonaJuridica) persona;
        personaJuridica.setNombre(dto.getNombre());
        personaJuridica.setRazonSocial(dto.getRazonSocial());
        personaJuridica.setEmail(dto.getEmail());
        personaJuridica.setTelefono(dto.getTelefono());
        personaJuridica.setDireccion(dto.getDireccion());
        personaJuridica.setRepresentanteLegal(dto.getRepresentanteLegal());
        
        Persona actualizado = personaRepository.save(personaJuridica);
        
        // Enviar notificación
        notificationProducer.sendPersonaUpdated(
            actualizado.getId(),
            actualizado.getIdentificacion(),
            actualizado.getNombre()
        );
        
        return personaMapper.toDto(actualizado);
    }

    @Override
    public void deletePersona(UUID id) {
        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));
        
        // Enviar notificación antes de eliminar
        notificationProducer.sendPersonaDeleted(
            persona.getId(),
            persona.getIdentificacion(),
            persona.getNombre()
        );
        
        personaRepository.deleteById(id);
    }

    @Override
    public List<PersonaResponseDto> findAllPerson() {
        return personaRepository.findAll().stream()
                .map(personaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PersonaResponseDto> findAllPersonByName(String name) {
        // Ahora sí funcionará porque agregamos el método en el Repository
        return personaRepository.findAllByNombreIgnoreCaseContaining(name)
                .stream()
                .map(personaMapper::toDto)
                .collect(Collectors.toList());
    }

    // MÉTODOS REQUERIDOS POR TICKET SERVICE

    @Override
    public PersonaResponseDto findByIdentificacion(String identificacion) {
        Persona persona = personaRepository.findByIdentificacion(identificacion)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con ID: " + identificacion));
        return personaMapper.toDto(persona);
    }

    @Override
    public VehiculoResponseDto findVehiculoByPlaca(String placa) {
        Vehiculo vehiculo = vehiculoRepository.findByPlaca(placa)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con placa: " + placa));
        return mapToVehiculoDto(vehiculo);
    }

    @Override
    public List<VehiculoResponseDto> findVehiculosByPropietario(UUID personaId) {
        return vehiculoRepository.findByPropietarioId(personaId).stream()
                .map(this::mapToVehiculoDto)
                .collect(Collectors.toList());
    }

    private VehiculoResponseDto mapToVehiculoDto(Vehiculo v) {
        return VehiculoResponseDto.builder()
                .id(v.getId())
                .placa(v.getPlaca())
                .marca(v.getMarca())
                .modelo(v.getModelo())
                .color(v.getColor())
                .anioFabricacion(v.getAnioFabricacion())
                .tipoVehiculo(v.getClass().getSimpleName().toUpperCase()) // Retorna "AUTOFAMILIAR", "MOTO", etc.
                .propietarioId(v.getPropietario().getId())
                .propietarioNombre(v.getPropietario().getNombre())
                .activo(v.isActivo())
                .build();
    }

    @Override
    @Transactional
    public VehiculoResponseDto createAutoFamiliar(AutoFamiliarRequestDto dto) {
        // 1. Validar si ya existe la placa
        if (vehiculoRepository.findByPlaca(dto.getPlaca()).isPresent()) {
            throw new RuntimeException("El vehículo con placa " + dto.getPlaca() + " ya existe");
        }

        // 2. Buscar al dueño usando 'idPropietario'
        Persona propietario = personaRepository.findById(dto.getIdPropietario())
                .orElseThrow(() -> new RuntimeException("Propietario no encontrado con ID: " + dto.getIdPropietario()));

        // 3. Crear entidad AutoFamiliar
        AutoFamiliar auto = new AutoFamiliar();

        // Mapeo manual del Padre (Vehiculo)
        auto.setPlaca(dto.getPlaca());
        auto.setMarca(dto.getMarca());
        auto.setModelo(dto.getModelo());
        auto.setColor(dto.getColor());
        auto.setAnioFabricacion(dto.getAnioFabricacion());
        auto.setCilindraje(dto.getCilindraje());
        auto.setActivo(true);
        auto.setPropietario(propietario); // <--- Asignamos la relación

        // Mapeo manual del Hijo (AutoFamiliar)
        // Convertimos String a Enum (Manejo de errores básico)
        try {
            auto.setTipo(TipoAuto.valueOf(dto.getTipo()));
            auto.setCombustible(TipoCombustible.valueOf(dto.getCombustible()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Tipo de auto o combustible inválido. Valores permitidos: SEDAN, SUV... / GASOLINA, DIESEL...");
        }

        auto.setCapacidadMaletero(dto.getCapacidadMaletero());
        auto.setOcupantes(dto.getOcupantes());

        // 4. Guardar y Retornar
        Vehiculo saved = vehiculoRepository.save(auto);
        
        // Enviar notificación
        notificationProducer.sendVehiculoCreated(
            saved.getId(),
            saved.getPlaca(),
            saved.getMarca(),
            saved.getModelo()
        );
        
        return mapToVehiculoDto(saved);
    }

    @Override
    @Transactional
    public VehiculoResponseDto createMoto(MotoRequestDto dto) {
        // 1. Validar placa
        if (vehiculoRepository.findByPlaca(dto.getPlaca()).isPresent()) {
            throw new RuntimeException("La moto con placa " + dto.getPlaca() + " ya existe");
        }

        // 2. Buscar dueño
        Persona propietario = personaRepository.findById(dto.getIdPropietario())
                .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));

        // 3. Crear entidad Moto
        Moto moto = new Moto();

        // Mapeo del Padre
        moto.setPlaca(dto.getPlaca());
        moto.setMarca(dto.getMarca());
        moto.setModelo(dto.getModelo());
        moto.setColor(dto.getColor());
        moto.setAnioFabricacion(dto.getAnioFabricacion());
        moto.setCilindraje(dto.getCilindraje());
        moto.setActivo(true);
        moto.setPropietario(propietario);

        // Mapeo del Hijo
        // Convertimos String a Enum (manejo similar a AutoFamiliar)
        try {
            moto.setTipo(TipoMoto.valueOf(dto.getTipo()));
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new RuntimeException("Tipo de moto inválido. Valores permitidos: SCOOTER, DEPORTIVA, CHOPPER, CRUCERO, NAKED, OTRO");
        }
        moto.setTieneCasco(dto.getTieneCasco());

        // 4. Guardar y Retornar
        Vehiculo saved = vehiculoRepository.save(moto);
        
        // Enviar notificación
        notificationProducer.sendVehiculoCreated(
            saved.getId(),
            saved.getPlaca(),
            saved.getMarca(),
            saved.getModelo()
        );
        
        return mapToVehiculoDto(saved);
    }

    @Override
    public List<VehiculoResponseDto> findAllVehiculos() {
        return vehiculoRepository.findAll().stream()
                .map(this::mapToVehiculoDto)
                .collect(Collectors.toList());
    }
}
