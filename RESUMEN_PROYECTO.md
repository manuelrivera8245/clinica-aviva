# Proyecto: Sistema de Gestión de Citas Médicas - Clínica Aviva

## 1. Descripción Breve del Proyecto
El proyecto de la **Clínica Aviva** es un **Sistema Web Integral para la Gestión y Reserva de Citas Médicas**. 

**¿Qué problema resuelve?**
Antes del sistema, la clínica operaba con procesos manuales (cuadernos, hojas de Excel) y dependía de una central telefónica saturada, lo que generaba fricciones administrativas, cuellos de botella y tiempos de espera prolongados. Además, la falta de seguimiento y recordatorios automáticos producía una alta tasa de ausentismo (30% - 35%) y tiempos muertos para los médicos.

**Solución aportada:**
El sistema digitaliza todo el ciclo de vida de una cita médica, permitiendo la **autogestión digital asincrónica (24/7)** por parte de los pacientes. Centraliza la base de datos para evitar pérdida de información, automatiza el bloqueo de horarios concurrentes y envía notificaciones automáticas. Esto optimiza la capacidad instalada de la clínica, reduce las inasistencias y mejora notablemente la experiencia del paciente y la eficiencia operativa.

---

## 2. Arquitectura y Stack Tecnológico

El proyecto se basa en una **Arquitectura Cliente-Servidor** separada, empleando un diseño de **N-Capas** (Controladores, Servicios y Repositorios) en el lado del servidor, y comunicándose a través de una API RESTful que serializa los datos en JSON.

### Frontend (Capa de Presentación)
Desarrollado como una *Single Page Application (SPA)*.
*   **Framework Principal:** Angular v17.3.0
*   **Lenguaje:** TypeScript v5.4.5
*   **Estilos y UI:** Bootstrap v5.3.3, `@ng-bootstrap/ng-bootstrap`, CSS nativo, FontAwesome (v6.5.1).
*   **Gestión de estado y reactividad:** RxJS v7.8.0
*   **Gestor de Paquetes:** pnpm (Node.js)

### Backend (Capa de Lógica de Negocio)
Proporciona una API REST segura utilizando Inyección de Dependencias (IoC) y el patrón DTO para la transferencia de datos.
*   **Lenguaje:** Java 17
*   **Framework Principal:** Spring Boot v3.2.5
*   **Seguridad y Autenticación:** Spring Security con **JWT (JSON Web Tokens)** (JJWT v0.12.5) e intercepción de rutas. Contraseñas encriptadas mediante hash (Bcrypt).
*   **Persistencia de Datos (ORM):** Spring Data JPA implementando Hibernate.
*   **Otras utilidades:** Lombok (reducción de *boilerplate*), LibrePDF / OpenPDF v1.3.37 (generación de comprobantes digitales), y Manejo Global de Excepciones (`@ControllerAdvice`).
*   **Gestor de Dependencias:** Maven

### Base de Datos (Capa de Datos)
Repositorio centralizado con alta integridad de datos.
*   **Motor de Base de Datos:** MySQL 8.0+
*   **Características adicionales:** Diseño relacional, restricciones de integridad, control de concurrencia mediante procedimientos almacenados (*stored procedures* con bloqueos pesimistas `FOR UPDATE`) y el Pool de Conexiones HikariCP gestionado por Spring Boot.

---

## 3. Tareas Principales (Funcionalidades del Sistema)

El sistema está dividido por perfiles de usuario, con acceso protegido y cifrado (JWT):

### Módulo de Pacientes (Autogestión)
*   **Registro y Autenticación:** Creación de cuenta de usuario con validación de credenciales (correo, DNI) y acceso protegido.
*   **Búsqueda en Tiempo Real:** Filtrado dinámico de la disponibilidad médica por **especialidad**, **nombre del doctor** y **fecha específica**.
*   **Gestión de Reservas:** Selección y confirmación de turnos. El sistema bloquea automáticamente el cupo en el calendario para prevenir concurrencias.
*   **Historial de Citas:** Panel o *dashboard* donde el paciente visualiza sus citas vigentes e históricas, y cuenta con la opción de cancelar reservas (con 24h de antelación).
*   **Comprobantes:** Generación y descarga automática de comprobantes de reserva de citas en formato **PDF**.

### Módulo de Personal Médico
*   **Autenticación Institucional:** Ingreso seguro al sistema con credenciales internas.
*   **Agenda Diaria:** Visualización de su calendario de turnos asignados y pacientes programados.
*   **Gestión de Estado de Citas:** Capacidad de actualizar el resultado de la cita a "Atendido" o "No Asistió".

### Módulo de Administrador
*   **Gestión del Personal:** Registro, actualización y baja lógica de médicos y asignación de sus especialidades.
*   **Configuración de Horarios:** Parametrización de la disponibilidad médica (días de atención, rangos horarios y duración de turnos en minutos).
*   **Monitor en Tiempo Real:** Visualización y control global de todas las citas programadas en la institución.

### Funcionalidades Automatizadas del Sistema (Background)
*   **Sistema de Notificaciones:** Generación automática de comprobantes de confirmación y disparo de notificaciones (recordatorios o cancelaciones) para mitigar las tasas de ausentismo médico.
