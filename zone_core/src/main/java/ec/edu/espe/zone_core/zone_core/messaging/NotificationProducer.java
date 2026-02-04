package ec.edu.espe.zone_core.zone_core.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationProducer {
    @Autowired
    private final RabbitTemplate rabbitTemplate;

    private static final String ACTION_CREATE = "CREATE";
    private static final String ACTION_DELETE = "DELETE";
    private static final String ACTION_UPDATE = "UPDATE";
    private static final String ENTITY_ZONE = "ENTIDAD ZONA";
    private static final String ENTITY_SPACE = "ENTIDAD ESPACIO";
    private static final String SEVERITY_INFO = "INFO";
    private static final String SEVERITY_WARN = "WARN";
    private static final String SEVERITY_ERROR = "ERROR";

    /**
     * Obtiene la dirección IP del host
     */
    private String getHostIP() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("No se pudo obtener la IP del host", e);
            return "unknown";
        }
    }

    /**
     * Obtiene el nombre del host
     */
    private String getHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            log.warn("No se pudo obtener el hostname", e);
            return "unknown";
        }
    }

    public void sendNotification(String action, String entityType, UUID entityId, String message, Map<String, Object> data) {
        NotificationEvent event = NotificationEvent.builder()
                .id(UUID.randomUUID())
                .microservice("microservice - zones")
                .action(action)
                .entityType(entityType)
                .entityId(entityId.toString())
                .message(message)
                .timestamp(LocalDateTime.now())
                .data(data != null ? data : new HashMap<>())
                .severity(SEVERITY_INFO)
                .build();
        try{
            log.debug("Sending notification event: {}", event);
            rabbitTemplate.convertAndSend("notifications.exchange", "notifications.routingkey", event);
            log.info("Notificacion enviada: {} - {}", action, event);
        } catch (Exception e){
            log.error("Error al enviar notificacion: {} - {}", action, event, e);
        }
    }

    public void sendZoneCreate(UUID idZone, String zoneName){
        Map<String, Object> data = new HashMap<>();
        data.put("idZone", idZone);
        data.put("zoneName", zoneName);
        data.put("IP", getHostIP());
        data.put("HOSTNAME", getHostname());

        String message = "Nueva zona creada: " + zoneName;
        sendNotification(ACTION_CREATE, ENTITY_ZONE, idZone, message, data);
    }

    public void sendZoneDeleted(UUID idZone, String zoneName){
        Map<String, Object> data = new HashMap<>();
        data.put("idZone", idZone);
        data.put("zoneName", zoneName);

        String message = "Zona eliminada: " + zoneName;
        sendNotification(ACTION_DELETE, ENTITY_ZONE, idZone, message, data);
    }

    public void sendZoneUpdated(UUID idZone, String zoneName){
        Map<String, Object> data = new HashMap<>();
        data.put("idZone", idZone);
        data.put("zoneName", zoneName);

        String message = "Zona actualizada: " + zoneName;
        sendNotification(ACTION_UPDATE, ENTITY_ZONE, idZone, message, data);
    }

    public void sendSpaceCreated(UUID spaceId, String spaceName, String zoneName){
        Map<String, Object> data = new HashMap<>();
        data.put("idSpace", spaceId);
        data.put("spaceName", spaceName);
        data.put("zoneName", zoneName);
        data.put("IP", getHostIP());
        data.put("HOSTNAME", getHostname());

        String message = "Nuevo espacio '" + spaceName + "' creado en zona " + zoneName;
        sendNotification(ACTION_CREATE, ENTITY_SPACE, spaceId, message, data);

        log.info("Space Created: {} - {}", spaceId);
    }

    public void sendSpaceDeleted(UUID spaceId, String spaceName, String zoneName){
        Map<String, Object> data = new HashMap<>();
        data.put("idSpace", spaceId);
        data.put("spaceName", spaceName);
        data.put("zoneName", zoneName);

        String message = "Espacio eliminado: " + spaceName + " de la zona " + zoneName;
        sendNotification(ACTION_DELETE, ENTITY_SPACE, spaceId, message, data);
    }

    public  void sendSpaceUpdated(UUID spaceId, String spaceName, String zoneName){
        Map<String, Object> data = new HashMap<>();
        data.put("idSpace", spaceId);
        data.put("spaceName", spaceName);
        data.put("zoneName", zoneName);

        String message = "Espacio actualizado: " + spaceName + " en zona " + zoneName;
        sendNotification(ACTION_UPDATE, ENTITY_SPACE, spaceId, message, data);
    }
}
