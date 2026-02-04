package ec.edu.espe.ms_clientes.model.repository;

import ec.edu.espe.ms_clientes.model.Persona;
import ec.edu.espe.ms_clientes.model.PersonaJuridica;
import ec.edu.espe.ms_clientes.model.PersonaNatural;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, UUID> {
    boolean existsByIdentificacion(String identificacion);

    Optional<Persona> findByIdentificacion(String identificacion);

    // Para búsqueda por nombre
    List<Persona> findAllByNombreIgnoreCaseContaining(String nombre);
}