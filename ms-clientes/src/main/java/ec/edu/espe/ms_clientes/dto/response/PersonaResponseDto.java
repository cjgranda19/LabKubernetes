package ec.edu.espe.ms_clientes.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class PersonaResponseDto {
    private UUID id;
    private String nombre; //nombre completo pn - razon social pj
    private String identificacion; //cedula pn - ruc pj
    private String tipoPersona; //PN - PJ, traer refactorizado en booleano
    private String email;
    private String direccion;
    private String telefono;
    private Boolean activo;
    private LocalDateTime fechaCreacion;


}
