/* eslint-disable @typescript-eslint/no-unsafe-member-access */
/* eslint-disable @typescript-eslint/no-unsafe-call */
/* eslint-disable @typescript-eslint/no-unsafe-assignment */

import {
  Injectable,
  Logger,
  OnModuleDestroy,
  OnModuleInit,
} from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import { NotificationService } from 'src/notifications/notification.service';
import { Connection, Channel, connect, ConsumeMessage } from 'amqplib';
import { NotificationEvent } from './interfaces/notification-event.interface';

@Injectable()
export class RabbitMQService implements OnModuleInit, OnModuleDestroy {
  private readonly logger = new Logger(RabbitMQService.name);
  private connection: Connection;
  private channel: Channel;

  private readonly exchangeName = 'notifications.exchange';
  private readonly queueName = 'notifications.queue';
  private readonly routingKey = 'notifications.routingkey';

  constructor(
    private readonly configService: ConfigService,
    private readonly notificationService: NotificationService,
  ) {}

  async onModuleInit() {
    await this.connect();
    await this.setupQueue();
    await this.consumeMessages();
  }

  async onModuleDestroy() {
    await this.closeConnection();
  }

  private async connect(): Promise<void> {
    try {
      const host = this.configService.get('RABBITMQ_HOST');
      const port = this.configService.get('RABBITMQ_PORT');
      const username = this.configService.get('RABBITMQ_USERNAME');
      const password = this.configService.get('RABBITMQ_PASSWORD');

      const conexion = `amqp://${username}:${password}@${host}:${port}`;

      this.connection = await connect(conexion);
      this.channel = await this.connection.createChannel();
      this.logger.log('Connected to RabbitMQ');
    } catch (error) {
      this.logger.error('Error al conectarse al RabbitMQ', error);
      throw error;
    }
  }

  private async setupQueue(): Promise<void> {
    try {
      //declarar la exhcange
      await this.channel.assertExchange(this.exchangeName, 'topic', {
        durable: true,
      });

      //declarar la cola - queue
      await this.channel.assertQueue(this.queueName, { durable: true });

      //enlaze el queue con el exchange
      await this.channel.bindQueue(
        this.queueName,
        this.exchangeName,
        this.routingKey,
      );

      this.logger.log(`Cola ${this.queueName} configurada correctamente`);
    } catch (error) {
      this.logger.error(
        `Error al configurar la cola: ${this.queueName} `,
        error,
      );
      throw error;
    }
  }

  // En el método consumeMessages, modificar la creación del DTO:
  private async consumeMessages(): Promise<void> {
    try {
      await this.channel.consume(
        this.queueName,
        async (message: ConsumeMessage) => {
          if (message) {
            try {
              const contentString = message.content.toString();
              this.logger.debug(`Mensaje recibido: ${contentString}`);

              // eslint-disable-next-line @typescript-eslint/no-unsafe-argument
              const content = JSON.parse(contentString) as NotificationEvent;

              this.logger.log(
                `Nueva notificación recibida: ${content.action} - ${content.entityType}`,
              );
              this.logger.debug(
                `Timestamp recibido: ${String(content.timestamp)}, tipo: ${typeof content.timestamp}`,
              );

              // Parsear el timestamp correctamente
              let eventTimestamp: Date;
              if (content.timestamp) {
                try {
                  // Intentar parsear como ISO string
                  eventTimestamp = new Date(content.timestamp);

                  // Si es inválido, usar fecha actual
                  if (isNaN(eventTimestamp.getTime())) {
                    this.logger.warn(
                      `Timestamp inválido: ${String(content.timestamp)}, usando fecha actual`,
                    );
                    eventTimestamp = new Date();
                  }
                } catch (error) {
                  this.logger.warn(
                    `Error parseando timestamp: ${error}, usando fecha actual`,
                  );
                  eventTimestamp = new Date();
                }
              } else {
                this.logger.warn(
                  `Timestamp no proporcionado, usando fecha actual`,
                );
                eventTimestamp = new Date();
              }

              // Guardar en base de datos
              await this.notificationService.create({
                eventId: content.id,
                microservice: content.microservice,
                action: content.action,
                entityType: content.entityType,
                entityId: content.entityId,
                message: content.message,
                eventTimestamp: eventTimestamp.toISOString(), // Enviar como ISO string
                data: content.data || {},
                severity: content.severity || 'INFO',
              });

              // Confirmar recepción del mensaje
              this.channel.ack(message);
            } catch (error) {
              this.logger.error('Error procesando mensaje:', error);
              this.logger.error(
                'Contenido del mensaje:',
                message.content.toString(),
              );

              // Rechazar mensaje (no lo reintenta)
              this.channel.nack(message, false, false);
            }
          }
        },
        {
          noAck: false, // Confirmación manual
        },
      );

      this.logger.log(`Consumiendo mensajes de la cola: ${this.queueName}`);
    } catch (error) {
      this.logger.error('Error iniciando consumo de mensajes:', error);
      throw error;
    }
  }

  private async closeConnection(): Promise<void> {
    try {
      if (this.channel) {
        await this.channel.close();
      }

      if (this.connection) {
        await this.connection.close();
      }
      this.logger.log('RabbitMQ connection closed');
    } catch (error) {
      this.logger.error('Error closing RabbitMQ connection', error);
      throw error;
    }
  }
}
//componentes de rabbitmq, y las queue de rabbitmq;
