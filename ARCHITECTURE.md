# libWill Architecture

## Estado actual

`libWill` funciona como backend Spring Boot unico, con MySQL en Docker.

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

1. Dockerizar base de datos y backend.
2. Crear `config-server`.
3. Mover configuraciones a `infra/config-repo`.
4. Crear `registry-server`.
5. Crear `gateway`.
6. Separar `producto-service`.
7. Integrar servicios con Feign.
