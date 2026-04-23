# libWill Infra

Esta carpeta prepara la estructura que se uso en clases para evolucionar hacia microservicios.

Estructura objetivo:

```text
infra/
  config-repo/
  config-server/
  registry-server/
  gateway/
services/
  libwill/
  producto-service/
  auth-service/
```

Por ahora el backend sigue funcionando como una sola aplicacion Spring Boot. El siguiente paso sera crear `config-server`, luego `registry-server`, luego `gateway`, y recien despues separar servicios.
