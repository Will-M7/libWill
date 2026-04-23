# libWill

Backend base de libWill preparado para evolucionar hacia una arquitectura de microservicios con Spring Boot, Docker, Config Server, Eureka, Gateway y Feign.

## Estado actual

Este repositorio contiene el backend inicial funcionando como una sola aplicacion Spring Boot, ya renombrada a `libWill` y lista para trabajar con Docker.

Componentes actuales:

- Spring Boot 3.5.x
- Java 17
- Maven
- MySQL 8.4 en Docker
- JWT
- Spring Security
- Spring Data JPA
- Docker Compose

## Ejecutar con Docker

Desde la raiz del proyecto:

```powershell
docker compose up -d --build
```

Servicios publicados:

```text
Backend: http://localhost:8080
MySQL: localhost:3308
Base de datos: libWill
Usuario MySQL: root
Password MySQL: root
```

Probar backend:

```powershell
curl.exe http://localhost:8080/api/productos
```

## Credenciales iniciales

```text
Usuario: administrador
Password: admin12345
```

## Estructura

```text
libWill/
  infra/
    config-repo/
  services/
  src/
  docker-compose.yml
  Dockerfile
  ARCHITECTURE.md
```

## Roadmap

El orden de trabajo seguira el avance de clases:

1. `vs00-libwill-base`: backend base con Docker.
2. `vs01-config-server`: configuracion centralizada.
3. `vs02-registry-server`: descubrimiento de servicios con Eureka.
4. `vs03-gateway`: entrada unificada con Spring Cloud Gateway.
5. `vs04-producto-service`: primer microservicio extraido.
6. `vs05-feign`: comunicacion entre servicios.
7. `vs06-circuit-breaker`: resiliencia.

## Documentacion

Ver tambien:

- [Arquitectura](ARCHITECTURE.md)
- [Infraestructura](infra/README.md)
- [Servicios](services/README.md)
