package ec.edu.espe.ms_clientes.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent {
    private UUID id;
    private String microservice;
    private String action;
    private String entityType;
    private String entityId;
    private String message;
    private LocalDateTime timestamp;
    private Map<String, Object> data;
    private String severity;
}
