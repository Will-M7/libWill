# libWill Services

Esta carpeta queda reservada para la separacion gradual del monolito en microservicios.

Orden recomendado:

1. Mantener el backend actual estable.
2. Crear infraestructura: `config-server`, `registry-server`, `gateway`.
3. Extraer primero el modulo de productos como `producto-service`.
4. Registrar servicios en Eureka y enrutar por Gateway.
5. Agregar comunicacion entre servicios con Feign.
6. Agregar resiliencia con Circuit Breaker.
