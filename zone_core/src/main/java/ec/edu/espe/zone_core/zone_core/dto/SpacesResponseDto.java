package ec.edu.espe.zone_core.zone_core.dto;

import ec.edu.espe.zone_core.zone_core.model.SapaceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpacesResponseDto {

    private UUID id;
    private String name;
    private String code;
    private SapaceStatus status;
    private String description;
    private Integer priority;
    private UUID idZone;

}
