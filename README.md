# DistriMicro - Sistema de Parking con Microservicios

Sistema distribuido de gestión de parking desarrollado con arquitectura de microservicios, desplegable en Kubernetes.

## 📋 Arquitectura

El proyecto está compuesto por los siguientes microservicios:

- **ms-clientes**: Microservicio de gestión de clientes (Spring Boot)
- **zone-core**: Microservicio de gestión de zonas de parking (Spring Boot)
- **tickets-graphql**: API GraphQL para gestión de tickets (Node.js/TypeScript)
- **notification-service**: Servicio de notificaciones con RabbitMQ (NestJS)
- **RabbitMQ**: Message broker para comunicación asíncrona
- **PostgreSQL**: Bases de datos para cada microservicio

## 🛠️ Prerrequisitos

### Para desarrollo local:
- Docker Desktop 20.10+
- Docker Compose 2.0+
- Java 17+
- Maven 3.8+
- Node.js 18+
- npm o yarn

### Para despliegue en Kubernetes:
- Kubernetes 1.24+ (Minikube, Docker Desktop Kubernetes, o cluster cloud)
- kubectl configurado
- Docker Hub account (para push de imágenes)

## 🚀 Instalación

### 1. Clonar el repositorio

```bash
git clone https://github.com/cjgranda19/LabKubernetes.git
cd LabKubernetes
```

### 2. Configurar variables de entorno

Cada microservicio tiene su archivo de configuración:
- `ms-clientes/src/main/resources/application.yaml`
- `zone_core/src/main/resources/application.yaml`
- `notification-service/.env` (crear si no existe)
- `tickets-graphql/.env` (crear si no existe)

### 3. Despliegue con Docker Compose (Local)

#### Opción A: Levantar todo el sistema

```bash
docker-compose up -d
```

#### Opción B: Levantar servicios individuales

```bash
# Solo RabbitMQ
cd rabbitmq
docker-compose up -d

# Microservicio de clientes
cd ms-clientes
docker-compose up -d

# Servicio de notificaciones
cd notification-service
docker-compose up -d

# Zone Core
cd zone_core
docker-compose up -d

# Tickets GraphQL
cd tickets-graphql
docker-compose up -d
```

### 4. Despliegue en Kubernetes

#### 4.1. Preparar el cluster

```bash
# Si usas Minikube
minikube start --cpus=4 --memory=8192

# Si usas Docker Desktop, habilita Kubernetes en la configuración
```

#### 4.2. Crear el namespace

```bash
kubectl apply -f k8s/namespace.yaml
```

#### 4.3. Desplegar las bases de datos

```bash
kubectl apply -f k8s/db-clientes.yaml
kubectl apply -f k8s/db-zones.yaml
kubectl apply -f k8s/db-tickets.yaml
kubectl apply -f k8s/db-notifications.yaml
```

#### 4.4. Desplegar RabbitMQ

```bash
kubectl apply -f k8s/rabbitmq.yaml
```

#### 4.5. Desplegar los microservicios

```bash
kubectl apply -f k8s/ms-clientes.yaml
kubectl apply -f k8s/zone-core.yaml
kubectl apply -f k8s/tickets-graphql.yaml
kubectl apply -f k8s/notification-service.yaml
```

#### 4.6. Verificar el despliegue

```bash
# Ver todos los pods
kubectl get pods -n parking-system

# Ver todos los servicios
kubectl get services -n parking-system

# Ver logs de un pod específico
kubectl logs <pod-name> -n parking-system
```

## 🔧 Compilar las imágenes Docker

### Microservicios Java (Spring Boot)

```bash
# MS Clientes
cd ms-clientes
mvn clean package -DskipTests
docker build -t <tu-usuario>/ms-clientes:latest .

# Zone Core
cd zone_core
mvn clean package -DskipTests
docker build -t <tu-usuario>/zone-core:latest .
```

### Microservicios Node.js

```bash
# Notification Service
cd notification-service
npm install
docker build -t <tu-usuario>/notification-service:latest .

# Tickets GraphQL
cd tickets-graphql
npm install
docker build -t <tu-usuario>/tickets-graphql:latest .
```

### Subir imágenes a Docker Hub

```bash
docker push <tu-usuario>/ms-clientes:latest
docker push <tu-usuario>/zone-core:latest
docker push <tu-usuario>/notification-service:latest
docker push <tu-usuario>/tickets-graphql:latest
```

## 📡 Endpoints

### MS Clientes
- REST API: `http://localhost:8081`
- Health: `http://localhost:8081/actuator/health`

### Zone Core
- REST API: `http://localhost:8082`
- Health: `http://localhost:8082/actuator/health`

### Tickets GraphQL
- GraphQL Playground: `http://localhost:4000/graphql`

### Notification Service
- REST API: `http://localhost:3000`

### RabbitMQ Management
- UI: `http://localhost:15672`
- Credenciales: guest/guest

## 🧪 Testing

### Tests unitarios

```bash
# Java services
cd ms-clientes
mvn test

cd zone_core
mvn test

# Node.js services
cd notification-service
npm test

cd tickets-graphql
npm test
```

### Tests de integración con Postman

Se incluyen colecciones de Postman para testing manual (no incluidas en el repositorio):
- `users_parking.postman_collection.json`
- `zonas_parking.postman_collection.json`

## 📊 Monitoreo

### Kubernetes Dashboard

```bash
# Instalar dashboard
kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.7.0/aio/deploy/recommended.yaml

# Acceder al dashboard
kubectl proxy
```

### Logs

```bash
# Ver logs de un pod
kubectl logs -f <pod-name> -n parking-system

# Ver logs de todos los pods de un deployment
kubectl logs -f deployment/<deployment-name> -n parking-system
```

## 🔄 Actualizar el sistema

### Actualizar imagen de un servicio

```bash
# 1. Construir nueva imagen
docker build -t <tu-usuario>/<servicio>:<version> .

# 2. Subir a Docker Hub
docker push <tu-usuario>/<servicio>:<version>

# 3. Actualizar el deployment
kubectl set image deployment/<deployment-name> <container-name>=<tu-usuario>/<servicio>:<version> -n parking-system

# O aplicar el archivo YAML actualizado
kubectl apply -f k8s/<servicio>.yaml
```

## 🗑️ Limpieza

### Docker Compose

```bash
docker-compose down -v
```

### Kubernetes

```bash
# Eliminar todo el namespace (cuidado: borra todo)
kubectl delete namespace parking-system

# O eliminar recursos individuales
kubectl delete -f k8s/
```

## 🤝 Contribuir

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📝 Licencia

Este proyecto es parte de un trabajo académico de Aplicaciones Distribuidas.

## 👥 Autores

- [cjgranda19](https://github.com/cjgranda19)

## 📧 Soporte

Para problemas o preguntas, abre un issue en el repositorio de GitHub.
