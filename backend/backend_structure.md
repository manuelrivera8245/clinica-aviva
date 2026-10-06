# CLINICA AVIVA - Estructura de Carpetas Backend (Spring Boot)

## Arquitectura: Capas (Layered Architecture) + Domain-Driven Design

```
clinica-aviva-backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── clinicaaviva/
│   │   │           └── ClinicaAvivaApplication.java
│   │   │
│   │   ├── resources/
│   │   │   ├── application.yml              # Configuracion principal
│   │   │   ├── application-dev.yml          # Perfil desarrollo
│   │   │   ├── application-prod.yml         # Perfil produccion
│   │   │   └── db/
│   │   │       └── data.sql                 # Datos semilla (opcional)
│   │   │
│   │   └── webapp/                          # (Si se usa JSP - no recomendado con Angular)
│   │
│   └── test/
│       └── java/
│           └── com/
│               └── clinicaaviva/
│                   ├── ClinicaAvivaApplicationTests.java
│                   ├── integration/           # Tests de integracion
│                   └── unit/                  # Tests unitarios
│
├── pom.xml                                  # Dependencias Maven
├── mvnw / mvnw.cmd                          # Maven Wrapper
└── README.md
```

## Estructura de Paquetes Java (src/main/java/com/clinicaaviva/)

```
com.clinicaaviva/
│
├── ClinicaAvivaApplication.java              # Clase principal Spring Boot
│
├── config/                                   # CONFIGURACIONES GLOBALES
│   ├── SecurityConfig.java                   # Spring Security + BCrypt + Roles
│   ├── JwtConfig.java                        # Configuracion JWT (si se usa token)
│   ├── CorsConfig.java                       # Configuracion CORS para Angular
│   ├── DatabaseConfig.java                   # Configuracion DataSource
│   └── AuditConfig.java                      # Auditoria JPA
│
├── security/                                 # CAPA DE SEGURIDAD
│   ├── jwt/
│   │   ├── JwtTokenProvider.java
│   │   ├── JwtAuthenticationFilter.java
│   │   └── JwtAuthenticationEntryPoint.java
│   ├── UserDetailsServiceImpl.java           # Servicio de detalles de usuario
│   ├── UserPrincipal.java                    # Wrapper de autenticacion
│   └── CustomAuthenticationProvider.java
│
├── controller/                               # CAPA REST CONTROLLERS
│   ├── AuthController.java                   # Login / Registro
│   ├── PacienteController.java               # CRUD Pacientes + Perfil
│   ├── MedicoController.java                 # CRUD Medicos
│   ├── AdministradorController.java          # CRUD Administradores
│   ├── EspecialidadController.java           # CRUD Especialidades
│   ├── HorarioMedicoController.java          # Configuracion horarios
│   ├── TurnoController.java                  # Gestion de turnos
│   ├── CitaController.java                   # Reserva / Cancelacion / Historial
│   ├── DashboardController.java              # KPIs y reportes
│   ├── NotificacionController.java           # Cola de notificaciones
│   └── ConfiguracionGeneralController.java   # GET publico + PUT admin (config clinica)
│
├── dto/                                      # OBJETOS DE TRANSFERENCIA
│   ├── request/
│   │   ├── LoginRequest.java
│   │   ├── RegistroPacienteRequest.java
│   │   ├── ReservaCitaRequest.java
│   │   ├── CancelacionCitaRequest.java
│   │   ├── HorarioMedicoRequest.java
│   │   └── MedicoRequest.java
│   ├── response/
│   │   ├── ApiResponse.java                  # Wrapper generico de respuesta
│   │   ├── JwtAuthenticationResponse.java
│   │   ├── PacienteResponse.java
│   │   ├── MedicoResponse.java
│   │   ├── CitaResponse.java
│   │   ├── TurnoDisponibleResponse.java
│   │   ├── DashboardResponse.java
│   │   ├── NotificacionResponse.java
│   │   └── ConfiguracionGeneralResponse.java # Info publica de la clinica
│   └── mapper/
│       ├── PacienteMapper.java
│       ├── MedicoMapper.java
│       ├── CitaMapper.java
│       └── TurnoMapper.java
│
├── service/                                  # CAPA DE NEGOCIO
│   ├── PacienteService.java                  # Interface
│   ├── MedicoService.java                    # Interface
│   ├── CitaService.java                      # Interface
│   ├── TurnoService.java                     # Interface
│   ├── HorarioMedicoService.java             # Interface
│   ├── EspecialidadService.java              # Interface
│   ├── NotificacionService.java              # Interface
│   ├── AdministradorService.java             # Interface
│   ├── AuthService.java                      # Interface
│   ├── DashboardService.java                 # Interface
│   └── ConfiguracionGeneralService.java      # Interface (configuracion de clinica)
│
├── service/impl/                             # IMPLEMENTACIONES
│   ├── PacienteServiceImpl.java
│   ├── MedicoServiceImpl.java
│   ├── CitaServiceImpl.java                  # Contiene logica de SPs como codigo Java
│   ├── TurnoServiceImpl.java
│   ├── HorarioMedicoServiceImpl.java
│   ├── EspecialidadServiceImpl.java
│   ├── NotificacionServiceImpl.java
│   ├── AdministradorServiceImpl.java
│   ├── AuthServiceImpl.java                  # Autenticacion con BCrypt
│   ├── DashboardServiceImpl.java
│   └── ConfiguracionGeneralServiceImpl.java
│
├── repository/                               # CAPA DE ACCESO A DATOS (JPA)
│   ├── PacienteRepository.java               # JpaRepository + @Query nativos
│   ├── MedicoRepository.java
│   ├── CitaRepository.java
│   ├── TurnoRepository.java
│   ├── HorarioMedicoRepository.java
│   ├── EspecialidadRepository.java
│   ├── NotificacionRepository.java
│   ├── AdministradorRepository.java
│   ├── ConfiguracionGeneralRepository.java   # CRUD configuracion clinica
│   └── dashboard/
│       └── DashboardRepository.java          # Queries complejas para reportes
│
├── entity/                                   # ENTIDADES JPA / HIBERNATE
│   ├── Paciente.java                         # @Entity paciente
│   ├── Medico.java                           # @Entity medico
│   ├── Administrador.java                    # @Entity administrador
│   ├── Especialidad.java                     # @Entity especialidad
│   ├── HorarioMedico.java                    # @Entity horario_medico
│   ├── Turno.java                            # @Entity turno
│   ├── Cita.java                             # @Entity cita
│   ├── Notificacion.java                     # @Entity notificacion
│   └── ConfiguracionGeneral.java             # @Entity configuracion_general
│
├── model/                                    # ENUMS Y TIPOS COMPLEJOS
│   ├── enums/
│   │   ├── EstadoTurno.java                  # LIBRE, OCUPADO, BLOQUEADO
│   │   ├── EstadoCita.java                   # PROGRAMADA, ATENDIDA, NO_ASISTIO, CANCELADA
│   │   ├── TipoNotificacion.java             # CONFIRMACION, RECORDATORIO, CANCELACION
│   │   ├── EstadoEnvio.java                  # PENDIENTE, ENVIADO, FALLIDO
│   │   ├── DiaSemana.java                    # LUNES=1 ... DOMINGO=7
│   │   └── RolUsuario.java                   # PACIENTE, MEDICO, ADMINISTRADOR
│   └── dto/
│       └── (opcional - tipos internos)
│
├── exception/                                # MANEJO GLOBAL DE EXCEPCIONES
│   ├── GlobalExceptionHandler.java           # @ControllerAdvice
│   ├── BusinessException.java                # Excepciones de negocio
│   ├── ResourceNotFoundException.java
│   ├── ConflictException.java                # Concurrencia / duplicados
│   ├── UnauthorizedException.java
│   └── ValidationException.java
│
└── util/                                     # UTILITARIOS
    ├── PasswordEncoderUtil.java              # Wrapper de BCrypt
    ├── FechaUtil.java                        # Manipulacion de fechas
    └── Constants.java                        # Constantes globales
```

## Dependencias Maven (pom.xml) - Resumen

```xml
<!-- Spring Boot Starter Web -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Spring Boot Starter Data JPA -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- Spring Boot Starter Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- MySQL Connector -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- BCrypt (incluido en Spring Security) -->
<!-- No requiere dependencia adicional - usar BCryptPasswordEncoder -->

<!-- Validation -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>

<!-- Lombok (opcional - reduce boilerplate) -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>

<!-- DevTools (desarrollo) -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- Testing -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-test</artifactId>
    <scope>test</scope>
</dependency>
```

## Configuracion de Seguridad (Resumen)

| Rol | Rutas Permitidas |
|-----|-----------------|
| **PACIENTE** | `/api/pacientes/**` (propio), `/api/citas/reservar`, `/api/citas/cancelar`, `/api/citas/mis-citas`, `/api/turnos/disponibles`, `/api/especialidades` |
| **MEDICO** | `/api/medicos/**` (propio), `/api/citas/mis-citas`, `/api/citas/actualizar-estado`, `/api/turnos/**` |
| **ADMINISTRADOR** | `/api/admin/**`, `/api/medicos/**`, `/api/especialidades/**`, `/api/horarios/**`, `/api/dashboard/**`, `/api/notificaciones/**`, `/api/configuracion` (PUT) |
| **PUBLICO** | `/api/auth/**`, `/api/especialidades` (GET), `/api/configuracion` (GET) |

## Notas de Implementacion

1. **Procedimientos Almacenados**: Los SPs (`sp_reservar_cita`, `sp_cancelar_cita`) se implementaran como codigo Java transaccional en `CitaServiceImpl` usando `@Transactional` con `Propagation.REQUIRED` y bloqueo pesimista (`PESSIMISTIC_WRITE`), preservando la logica de negocio exacta del documento.

2. **BCrypt**: Todas las contrasenas se hashean con `BCryptPasswordEncoder` (strength=10) antes de persistir. Nunca se almacena texto plano.

3. **CORS**: Configurado para permitir solicitudes desde `http://localhost:4200` (Angular dev server).
