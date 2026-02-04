import { ApiProperty } from '@nestjs/swagger';

export class NotificationResponseDto {
  @ApiProperty()
  id: string;
  @ApiProperty()
  action: string;
  @ApiProperty()
  microservice: string;
  @ApiProperty()
  entityId: string;
  @ApiProperty()
  entityType: string;
  @ApiProperty()
  createdAt: Date;
  @ApiProperty()
  eventTimeStamp: Date;
  @ApiProperty({ required: false })
  data?: Record<string, any>;
  @ApiProperty()
  severity?: string;
  @ApiProperty()
  read: boolean;
  @ApiProperty()
  processed: boolean;
}
