# Plataforma de Aprendizaje - Spring Boot

## Descripción

Este proyecto corresponde a una plataforma de aprendizaje en línea desarrollada con Spring Boot. La aplicación permite gestionar usuarios, cursos, inscripciones, evaluaciones, pagos y notificaciones mediante endpoints REST.

El sistema está construido como una aplicación backend con arquitectura por capas, separando controladores, servicios, repositorios, modelos, DTO, configuración y manejo de errores. Además, se incluye una propuesta de arquitectura basada en microservicios para explicar cómo podría evolucionar el sistema en un entorno más escalable.

En esta versión se incorpora integración con AWS S3 para almacenar archivos físicos asociados al resumen de inscripción. Esta funcionalidad permite generar, subir, descargar, modificar y eliminar el archivo del resumen desde un bucket en la nube.

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
docs/arquitectura_micro.md
```

---

## Tecnologías utilizadas

- Java 17
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Spring Boot Actuator
- Spring AOP
- Oracle Database
- Oracle JDBC Driver
- Hibernate
- HikariCP
- Maven Wrapper
- AWS S3
- AWS SDK for Java
- Docker
- Docker Hub
- GitHub Actions
- EC2
- Postman / PowerShell

---

## Estructura principal

```txt
src/main/java/com/duoc/LearningPlatformValidation
├── aspect
├── config
├── controller
├── dto
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
- `dto`: define objetos de transferencia de datos.
- `exception`: centraliza el manejo de errores.
- `aspect`: contiene la lógica transversal de logging.
- `config`: contiene configuraciones adicionales, como la conexión con AWS S3.

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
$env:DB_USERNAME="LPV_APP"
$env:DB_PASSWORD="********"
$env:DB_DRIVER="oracle.jdbc.OracleDriver"
$env:DB_DIALECT="org.hibernate.dialect.OracleDialect"
```

Aunque en algunos recursos se menciona H2 como alternativa para pruebas locales, en este proyecto se utiliza Oracle Database, ya que corresponde al motor usado durante la asignatura.

---

## Configuración de AWS S3

El proyecto incorpora almacenamiento de archivos en AWS S3 para guardar los resúmenes de inscripción generados por el sistema.

Bucket utilizado:

```txt
lpv-resumenes-inscripcion
```

Región:

```txt
us-east-1
```

La configuración se realiza mediante variables de entorno:

```txt
AWS_REGION
AWS_S3_BUCKET
AWS_ACCESS_KEY_ID
AWS_SECRET_ACCESS_KEY
AWS_SESSION_TOKEN
```

Ejemplo en PowerShell:

```powershell
$env:AWS_REGION="us-east-1"
$env:AWS_S3_BUCKET="lpv-resumenes-inscripcion"

$env:AWS_ACCESS_KEY_ID="********"
$env:AWS_SECRET_ACCESS_KEY="********"
$env:AWS_SESSION_TOKEN="********"
```

Las credenciales de AWS no se almacenan en el código fuente. En este proyecto se utilizaron credenciales temporales entregadas por el entorno académico AWS Learner Lab.

La configuración del cliente S3 se encuentra en:

```txt
src/main/java/com/duoc/LearningPlatformValidation/config/S3Config.java
```

El servicio encargado de operar con S3 se encuentra en:

```txt
src/main/java/com/duoc/LearningPlatformValidation/service/S3StorageService.java
```

---

## Ejecución del proyecto

Compilar el proyecto:

```bash
./mvnw clean compile
```

En Windows PowerShell:

```powershell
.\mvnw clean compile
```

Ejecutar la aplicación:

```bash
./mvnw spring-boot:run
```

En Windows PowerShell:

```powershell
.\mvnw spring-boot:run
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
GET    /api/inscripciones/estudiante/{estudianteId}
POST   /api/inscripciones
GET    /api/inscripciones/{id}/boleta
DELETE /api/inscripciones/{id}
```

### Resumen de inscripción y AWS S3

```txt
GET    /api/inscripciones/{id}/resumen/archivo
POST   /api/inscripciones/{id}/resumen/s3
GET    /api/inscripciones/{id}/resumen/s3/download
PUT    /api/inscripciones/{id}/resumen/s3
DELETE /api/inscripciones/{id}/resumen/s3
```

Detalle de los endpoints:

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/inscripciones/{id}/resumen/archivo` | Genera y descarga el resumen como archivo físico `.txt`. |
| POST | `/api/inscripciones/{id}/resumen/s3` | Genera el resumen y lo sube al bucket S3. |
| GET | `/api/inscripciones/{id}/resumen/s3/download` | Descarga desde S3 el archivo del resumen. |
| PUT | `/api/inscripciones/{id}/resumen/s3` | Reemplaza/modifica el archivo del resumen en S3. |
| DELETE | `/api/inscripciones/{id}/resumen/s3` | Elimina el archivo del resumen desde S3. |

Cada archivo se almacena dentro del bucket usando una carpeta cuyo nombre corresponde al número del resumen:

```txt
BOL-00001/resumen-inscripcion-BOL-00001.txt
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

## Ejemplo de flujo probado

### 1. Crear usuario

```powershell
$body = @{
    nombre = "Juan Perez"
    correo = "juan.perez@test.cl"
    contrasena = "123456"
    rol = "ESTUDIANTE"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/usuarios" -Method Post -ContentType "application/json" -Body $body
```

### 2. Crear curso

```powershell
$body = @{
    nombre = "Spring Boot desde cero"
    instructor = "Carlos Soto"
    duracion = "40 horas"
    costo = 50000
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/cursos" -Method Post -ContentType "application/json" -Body $body
```

### 3. Crear inscripción

```powershell
$body = @{
    estudianteId = 1
    cursoIds = @(1)
    metodoPago = "TARJETA"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/inscripciones" -Method Post -ContentType "application/json" -Body $body
```

### 4. Descargar resumen como archivo físico

```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/inscripciones/1/resumen/archivo" -OutFile "resumen-inscripcion-1.txt"
```

### 5. Subir resumen a S3

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/inscripciones/1/resumen/s3" -Method Post
```

Respuesta esperada:

```txt
archivo                           numeroResumen mensaje                                           rutaS3
-------                           ------------- -------                                           ------
resumen-inscripcion-BOL-00001.txt BOL-00001     Resumen de inscripción subido correctamente a S3 BOL-00001/resumen-inscripcion-BOL-00001.txt
```

### 6. Descargar resumen desde S3

```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/inscripciones/1/resumen/s3/download" -OutFile "resumen-descargado-s3.txt"
```

### 7. Modificar resumen en S3

```powershell
$nuevoContenido = @"
RESUMEN DE INSCRIPCION MODIFICADO
=================================

Numero de resumen: BOL-00001
ID de inscripcion: 1
ID estudiante: 1
Fecha de emision: 2026-05-31

Cursos inscritos:
- Spring Boot desde cero | ID curso: 1 | Costo: `$50000

Total pagado: `$50000
Metodo de pago: TARJETA
Estado de pago: APROBADO_SIMULADO

Observacion: Archivo modificado correctamente desde el endpoint PUT.
"@

Invoke-RestMethod -Uri "http://localhost:8080/api/inscripciones/1/resumen/s3" -Method Put -ContentType "text/plain; charset=utf-8" -Body $nuevoContenido
```

### 8. Eliminar resumen desde S3

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/inscripciones/1/resumen/s3" -Method Delete
```

---

## Pruebas con Postman / PowerShell

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

Para la integración con AWS S3 se validó el siguiente flujo:

```txt
GET    /api/inscripciones/{id}/resumen/archivo
POST   /api/inscripciones/{id}/resumen/s3
GET    /api/inscripciones/{id}/resumen/s3/download
PUT    /api/inscripciones/{id}/resumen/s3
DELETE /api/inscripciones/{id}/resumen/s3
```

Estas pruebas permitieron validar la persistencia en base de datos, la generación de archivos físicos, la subida al bucket S3, la descarga desde S3, la modificación del archivo y la eliminación del objeto almacenado.

---

## CI/CD

El proyecto cuenta con un pipeline de GitHub Actions que automatiza:

1. Descarga del código fuente.
2. Configuración de Java 21.
3. Compilación del proyecto Spring Boot usando Maven Wrapper.
4. Construcción de imagen Docker.
5. Inicio de sesión en Docker Hub.
6. Publicación de imagen en Docker Hub.
7. Despliegue en una instancia EC2 mediante SSH.
8. Ejecución del contenedor con variables de entorno.

El pipeline utiliza:

```txt
actions/checkout@v4
actions/setup-java@v4
docker/login-action
```

El uso de `docker/login-action` permite reemplazar el login manual a Docker Hub por una acción oficial, dejando el pipeline más ordenado y alineado con buenas prácticas.

Actualmente se utiliza:

```txt
-DskipTests
```

---

## Docker

El proyecto incluye un `Dockerfile` para construir y ejecutar la aplicación en contenedor.

El despliegue en EC2 ejecuta el contenedor usando variables de entorno para la conexión a la base de datos y la configuración de la aplicación.

Además, se utiliza:

```txt
--restart unless-stopped
```

Esto permite que el servicio se reinicie automáticamente si el contenedor se detiene o si la instancia se reinicia.

---

## Actuator

El proyecto incluye Spring Boot Actuator para revisar el estado del servicio.

Endpoint principal:

```txt
GET /actuator/health
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

## Relación con los requerimientos

| Requerimiento | Estado en el proyecto |
|---|---|
| Usuarios y autenticación | Se implementa gestión base de usuarios y se propone Auth Service en la arquitectura. |
| Gestión de cursos | Implementado mediante CRUD de cursos. |
| Inscripción en cursos | Implementado mediante módulo de inscripciones. |
| Generación de resumen de inscripción | Implementado mediante endpoint de boleta/resumen. |
| Descarga de resumen como archivo físico | Implementado mediante archivo `.txt`. |
| Subida de resumen a AWS S3 | Implementado mediante endpoint dedicado. |
| Carpeta por número de resumen en S3 | Implementado con ruta `BOL-00001/resumen-inscripcion-BOL-00001.txt`. |
| Descarga de resumen desde S3 | Implementado. |
| Modificación de resumen en S3 | Implementado. |
| Eliminación de resumen en S3 | Implementado. |
| Evaluaciones | Implementado mediante módulo de evaluaciones. |
| Pagos | Implementado mediante módulo de pagos con estados. |
| Notificaciones | Implementado mediante módulo de notificaciones. |
| Comunicación entre microservicios | Propuesta documentada usando REST. |
| CI/CD con GitHub Actions, Docker Hub y EC2 | Implementado. |

---