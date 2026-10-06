<div align="center">

# 🏥 Clínica Aviva — Sistema de Gestión de Citas Médicas

[![Angular](https://img.shields.io/badge/Angular-17.3.0-DD0031?style=for-the-badge&logo=angular&logoColor=white)](https://angular.io/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.5-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/projects/jdk/17/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.4.5-3178C6?style=for-the-badge&logo=typescript&logoColor=white)](https://www.typescriptlang.org/)
[![License: Academic](https://img.shields.io/badge/License-Academic-blueviolet?style=for-the-badge)](.)

**Proyecto Académico — Ingeniería de Sistemas e Informática**

*Sistema web integral para la digitalización y automatización del ciclo de vida de citas médicas en el Hospital Aviva.*

</div>

---

## 📋 Tabla de Contenidos

1. [El Problema Operativo](#-el-problema-operativo)
2. [La Solución](#-la-solución)
3. [Integrantes del Equipo](#-integrantes-del-equipo)
4. [Arquitectura del Sistema](#-arquitectura-del-sistema)
5. [Stack Tecnológico](#-stack-tecnológico)
6. [Funcionalidades por Módulo](#-funcionalidades-por-módulo)
7. [Estructura del Repositorio](#-estructura-del-repositorio)
8. [Prerequisitos e Instalación](#-prerequisitos-e-instalación)
9. [Configuración del Entorno](#%EF%B8%8F-configuración-del-entorno)
10. [Ejecución del Proyecto](#-ejecución-del-proyecto)
11. [Endpoints de la API REST](#-endpoints-de-la-api-rest)
12. [Decisiones de Diseño Clave](#-decisiones-de-diseño-clave)

---

## 🔴 El Problema Operativo

El Hospital Aviva operaba bajo un modelo de gestión de citas **completamente manual**, generando una cadena de ineficiencias que impactaba directamente en la calidad del servicio:

| Problema | Impacto Medido |
|---|---|
| Agendamiento exclusivo por central telefónica | Cuello de botella y tiempos de espera inaceptables |
| Registros en cuadernos y hojas de Excel | Pérdida de información, duplicidad de reservas y errores humanos |
| Sin sistema de recordatorios automáticos | **Tasa de ausentismo del 30–35%** con costos operativos directos |
| Sin control de concurrencia en reservas | Dobles reservas en el mismo turno médico |
| Sin reportes ni métricas en tiempo real | Imposibilidad de tomar decisiones gerenciales basadas en datos |

El resultado: médicos con tiempos muertos, pacientes frustrados y una administración saturada de trabajo operativo de bajo valor.

---

## ✅ La Solución

**Clínica Aviva Web** digitaliza y automatiza el ciclo completo de una cita médica, ofreciendo:

- **Autogestión 24/7** para pacientes sin depender de la central telefónica.
- **Control de concurrencia** mediante *stored procedures* con bloqueos pesimistas (`SELECT ... FOR UPDATE`) que garantizan que dos pacientes no reserven el mismo turno simultáneamente.
- **Notificaciones y comprobantes automáticos** en PDF para reducir el ausentismo.
- **Paneles especializados** por rol (Paciente, Médico, Administrador) con acceso protegido por JWT.
- **Reportes y métricas** en tiempo real para la toma de decisiones gerenciales.

---

## 👥 Integrantes del Equipo

> Este proyecto fue desarrollado por el siguiente equipo. Los colaboradores pueden completar sus datos en esta sección.

| Rol | Nombre Completo | GitHub |
|---|---|---|
| Desarrollador | Manuel Rivera | [@manuelrivera8245](https://github.com/manuelrivera8245) |
| Desarrollador | [Nombre Apellido] | [@usuario](https://github.com/usuario) |
| Desarrollador | [Nombre Apellido] | [@usuario](https://github.com/usuario) |
| Desarrollador | [Nombre Apellido] | [@usuario](https://github.com/usuario) |
| Desarrollador | [Nombre Apellido] | [@usuario](https://github.com/usuario) |

---

## 🏛 Arquitectura del Sistema

El sistema sigue una arquitectura **Cliente-Servidor desacoplada** con diseño de **N-Capas** en el backend, comunicadas a través de una **API RESTful** con intercambio de datos en JSON.

```
┌─────────────────────────────────────────────────────────────────────┐
│                         CLIENTE (Navegador)                         │
│                    Angular 17 SPA — Puerto 4200                     │
│          [ Módulo Público | Paciente | Médico | Admin ]             │
└──────────────────────────┬──────────────────────────────────────────┘
                           │  HTTPS — REST/JSON + JWT Bearer Token
                           ▼
┌─────────────────────────────────────────────────────────────────────┐
│                   SERVIDOR — Spring Boot 3.2.5                      │
│                          Puerto 8080                                │
│  ┌─────────────────────────────────────────────────────────────┐    │
│  │   Capa de Seguridad — Spring Security + JWT Filter          │    │
│  └──────────────────────────┬──────────────────────────────────┘    │
│  ┌───────────────────────────▼──────────────────────────────────┐   │
│  │   Capa de Controladores REST (@RestController)               │   │
│  │   Auth | Cita | Médico | Paciente | Admin | Reportes         │   │
│  └──────────────────────────┬───────────────────────────────────┘   │
│  ┌───────────────────────────▼──────────────────────────────────┐   │
│  │   Capa de Servicios (@Service) — Lógica de Negocio           │   │
│  │   + Generación de PDF (OpenPDF) + Manejo de Excepciones      │   │
│  └──────────────────────────┬───────────────────────────────────┘   │
│  ┌───────────────────────────▼──────────────────────────────────┐   │
│  │   Capa de Repositorios (Spring Data JPA / Hibernate)         │   │
│  │   @Repository — Entidades + DTOs + Stored Procedures         │   │
│  └──────────────────────────┬───────────────────────────────────┘   │
└──────────────────────────────┼──────────────────────────────────────┘
                               │  JDBC — HikariCP Connection Pool
                               ▼
┌─────────────────────────────────────────────────────────────────────┐
│                BASE DE DATOS — MySQL 8.0                            │
│     Stored Procedures (SELECT ... FOR UPDATE) | Triggers            │
│     Relational Schema | Integrity Constraints                       │
└─────────────────────────────────────────────────────────────────────┘
```

### Patrones de Diseño Aplicados

- **DTO Pattern**: Separación estricta entre entidades JPA y objetos de transferencia de datos.
- **Repository Pattern**: Abstracción de la capa de datos mediante `JpaRepository`.
- **Singleton (IoC/DI)**: Gestión del ciclo de vida de beans por el contenedor de Spring.
- **Filter Chain**: Interceptación de requests HTTP para validación del token JWT antes de llegar al controlador.
- **Global Exception Handler**: `@ControllerAdvice` centraliza el manejo de errores con respuestas estandarizadas.

---

## 🛠 Stack Tecnológico

### 🎨 Frontend — Capa de Presentación

| Tecnología | Versión | Propósito |
|---|---|---|
| **Angular** | 17.3.0 | Framework SPA principal |
| **TypeScript** | 5.4.5 | Lenguaje de programación tipado |
| **Bootstrap** | 5.3.3 | Sistema de grillas y componentes UI |
| **ng-bootstrap** | 16.0.0 | Componentes Angular nativos de Bootstrap |
| **RxJS** | 7.8.0 | Programación reactiva y manejo de streams |
| **Font Awesome** | 6.5.1 | Biblioteca de iconos |
| **pnpm** | — | Gestor de paquetes (rápido, eficiente en disco) |
| **Angular CLI** | 17.3.0 | Herramienta de scaffolding y build |

**Módulos del Frontend:**
- `AuthModule` — Login y registro de pacientes
- `PacienteModule` — Dashboard, reservas, historial, perfil
- `MedicoModule` — Agenda diaria, gestión de citas
- `AdministradorModule` — CRUD personal, horarios, reportes
- `PublicoModule` — Landing page, especialidades, nosotros
- `SharedModule` — Componentes reutilizables: `Navbar`, `Sidebar`, `Footer`, `LoadingSpinner`, `ConfirmModal`

---

### ⚙️ Backend — Capa de Lógica de Negocio

| Tecnología | Versión | Propósito |
|---|---|---|
| **Java** | 17 (LTS) | Lenguaje de programación |
| **Spring Boot** | 3.2.5 | Framework de aplicación principal |
| **Spring Security** | (incluido) | Autenticación, autorización y filtros de seguridad |
| **JJWT** | 0.12.5 | Generación y validación de JSON Web Tokens |
| **Bcrypt** | (Spring Security) | Hash seguro de contraseñas |
| **Spring Data JPA** | (incluido) | ORM y abstracción de repositorios |
| **Hibernate** | (incluido) | Implementación JPA |
| **HikariCP** | (incluido) | Pool de conexiones de alto rendimiento |
| **OpenPDF** | 1.3.37 | Generación de comprobantes en PDF |
| **Lombok** | (incluido) | Reducción de boilerplate (getters, setters, builders) |
| **Spring Validation** | (incluido) | Validación de DTOs con anotaciones (`@Valid`) |
| **Maven** | 3.x | Gestión de dependencias y build |

**Controladores REST expuestos:**

| Controlador | Ruta Base | Responsabilidad |
|---|---|---|
| `AuthController` | `/api/auth` | Login, registro, refresh token |
| `CitaController` | `/api/citas` | CRUD y gestión de estados de citas |
| `MedicoController` | `/api/medicos` | Gestión del personal médico |
| `PacienteController` | `/api/pacientes` | Perfil y autogestión de pacientes |
| `HorarioMedicoController` | `/api/horarios` | Configuración de disponibilidad |
| `TurnoController` | `/api/turnos` | Generación y consulta de turnos |
| `EspecialidadController` | `/api/especialidades` | Catálogo de especialidades médicas |
| `DashboardController` | `/api/dashboard` | Métricas y KPIs por rol |
| `ReportesController` | `/api/reportes` | Exportación de datos e informes |
| `NotificacionController` | `/api/notificaciones` | Gestión de alertas del sistema |
| `ConfiguracionGeneralController` | `/api/configuracion` | Parámetros globales del sistema |

---

### 🗄️ Base de Datos — Capa de Datos

| Tecnología | Versión | Propósito |
|---|---|---|
| **MySQL** | 8.0+ | Motor de base de datos relacional |
| **Stored Procedures** | — | Control de concurrencia con `SELECT ... FOR UPDATE` |
| **HikariCP** | (Spring Boot) | Pool de conexiones administrado por Spring |

**Características de la capa de datos:**
- Esquema relacional completo definido en `database/clinica_aviva_schema.sql`.
- **Control de concurrencia pesimista**: Los *stored procedures* de reserva usan `SELECT ... FOR UPDATE` dentro de transacciones explícitas, garantizando integridad bajo alta concurrencia.
- Restricciones de integridad referencial (`FOREIGN KEY`, `UNIQUE`, `NOT NULL`).
- Índices optimizados para búsquedas por fecha, médico y paciente.

---

## ⚡ Funcionalidades por Módulo

### 🧑‍💼 Módulo de Paciente (Autogestión)

- **Registro e inicio de sesión** con validación de DNI y correo electrónico únicos.
- **Búsqueda dinámica de disponibilidad** filtrada por especialidad, nombre del médico y fecha.
- **Reserva de turno** con bloqueo automático del cupo para prevenir concurrencias simultáneas.
- **Dashboard personal** con visualización de citas vigentes e históricas.
- **Cancelación de reservas** con validación de política de 24 horas de antelación.
- **Descarga de comprobante PDF** generado en el servidor (OpenPDF) con detalle de la cita.
- **Actualización de perfil** con datos de contacto y dirección.

### 👨‍⚕️ Módulo de Personal Médico

- **Autenticación institucional** con credenciales internas de la clínica.
- **Agenda diaria** con vista de turnos asignados y datos del paciente programado.
- **Gestión de estado de citas**: Transición a `ATENDIDO` o `NO_ASISTIO` desde la interfaz.
- **Dashboard de métricas** con resumen de atenciones del día y tasa de asistencia.
- **Historial médico** con registro cronológico de las atenciones realizadas.
- **Gestión de perfil médico** con especialidades asignadas.

### 🛡️ Módulo de Administrador

- **CRUD completo de médicos**: Alta, edición, baja lógica y asignación de especialidades.
- **Configuración de horarios médicos**: Parametrización de días de atención, rangos horarios y duración de cada turno en minutos.
- **Generación masiva de turnos** a partir de los horarios configurados.
- **Monitor global de citas**: Visualización y control de todas las reservas activas en la institución.
- **Gestión de especialidades**: Alta y mantenimiento del catálogo de especialidades.
- **Panel de reportes**: Métricas de ausentismo, citas por especialidad y desempeño médico.
- **Configuración general del sistema**: Parámetros operativos globales de la clínica.

### 🤖 Servicios Automatizados del Sistema (Background)

- **Generación de comprobantes PDF** automática en el momento de la confirmación de reserva.
- **Sistema de notificaciones internas** para confirmaciones, recordatorios y cancelaciones.
- **Validación de reglas de negocio** automática: solapamiento de horarios, cupos máximos por turno y plazos de cancelación.
- **Manejo global de excepciones** con respuestas estandarizadas y códigos HTTP apropiados para todos los errores del sistema.

---

## 📁 Estructura del Repositorio

```
clinica-aviva/
│
├── 📂 backend/                         # Servidor Spring Boot (API REST)
│   ├── src/
│   │   └── main/java/com/clinicaaviva/
│   │       ├── config/                 # SecurityConfig, CORS, JWT
│   │       ├── controller/             # Endpoints REST
│   │       ├── dto/
│   │       │   ├── request/            # DTOs de entrada (validados)
│   │       │   └── response/           # DTOs de salida
│   │       ├── model/                  # Entidades JPA
│   │       ├── repository/             # Interfaces JpaRepository
│   │       ├── service/                # Lógica de negocio
│   │       └── exception/             # Manejo global de excepciones
│   └── pom.xml
│
├── 📂 frontend/                        # Cliente Angular (SPA)
│   ├── src/
│   │   └── app/
│   │       ├── core/                   # Guards, interceptors, servicios core
│   │       ├── modules/
│   │       │   ├── auth/               # Login y registro
│   │       │   ├── paciente/           # Dashboard, citas, perfil
│   │       │   ├── medico/             # Agenda, gestión de citas
│   │       │   ├── administrador/      # Panel completo de admin
│   │       │   └── publico/            # Landing page
│   │       └── shared/                 # Componentes, pipes y directivas globales
│   ├── angular.json
│   └── package.json
│
├── 📂 database/
│   └── clinica_aviva_schema.sql        # DDL completo del esquema MySQL
│
├── .gitignore
├── README.md
├── RESUMEN_PROYECTO.md
└── INSTRUCCIONES_PROYECTO.md
```

---

## 📦 Prerequisitos e Instalación

Asegúrate de tener instalado en tu máquina:

| Herramienta | Versión Mínima | Verificar con |
|---|---|---|
| **Node.js** | 18.x o superior | `node -v` |
| **pnpm** | 8.x o superior | `pnpm -v` |
| **Java JDK** | 17 (LTS) | `java -version` |
| **Maven** | 3.8.x o superior | `mvn -version` |
| **MySQL Server** | 8.0 o superior | `mysql --version` |
| **Angular CLI** | 17.x | `ng version` |

```bash
# Instalar pnpm si no lo tienes
npm install -g pnpm

# Instalar Angular CLI si no lo tienes
npm install -g @angular/cli@17
```

---

## ⚙️ Configuración del Entorno

### 1. Base de Datos

```sql
-- Crear el esquema en MySQL
CREATE DATABASE clinica_aviva CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Ejecutar el script DDL completo
-- Desde MySQL Workbench o la terminal:
mysql -u root -p clinica_aviva < database/clinica_aviva_schema.sql
```

### 2. Backend — Variables de Entorno

> **⚠️ Buena práctica de seguridad**: Las credenciales **no deben vivir en archivos `.properties`** dentro de `src/`, ya que corren el riesgo de empaquetarse en el `.jar` final o quedar en el historial de Git. En su lugar, se inyectan mediante **variables de entorno del sistema operativo**, que Spring Boot resuelve automáticamente gracias a su *Relaxed Binding* (ej. `SPRING_DATASOURCE_URL` → `spring.datasource.url`).

**Paso 1**: Copia el archivo de plantilla y completa tus credenciales locales:

```bash
# Desde la raíz del proyecto
cp .env.example .env
```

**Paso 2**: Edita `.env` con tus valores reales (este archivo está en `.gitignore`, **nunca se sube al repositorio**):

```dotenv
# Conexión a MySQL
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/clinica_aviva?useSSL=false&serverTimezone=America/Lima
SPRING_DATASOURCE_USERNAME=tu_usuario_mysql
SPRING_DATASOURCE_PASSWORD=tu_contraseña_mysql

# JWT — clave de al menos 256 bits (64 caracteres hex)
APP_JWT_SECRET=cambia_esto_por_una_clave_secreta_larga_y_aleatoria
APP_JWT_EXPIRATION=86400000

# Perfil activo de Spring
SPRING_PROFILES_ACTIVE=dev
```

**Paso 3**: Al ejecutar el backend, carga las variables antes de arrancar Maven:

```bash
# Linux / macOS
export $(grep -v '^#' .env | xargs) && mvn spring-boot:run

# Windows PowerShell
Get-Content .env | Where-Object { $_ -notmatch '^#' -and $_ -ne '' } | ForEach-Object { $k,$v = $_ -split '=',2; [System.Environment]::SetEnvironmentVariable($k, $v, 'Process') }
mvn spring-boot:run
```

### 3. Frontend — Variables de Entorno

Edita `frontend/src/environments/environment.ts` según tu configuración local:

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api'
};
```

---

## 🚀 Ejecución del Proyecto

### Backend (Spring Boot)

```bash
cd backend

# Compilar el proyecto
mvn clean install -DskipTests

# Ejecutar el servidor (Puerto 8080) — las variables de entorno deben estar cargadas antes
mvn spring-boot:run
```

El servidor estará disponible en: `http://localhost:8080`

### Frontend (Angular)

```bash
cd frontend

# Instalar dependencias
pnpm install

# Iniciar el servidor de desarrollo (Puerto 4200)
pnpm start
```

La aplicación estará disponible en: `http://localhost:4200`

> **⚠️ Orden de inicio**: Asegúrate de iniciar primero el **servidor MySQL**, luego el **backend** y finalmente el **frontend**.

---

## 🔌 Endpoints de la API REST

La API base es: `http://localhost:8080/api`

### Autenticación (Pública)

| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/auth/login` | Iniciar sesión (devuelve JWT) |
| `POST` | `/auth/register` | Registrar nuevo paciente |

### Protegidos (requieren `Authorization: Bearer <token>`)

| Método | Endpoint | Rol | Descripción |
|---|---|---|---|
| `GET` | `/pacientes/mis-citas` | `PACIENTE` | Historial de citas del usuario |
| `POST` | `/citas/reservar` | `PACIENTE` | Reservar un turno disponible |
| `DELETE` | `/citas/{id}/cancelar` | `PACIENTE` | Cancelar una cita propia |
| `GET` | `/citas/{id}/comprobante` | `PACIENTE` | Descargar comprobante PDF |
| `GET` | `/medicos/agenda` | `MEDICO` | Ver agenda del día |
| `PUT` | `/citas/{id}/estado` | `MEDICO` | Actualizar estado de la cita |
| `GET` | `/medicos` | `ADMIN` | Listar todo el personal médico |
| `POST` | `/medicos` | `ADMIN` | Registrar nuevo médico |
| `GET` | `/reportes/ausentismo` | `ADMIN` | Reporte de ausentismo médico |
| `GET` | `/dashboard` | `ADMIN`, `MEDICO` | KPIs del dashboard |

---

## 🔑 Decisiones de Diseño Clave

### Control de Concurrencia con Bloqueo Pesimista
Para evitar la reserva simultánea del mismo turno por dos pacientes, la lógica de reserva se ejecuta dentro de un *stored procedure* MySQL que utiliza `SELECT ... FOR UPDATE`. Esta instrucción adquiere un bloqueo a nivel de fila dentro de la transacción, garantizando que solo una operación pueda completar la reserva.

### JWT sin Estado (Stateless)
El backend no mantiene sesiones en servidor. Cada request incluye un token JWT firmado con la clave secreta del servidor. El `JwtAuthenticationFilter` valida el token en cada petición, extrae el rol del usuario y configura el `SecurityContext` de Spring.

### Separación estricta DTO ↔ Entidad
Las entidades JPA nunca se exponen directamente al cliente. Las capas de entrada usan *Request DTOs* validados con `@Valid` y las salidas usan *Response DTOs*, evitando la exposición accidental de datos sensibles (como hashes de contraseñas) y desacoplando el modelo de datos interno de la API pública.

### Arquitectura Modular en Angular
Cada rol de usuario tiene su propio módulo Angular con *lazy loading* (`loadChildren`), lo que reduce el bundle inicial y mejora los tiempos de carga. Los módulos comparten componentes a través del `SharedModule`.

---

<div align="center">

**Proyecto académico desarrollado para la carrera de Ingeniería de Sistemas e Informática**

</div>
