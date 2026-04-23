# libWill

Backend de `libWill` con base distribuida funcional para U1: Config Server, Eureka, Gateway y `producto-service`.

## Estado actual (U1)

Componentes implementados:

- Spring Boot 3.5.x
- Java 17
- Maven
- MySQL 8.4 en Docker
- Config Server (`infra/config-server`)
- Registry/Eureka (`infra/registry-server`)
- API Gateway (`infra/gateway`)
- Microservicio `producto-service` (`services/producto-service`)
- Config Repo centralizado (`infra/config-repo`)

## Servicios y puertos (dev)

- Config Server: `http://localhost:7071`
- Eureka: `http://localhost:7081`
- Gateway: `http://localhost:7091`
- Producto Service: `http://localhost:9091`
- MySQL producto: `localhost:3391`

## Estructura

```text
libWill/
  infra/
    config-repo/
    config-server/
    registry-server/
    gateway/
  services/
    producto-service/
  ARCHITECTURE.md
```

## Estado por hito

- `vs00-libwill-base`: base en Docker.
- `vs01-producto-service`: microservicio de productos.
- `vs02-config-server`: configuracion centralizada.
- `vs03-registry-server`: descubrimiento de servicios.
- `gateway`: implementado y enruta `producto-service`.
- Siguiente: Feign + seguridad + resiliencia (U2).

## Documentacion

Ver tambien:

- [Arquitectura](ARCHITECTURE.md)
- [Infraestructura](infra/README.md)
- [Servicios](services/README.md)
