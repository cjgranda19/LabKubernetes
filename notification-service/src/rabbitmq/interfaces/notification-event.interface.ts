export interface NotificationEvent {
  id: string;
  microservice: string;
  action: string;
  entityType: string;
  entityId: string;
  message: string;
  timestamp: Date;
  data?: Record<string, any>;
  severity: string;
}
