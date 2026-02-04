"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.Ticket = void 0;
const typeorm_1 = require("typeorm");
let Ticket = class Ticket {
};
exports.Ticket = Ticket;
__decorate([
    (0, typeorm_1.PrimaryGeneratedColumn)('uuid'),
    __metadata("design:type", String)
], Ticket.prototype, "id", void 0);
__decorate([
    (0, typeorm_1.Column)({ name: 'codigo_ticket', unique: true }),
    __metadata("design:type", String)
], Ticket.prototype, "codigoTicket", void 0);
__decorate([
    (0, typeorm_1.Column)({ name: 'persona_identificacion', nullable: false }),
    __metadata("design:type", String)
], Ticket.prototype, "personaIdentificacion", void 0);
__decorate([
    (0, typeorm_1.Column)({ name: 'persona_nombre', nullable: false }),
    __metadata("design:type", String)
], Ticket.prototype, "personaNombre", void 0);
__decorate([
    (0, typeorm_1.Column)({ name: 'vehiculo_placa', nullable: false }),
    __metadata("design:type", String)
], Ticket.prototype, "vehiculoPlaca", void 0);
__decorate([
    (0, typeorm_1.Column)({ name: 'vehiculo_marca', nullable: false }),
    __metadata("design:type", String)
], Ticket.prototype, "vehiculoMarca", void 0);
__decorate([
    (0, typeorm_1.Column)({ name: 'vehiculo_modelo', nullable: false }),
    __metadata("design:type", String)
], Ticket.prototype, "vehiculoModelo", void 0);
__decorate([
    (0, typeorm_1.Column)({ name: 'zona_nombre', nullable: false }),
    __metadata("design:type", String)
], Ticket.prototype, "zonaNombre", void 0);
__decorate([
    (0, typeorm_1.Column)({ name: 'espacio_codigo', nullable: false }),
    __metadata("design:type", String)
], Ticket.prototype, "espacioCodigo", void 0);
__decorate([
    (0, typeorm_1.Column)({ name: 'fecha_ingreso', type: 'timestamp', nullable: false }),
    __metadata("design:type", Date)
], Ticket.prototype, "fechaHoraIngreso", void 0);
__decorate([
    (0, typeorm_1.Column)({ name: 'fecha_salida', type: 'timestamp', nullable: true }) // permite nulo porque cuando el cliente sale es que se llena
    ,
    __metadata("design:type", Date)
], Ticket.prototype, "fechaHoraSalida", void 0);
__decorate([
    (0, typeorm_1.Column)({ name: 'estado', default: 'ACTIVO' }) // incluir un ENUM
    ,
    __metadata("design:type", String)
], Ticket.prototype, "estado", void 0);
__decorate([
    (0, typeorm_1.Column)({ name: 'tiempo_estacionado', nullable: true }),
    __metadata("design:type", Number)
], Ticket.prototype, "tiempoEstacionado", void 0);
__decorate([
    (0, typeorm_1.Column)({ name: 'created_at' }),
    __metadata("design:type", Date)
], Ticket.prototype, "createdAt", void 0);
__decorate([
    (0, typeorm_1.Column)({ name: 'updated_at' }),
    __metadata("design:type", Date)
], Ticket.prototype, "updatedAt", void 0);
exports.Ticket = Ticket = __decorate([
    (0, typeorm_1.Entity)('tickets')
], Ticket);
