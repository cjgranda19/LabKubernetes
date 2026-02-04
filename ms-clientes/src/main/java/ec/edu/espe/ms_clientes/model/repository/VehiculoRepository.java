package ec.edu.espe.ms_clientes.model.repository;

import ec.edu.espe.ms_clientes.model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, UUID> {
    // Buscar por placa (Ticket Service)
    Optional<Vehiculo> findByPlaca(String placa);

    // Buscar por dueño (Ticket Service)
    List<Vehiculo> findByPropietarioId(UUID idPersona);
}
