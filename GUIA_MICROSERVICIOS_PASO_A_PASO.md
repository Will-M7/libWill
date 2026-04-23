# Guia Paso a Paso - Como crear microservicios (caso libWill)

## Objetivo

Repetir el mismo metodo aplicado con `producto-service` para nuevos microservicios sin romper lo ya logrado.

## Fase 0: Preparar rama

```powershell
cd D:\Proyecto\cuak\back\Molineria
git checkout main
git pull
git checkout -b tarea/<nombre-service>
```

Ejemplo: `tarea/inventario-service`.

## Fase 1: Crear el microservicio

1. Crear carpeta dentro de `services/`.
2. Generar proyecto Spring Boot (Maven, Java 17).
3. Dependencias minimas recomendadas:
   - `spring-boot-starter-web`
   - `spring-boot-starter-data-jpa`
   - `mysql-connector-j`
   - `spring-cloud-starter-config`
   - `spring-cloud-starter-netflix-eureka-client`
   - `spring-boot-starter-actuator`
   - `spring-boot-starter-validation`

Compilar:

```powershell
cd services/<nombre-service>
mvn clean compile
```

## Fase 2: Config externa (obligatoria)

1. En `services/<nombre-service>/src/main/resources/application.yml`:
   - `spring.application.name: <nombre-service>`
   - `spring.config.import: optional:configserver:http://localhost:7071`
   - `spring.profiles.active: dev`
2. Crear archivo en `infra/config-repo/<nombre-service>-dev.yml` con:
   - `server.port`
   - `spring.datasource.*`
   - `spring.jpa.*`
   - `eureka.client.service-url.defaultZone`

Prueba:

```powershell
curl.exe http://localhost:7071/<nombre-service>/dev
```

## Fase 3: Registro en Eureka

1. Levantar `registry-server`.
2. Levantar el microservicio.
3. Verificar registro:

```powershell
curl.exe http://localhost:7081/eureka/apps
```

Debe aparecer en estado `UP`.

## Fase 4: Exponer por Gateway

1. Agregar ruta en `infra/config-repo/gateway-dev.yml`:

```yaml
spring:
  cloud:
    gateway:
      server:
        webflux:
          routes:
            - id: inventario-route
              uri: lb://inventario-service
              predicates:
                - Path=/api/inventario/**
```

2. Reiniciar gateway para recargar configuracion.
3. Probar acceso:

```powershell
curl.exe http://localhost:7091/api/inventario
```

## Fase 5: Persistencia aislada

Regla: cada microservicio debe tener su propia base de datos o esquema controlado por ese servicio.

Ejemplo contenedor MySQL nuevo:

```powershell
docker run -d --name mysql-inventario-dev -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=db_inventario -p 3392:3306 mysql:8.4
```

Luego configurar `jdbc:mysql://localhost:3392/db_inventario`.

## Fase 6: Validacion minima (checklist)

- [ ] Compila (`mvn clean compile`)
- [ ] Levanta (`mvn spring-boot:run`)
- [ ] Config externa responde desde config-server
- [ ] Servicio registrado en Eureka
- [ ] Endpoint directo responde
- [ ] Endpoint por gateway responde

## Fase 7: Git y evidencia

```powershell
git add services/<nombre-service> infra/config-repo/<nombre-service>-dev.yml infra/config-repo/gateway-dev.yml
git commit -m "feat: add <nombre-service> microservice"
git push -u origin tarea/<nombre-service>
git tag vs0X-<nombre-service>
git push origin vs0X-<nombre-service>
```

Luego crear PR en GitHub para revisión.

## Orden recomendado para U2

1. `auth-service` (seguridad central)
2. `inventario-service`
3. `venta-service`
4. Integracion Feign entre servicios
5. Circuit breaker / resiliencia

Con este flujo, cada nuevo microservicio sale consistente con la arquitectura y no rompe el ecosistema ya estable.
