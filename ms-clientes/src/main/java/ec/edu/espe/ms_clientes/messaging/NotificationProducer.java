package ec.edu.espe.ms_clientes.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationProducer {
    private final RabbitTemplate rabbitTemplate;

    private static final String ACTION_CREATE = "CREATE";
    private static final String ACTION_DELETE = "DELETE";
    private static final String ACTION_UPDATE = "UPDATE";
    private static final String ENTITY_PERSONA = "PERSONA";
    private static final String ENTITY_VEHICULO = "VEHICULO";
    private static final String SEVERITY_INFO = "INFO";

    public void sendNotification(String action, String entityType, UUID entityId, String message, Map<String, Object> data) {
        NotificationEvent event = NotificationEvent.builder()
                .id(UUID.randomUUID())
                .microservice("microservice - clientes")
                .action(action)
                .entityType(entityType)
                .entityId(entityId.toString())
                .message(message)
                .timestamp(LocalDateTime.now())
                .data(data != null ? data : new HashMap<>())
                .severity(SEVERITY_INFO)
                .build();
        try {
            log.debug("Sending notification event: {}", event);
            rabbitTemplate.convertAndSend("notifications.exchange", "notifications.routingkey", event);
            log.info("Notificacion enviada: {} - {}", action, event);
        } catch (Exception e) {
            log.error("Error al enviar notificacion: {} - {}", action, event, e);
        }
    }

    // Notificaciones para Personas
    public void sendPersonaCreated(UUID personaId, String identificacion, String nombre) {
        Map<String, Object> data = new HashMap<>();
        data.put("personaId", personaId);
        data.put("identificacion", identificacion);
        data.put("nombre", nombre);

        String message = "Nueva persona creada: " + nombre + " (ID: " + identificacion + ")";
        sendNotification(ACTION_CREATE, ENTITY_PERSONA, personaId, message, data);
    }

    public void sendPersonaUpdated(UUID personaId, String identificacion, String nombre) {
        Map<String, Object> data = new HashMap<>();
        data.put("personaId", personaId);
        data.put("identificacion", identificacion);
        data.put("nombre", nombre);

        String message = "Persona actualizada: " + nombre + " (ID: " + identificacion + ")";
        sendNotification(ACTION_UPDATE, ENTITY_PERSONA, personaId, message, data);
    }

    public void sendPersonaDeleted(UUID personaId, String identificacion, String nombre) {
        Map<String, Object> data = new HashMap<>();
        data.put("personaId", personaId);
        data.put("identificacion", identificacion);
        data.put("nombre", nombre);

        String message = "Persona eliminada: " + nombre + " (ID: " + identificacion + ")";
        sendNotification(ACTION_DELETE, ENTITY_PERSONA, personaId, message, data);
    }

    // Notificaciones para Vehículos
    public void sendVehiculoCreated(UUID vehiculoId, String placa, String marca, String modelo) {
        Map<String, Object> data = new HashMap<>();
        data.put("vehiculoId", vehiculoId);
        data.put("placa", placa);
        data.put("marca", marca);
        data.put("modelo", modelo);

        String message = "Nuevo vehículo registrado: " + marca + " " + modelo + " (Placa: " + placa + ")";
        sendNotification(ACTION_CREATE, ENTITY_VEHICULO, vehiculoId, message, data);
    }

    public void sendVehiculoUpdated(UUID vehiculoId, String placa, String marca, String modelo) {
        Map<String, Object> data = new HashMap<>();
        data.put("vehiculoId", vehiculoId);
        data.put("placa", placa);
        data.put("marca", marca);
        data.put("modelo", modelo);

        String message = "Vehículo actualizado: " + marca + " " + modelo + " (Placa: " + placa + ")";
        sendNotification(ACTION_UPDATE, ENTITY_VEHICULO, vehiculoId, message, data);
    }

    public void sendVehiculoDeleted(UUID vehiculoId, String placa, String marca, String modelo) {
        Map<String, Object> data = new HashMap<>();
        data.put("vehiculoId", vehiculoId);
        data.put("placa", placa);
        data.put("marca", marca);
        data.put("modelo", modelo);

        String message = "Vehículo eliminado: " + marca + " " + modelo + " (Placa: " + placa + ")";
        sendNotification(ACTION_DELETE, ENTITY_VEHICULO, vehiculoId, message, data);
    }
}
