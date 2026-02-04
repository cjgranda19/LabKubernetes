package ec.edu.espe.ms_clientes.dto.mapper;

import ec.edu.espe.ms_clientes.dto.request.AutoFamiliarRequestDto;
import ec.edu.espe.ms_clientes.dto.request.MotoRequestDto;
import ec.edu.espe.ms_clientes.model.*;

public class VehiculoMapper {
    public Moto toEntityMoto(MotoRequestDto dto) {
        if (dto == null) {
            return null;
        }

        return Moto.builder()
                .placa(dto.getPlaca())
                .marca(dto.getMarca())
                .modelo(dto.getModelo())
                .color(dto.getColor())
                .cilindraje(dto.getCilindraje())
                .anioFabricacion(dto.getAnioFabricacion())
                .tieneCasco(dto.getTieneCasco())
                .tipo(TipoMoto.valueOf(dto.getTipo()))
                // Para el propietario se debe hacer el Servicio haciendo uso del id
                .build();
    }

    public AutoFamiliar toEntityAuto(AutoFamiliarRequestDto dto) {
        if (dto == null) {
            return null;
        }

        return AutoFamiliar.builder()
                .placa(dto.getPlaca())
                .marca(dto.getMarca())
                .modelo(dto.getModelo())
                .color(dto.getColor())
                .cilindraje(dto.getCilindraje())
                .anioFabricacion(dto.getAnioFabricacion())
                .capacidadMaletero(dto.getCapacidadMaletero())
                .ocupantes(dto.getOcupantes())
                .tipo(TipoAuto.valueOf(dto.getTipo()))
                .combustible(TipoCombustible.valueOf(dto.getCombustible()))
                .build();
    }
}
