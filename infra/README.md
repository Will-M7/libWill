# libWill Infra

Esta carpeta contiene la infraestructura distribuida base implementada en la U1.

Estructura objetivo:

```text
infra/
  config-repo/
  config-server/
  registry-server/
  gateway/
services/
  producto-service/
  auth-service/
```

Componentes ya operativos:

- `config-server` (configuracion centralizada)
- `registry-server` (Eureka)
- `gateway` (enrutamiento a microservicios)
- `config-repo` (archivos por servicio y entorno)
