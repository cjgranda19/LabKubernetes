import { Column, Entity, PrimaryGeneratedColumn, CreateDateColumn, UpdateDateColumn } from 'typeorm';

@Entity('tickets')
export class Ticket {
    @PrimaryGeneratedColumn('uuid')
    id!: string;

    @Column({ name: 'codigo_ticket', unique: true })
    codigoTicket!: string;

    @Column({ name: 'persona_identificacion', nullable: false })
    personaIdentificacion!: string;

    @Column({ name: 'persona_nombre', nullable: false })
    personaNombre!: string;
    
    @Column({ name: 'vehiculo_placa', nullable: false })
    vehiculoPlaca!: string;

    @Column({ name: 'vehiculo_marca', nullable: false })
    vehiculoMarca!: string;

    @Column({ name: 'vehiculo_modelo', nullable: false })
    vehiculoModelo!: string;
    
    @Column({ name: 'zona_nombre', nullable: false })
    zonaNombre!: string;

    @Column({ name: 'espacio_codigo', nullable: false })
    espacioCodigo!: string;

    @Column({ name: 'fecha_ingreso', type: 'timestamp', nullable: false })
    fechaHoraIngreso!: Date;
    
    @Column({ name: 'fecha_salida', type: 'timestamp', nullable: true }) // permite nulo porque cuando el cliente sale es que se llena
    fechaHoraSalida!: Date;

    @Column( { name: 'estado', default: 'ACTIVO' }) // incluir un ENUM
    estado!: string;

    @Column({ name: 'tiempo_estacionado', nullable: true })
    tiempoEstacionado!: number;

    @CreateDateColumn({ name: 'created_at', type: 'timestamp' })
    createdAt!: Date;

    @UpdateDateColumn({ name: 'updated_at', type: 'timestamp' })
    updatedAt!: Date;

    
}