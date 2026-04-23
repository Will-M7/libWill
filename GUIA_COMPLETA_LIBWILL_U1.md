# Guia Completa U1 - libWill

## 1) Resultado final

La base distribuida de U1 quedo funcional con estos componentes:

- `infra/config-server` en `http://localhost:7071`
- `infra/registry-server` (Eureka) en `http://localhost:7081`
- `infra/gateway` en `http://localhost:7091`
- `services/producto-service` en `http://localhost:9091`
- MySQL Docker de productos en `localhost:3391`

Validacion integral:

- `GET http://localhost:7071/producto-service/dev` carga configuracion externa.
- `GET http://localhost:7081/eureka/apps` muestra `PRODUCTO-SERVICE` y `GATEWAY`.
- `GET http://localhost:7091/api/productos` enruta correctamente al microservicio.

## 2) Codigo principal y funcionalidad

### producto-service

- `services/producto-service/src/main/java/com/libwill/producto/ProductoServiceApplication.java`
  - Arranque del microservicio.
- `services/producto-service/src/main/java/com/libwill/producto/controller/ProductController.java`
  - API REST de productos.
  - Endpoints: listar, obtener por id, crear, actualizar, eliminar, subir imagen.
- `services/producto-service/src/main/java/com/libwill/producto/service/impl/ProductServiceImpl.java`
  - Logica de negocio del dominio de productos.
- `services/producto-service/src/main/java/com/libwill/producto/repository/ProductRepository.java`
  - Persistencia JPA.
- `services/producto-service/src/main/java/com/libwill/producto/entity/ProductEntity.java`
  - Modelo de tabla principal.
- `services/producto-service/src/main/java/com/libwill/producto/dto/ProductDTO.java`
  - Contrato de entrada/salida para API.
- `services/producto-service/src/main/java/com/libwill/producto/mapper/ProductMapper.java`
  - Conversion `Entity <-> DTO` con MapStruct.
- `services/producto-service/src/main/java/com/libwill/producto/exception/GlobalExceptionHandler.java`
  - Manejo global de errores para respuestas controladas.
- `services/producto-service/src/main/resources/application.yml`
  - Nombre del servicio y carga de config externa por Config Server.
- `infra/config-repo/producto-service-dev.yml`
  - Puerto, datasource local, JPA, Eureka para entorno `dev`.
- `infra/config-repo/producto-service-prod.yml`
  - Configuracion orientada a contenedores para `prod`.

### infraestructura

- `infra/config-server/src/main/java/com/libwill/configserver/ConfigServerApplication.java`
  - `@EnableConfigServer`, servidor de configuracion central.
- `infra/registry-server/src/main/java/com/libwill/registryserver/RegistryServerApplication.java`
  - `@EnableEurekaServer`, descubrimiento de servicios.
- `infra/gateway/src/main/java/com/libwill/gateway/GatewayApplication.java`
  - Entrada unica del sistema.
- `infra/config-repo/gateway-dev.yml`
  - Ruta gateway: `/api/productos/**` -> `lb://producto-service`.
- `infra/config-repo/registry-server-dev.yml`
  - Puerto y parametros del servidor Eureka.

## 3) Comandos usados y cuando se usan

### compilacion

```powershell
cd services/producto-service
mvn clean compile

cd ../../infra/gateway
mvn clean compile
```

Uso: validar que el codigo compila antes de ejecutar.

### arranque por modulo (dev)

```powershell
cd infra/config-server
mvn spring-boot:run

cd ../registry-server
mvn spring-boot:run

cd ../../services/producto-service
mvn spring-boot:run

cd ../../infra/gateway
mvn spring-boot:run
```

Uso: levantar entorno distribuido local.

### verificaciones

```powershell
curl.exe http://localhost:7071/producto-service/dev
curl.exe http://localhost:7081/eureka/apps
curl.exe http://localhost:9091/api/productos
curl.exe http://localhost:7091/api/productos
```

Uso: validar configuracion externa, registro y enrutamiento.

### git (flujo tecnico)

```powershell
git checkout -b tarea/producto-service
git add services/producto-service
git commit -m "feat: add producto service"
git push -u origin tarea/producto-service

git tag vs01-producto-service
git push origin vs01-producto-service
```

Uso: trazabilidad por etapas, evidencia evaluable por rama/tag.

## 4) Problemas reales corregidos

- Error de dependencias faltantes (`MapStruct` / componentes cloud): se completo `pom.xml` y compilacion estable.
- Error de JSON en PowerShell con `curl`: se uso `Invoke-RestMethod` para pruebas confiables.
- Error gateway `500` por hostname en Eureka: se ajusto config del servicio para registro resolvible y se reinicio.

## 5) Estado de entrega

Base de U1 lista y funcionando para continuar U2:

- Configuracion externa
- Descubrimiento de servicios
- Gateway funcional
- Microservicio de productos operativo con base de datos Docker
- Repositorio con ramas y tags por avance
