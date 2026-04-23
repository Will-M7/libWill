# libWill Architecture

## Estado actual

Base distribuida de U1 implementada y funcional:

- Config Server
- Registry Server (Eureka)
- API Gateway
- producto-service
- MySQL Docker para el microservicio

## Estado objetivo

```text
Cliente
  -> Gateway
  -> Eureka / Registry
  -> Microservicios libWill
  -> MySQL por servicio
```

## Convenciones

- Nombre visible del proyecto: `libWill`.
- Service-id tecnico: `libwill`.
- Base de datos principal actual: `libWill`.
- Paquete Java: `libreria.com.libwill`.
- Contenedores y redes Docker: prefijo `libwill`.

## Orden de evolucion

1. Dockerizar base de datos y backend. (completado)
2. Crear `config-server`. (completado)
3. Mover configuraciones a `infra/config-repo`. (completado)
4. Crear `registry-server`. (completado)
5. Crear `gateway`. (completado)
6. Separar `producto-service`. (completado)
7. Integrar servicios con Feign. (siguiente paso)
