# Plataforma de Aprendizaje - Spring Boot

## Descripción

Este proyecto corresponde a una plataforma de aprendizaje en línea desarrollada con Spring Boot. La aplicación permite gestionar usuarios, cursos, inscripciones, evaluaciones, pagos y notificaciones mediante endpoints REST.

El sistema está construido como una aplicación backend con arquitectura por capas, separando controladores, servicios, repositorios y modelos. Además, se incluye una propuesta de arquitectura basada en microservicios para explicar cómo podría evolucionar el sistema en un entorno más escalable.

---

## Arquitectura propuesta

Para cubrir los requerimientos del caso, se propone dividir la plataforma en los siguientes microservicios:

- **Auth Service**: gestión de usuarios, roles y autenticación.
- **Course Service**: gestión de cursos e inscripciones.
- **Assignment and Evaluation Service**: gestión de evaluaciones, tareas y calificaciones.
- **Payment Service**: gestión de pagos.
- **Notification Service**: gestión de notificaciones.

La comunicación propuesta entre los servicios es mediante REST, usando JSON para el intercambio de datos.

La explicación completa de la arquitectura y el diagrama se encuentran en:

```txt
docs/arquitectura_microservicios.md
```

---

## Tecnologías utilizadas

- Java 17
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Spring Data JDBC
- Spring Boot Actuator
- Spring AOP
- Oracle Database
- Oracle JDBC Driver
- Hibernate
- HikariCP
- Maven
- Postman

---

## Estructura principal

```txt
src/main/java/com/duoc/LearningPlatformValidation
├── aspect
├── controller
├── exception
├── model
├── repository
└── service
```

La aplicación mantiene una separación por capas:

- `controller`: recibe las solicitudes HTTP.
- `service`: contiene la lógica de negocio.
- `repository`: comunica la aplicación con la base de datos.
- `model`: define las entidades del sistema.
- `exception`: centraliza el manejo de errores.
- `aspect`: contiene la lógica transversal de logging.

---

## Configuración de base de datos

El proyecto utiliza Oracle Database como motor principal. La conexión se realiza mediante Spring Data JPA, Hibernate y HikariCP.

Para evitar dejar credenciales directamente en el repositorio, la configuración usa variables de entorno:

```txt
DB_URL
DB_USERNAME
DB_PASSWORD
DB_DRIVER
DB_DIALECT
```

Ejemplo en PowerShell:

```powershell
$env:DB_URL="jdbc:oracle:thin:@..."
$env:DB_USERNAME="USUARIO"
$env:DB_PASSWORD="CONTRASEÑA"
$env:DB_DRIVER="oracle.jdbc.OracleDriver"
$env:DB_DIALECT="org.hibernate.dialect.OracleDialect"
```

Aunque en algunos recursos se menciona H2 como alternativa para pruebas locales, en este proyecto se utiliza Oracle Database, ya que corresponde al motor usado durante la asignatura.

---

## Ejecución del proyecto

Compilar el proyecto:

```bash
mvn clean install
```

Ejecutar la aplicación:

```bash
mvn spring-boot:run
```

La aplicación queda disponible en:

```txt
http://localhost:8080
```

---

## Endpoints principales

### Cursos

```txt
GET    /api/cursos
GET    /api/cursos/{id}
POST   /api/cursos
PUT    /api/cursos/{id}
DELETE /api/cursos/{id}
```

### Usuarios

```txt
GET    /api/usuarios
GET    /api/usuarios/{id}
POST   /api/usuarios
PUT    /api/usuarios/{id}
DELETE /api/usuarios/{id}
```

### Inscripciones

```txt
GET    /api/inscripciones
GET    /api/inscripciones/{id}
GET    /api/inscripciones/curso/{cursoId}
POST   /api/inscripciones
PUT    /api/inscripciones/{id}
DELETE /api/inscripciones/{id}
```

### Evaluaciones

```txt
GET    /api/evaluaciones
GET    /api/evaluaciones/{id}
GET    /api/evaluaciones/curso/{cursoId}
POST   /api/evaluaciones
PUT    /api/evaluaciones/{id}
DELETE /api/evaluaciones/{id}
```

### Pagos

```txt
GET    /api/pagos
GET    /api/pagos/{id}
GET    /api/pagos/inscripcion/{inscripcionId}
GET    /api/pagos/estado/{estado}
POST   /api/pagos
PUT    /api/pagos/{id}
PUT    /api/pagos/{id}/aprobar
PUT    /api/pagos/{id}/rechazar
DELETE /api/pagos/{id}
```

### Notificaciones

```txt
GET    /api/notificaciones
GET    /api/notificaciones/{id}
GET    /api/notificaciones/usuario/{usuarioId}
GET    /api/notificaciones/leida/{leida}
GET    /api/notificaciones/tipo/{tipo}
POST   /api/notificaciones
PUT    /api/notificaciones/{id}
PUT    /api/notificaciones/{id}/leer
DELETE /api/notificaciones/{id}
```

---

## Logging con Spring AOP

El proyecto incorpora Spring AOP mediante la clase `LoggingAspect`.

Esta clase registra automáticamente la ejecución de métodos en los paquetes `controller` y `service`, mostrando información como:

- método ejecutado,
- inicio y fin de ejecución,
- tiempo de ejecución,
- errores producidos.

Esto permite separar el logging de la lógica principal de negocio.

---

## Manejo global de errores

El manejo de errores se centraliza mediante:

```txt
GlobalExceptionHandler.java
RecursoNoEncontradoException.java
```

Cuando un recurso no existe, el sistema responde con un `404 Not Found` y un JSON controlado.

Ejemplo:

```json
{
  "timestamp": "2026-05-04T00:08:12",
  "status": 404,
  "error": "Recurso no encontrado",
  "message": "Curso no encontrado con ID: 3",
  "path": "/api/cursos/3"
}
```

También se manejan errores de solicitud inválida y errores generales del servidor.

---

## Pruebas con Postman

Se realizaron pruebas sobre el CRUD de cursos:

```txt
GET    /api/cursos
POST   /api/cursos
GET    /api/cursos/{id}
PUT    /api/cursos/{id}
DELETE /api/cursos/{id}
```

También se probaron flujos adicionales:

```txt
POST /api/pagos
PUT  /api/pagos/{id}/aprobar

POST /api/notificaciones
PUT  /api/notificaciones/{id}/leer
```

Estas pruebas permitieron validar la persistencia en base de datos, las respuestas HTTP y el manejo de errores.

---

## Actuator

El proyecto incluye Spring Boot Actuator para revisar el estado del servicio.

Endpoint principal:

```txt
GET /actuator/health
```

---

## Relación con los requerimientos

| Requerimiento | Estado en el proyecto |
|---|---|
| Usuarios y autenticación | Se implementa gestión base de usuarios y se propone Auth Service en la arquitectura. |
| Gestión de cursos | Implementado mediante CRUD de cursos. |
| Inscripción en cursos | Implementado mediante módulo de inscripciones. |
| Evaluaciones | Implementado mediante módulo de evaluaciones. |
| Pagos | Implementado mediante módulo de pagos con estados. |
| Notificaciones | Implementado mediante módulo de notificaciones. |
| Comunicación entre microservicios | Propuesta documentada usando REST. |

---

## Estado del proyecto

La aplicación cuenta con backend funcional en Spring Boot, conexión a Oracle Database, endpoints REST, servicios de negocio separados, logging con AOP, manejo global de excepciones y documentación de arquitectura.