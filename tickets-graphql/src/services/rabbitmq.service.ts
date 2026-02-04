import amqp from 'amqplib';

/**
 * Interface para el evento de notificación
 */
export interface NotificationEvent {
  id: string;
  microservice: string;
  action: string;
  entityType: string;
  entityId: string;
  message: string;
  timestamp: Date;
  data: Record<string, any>;
  severity: string;
}

/**
 * Servicio para publicar eventos de notificación a RabbitMQ
 */
export class RabbitMQService {
  private connection: any = null;
  private channel: any = null;
  private readonly exchangeName = 'notifications.exchange';
  private readonly routingKey = 'notifications.routingkey';

  constructor(
    private readonly host: string = process.env.RABBITMQ_HOST || 'localhost',
    private readonly port: number = parseInt(process.env.RABBITMQ_PORT || '5672'),
    private readonly username: string = process.env.RABBITMQ_USERNAME || 'admin',
    private readonly password: string = process.env.RABBITMQ_PASSWORD || 'admin'
  ) {}

  /**
   * Conecta al servidor RabbitMQ y crea el canal
   */
  async connect(): Promise<void> {
    try {
      const connectionUrl = `amqp://${this.username}:${this.password}@${this.host}:${this.port}`;
      this.connection = await amqp.connect(connectionUrl);
      this.channel = await this.connection.createConfirmChannel();
      
      // Declarar el exchange
      await this.channel.assertExchange(this.exchangeName, 'topic', { durable: true });

      console.log('Conectado a RabbitMQ');
    } catch (error) {
      console.error('Error al conectar a RabbitMQ:', error);
      throw error;
    }
  }

  /**
   * Publica un evento de notificación
   */
  async publishNotification(event: NotificationEvent): Promise<void> {
    if (!this.channel) {
      console.warn('Canal de RabbitMQ no disponible. Reconectando...');
      await this.connect();
    }

    try {
      const message = JSON.stringify(event);
      this.channel!.publish(
        this.exchangeName,
        this.routingKey,
        Buffer.from(message),
        { persistent: true, contentType: 'application/json' }
      );
      console.log(`Notificación enviada: ${event.action} - ${event.entityType}`);
    } catch (error) {
      console.error('Error al publicar notificación:', error);
    }
  }

  /**
   * Cierra la conexión con RabbitMQ
   */
  async close(): Promise<void> {
    try {
      if (this.channel) {
        await this.channel.close();
      }
      if (this.connection) {
        await this.connection.close();
      }
      console.log('🔌 Desconectado de RabbitMQ');
    } catch (error) {
      console.error('Error al cerrar conexión RabbitMQ:', error);
    }
  }

  /**
   * Crea y envía una notificación de ticket emitido
   */
  async sendTicketEmitido(ticketId: string, personaNombre: string, vehiculoPlaca: string, zonaNombre: string): Promise<void> {
    const event: NotificationEvent = {
      id: this.generateId(),
      microservice: 'microservice - tickets',
      action: 'CREATE',
      entityType: 'TICKET',
      entityId: ticketId,
      message: `Ticket emitido para ${personaNombre} - Vehículo ${vehiculoPlaca} en zona ${zonaNombre}`,
      timestamp: new Date(),
      data: {
        ticketId,
        personaNombre,
        vehiculoPlaca,
        zonaNombre
      },
      severity: 'INFO'
    };

    await this.publishNotification(event);
  }

  /**
   * Crea y envía una notificación de ticket cerrado
   */
  async sendTicketCerrado(ticketId: string, personaNombre: string, vehiculoPlaca: string): Promise<void> {
    const event: NotificationEvent = {
      id: this.generateId(),
      microservice: 'microservice - tickets',
      action: 'UPDATE',
      entityType: 'TICKET',
      entityId: ticketId,
      message: `Ticket cerrado para ${personaNombre} - Vehículo ${vehiculoPlaca}`,
      timestamp: new Date(),
      data: {
        ticketId,
        personaNombre,
        vehiculoPlaca,
        estado: 'CERRADO'
      },
      severity: 'INFO'
    };

    await this.publishNotification(event);
  }

  /**
   * Crea y envía una notificación de ticket anulado
   */
  async sendTicketAnulado(ticketId: string, personaNombre: string, vehiculoPlaca: string): Promise<void> {
    const event: NotificationEvent = {
      id: this.generateId(),
      microservice: 'microservice - tickets',
      action: 'DELETE',
      entityType: 'TICKET',
      entityId: ticketId,
      message: `Ticket anulado para ${personaNombre} - Vehículo ${vehiculoPlaca}`,
      timestamp: new Date(),
      data: {
        ticketId,
        personaNombre,
        vehiculoPlaca,
        estado: 'ANULADO'
      },
      severity: 'WARN'
    };

    await this.publishNotification(event);
  }

  /**
   * Genera un ID único para el evento
   */
  private generateId(): string {
    return `${Date.now()}-${Math.random().toString(36).substr(2, 9)}`;
  }
}
