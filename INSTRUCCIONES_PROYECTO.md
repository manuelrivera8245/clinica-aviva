# Guía de Instalación y Configuración - Proyecto Clínica Aviva

Esta guía describe todo lo necesario para levantar el proyecto en una nueva computadora después de descomprimir el archivo `.zip`. El proyecto está compuesto por un backend en Spring Boot (Java), un frontend en Angular y una base de datos MySQL.

## Prerrequisitos

Asegúrate de tener instaladas las siguientes herramientas en tu sistema:

### 1. Entorno Backend (Spring Boot)
- **Java Development Kit (JDK)**: Se recomienda tener instalada la versión de Java JDK 17 o superior.
- **Maven**: Gestor de dependencias para Java. Puedes instalarlo globalmente o usar el que viene integrado en el IDE.

### 2. Entorno Frontend (Angular)
- **Node.js**: Descarga e instala la versión LTS recomendada (v18 o superior).
- **pnpm**: El proyecto en el frontend utiliza `pnpm` como gestor de paquetes en lugar de `npm`. 
  - Para instalarlo, abre una terminal y ejecuta: 
    ```bash
    npm install -g pnpm
    ```
- **Angular CLI** (Opcional pero recomendado): 
  - Para instalarlo globalmente: `npm install -g @angular/cli`

### 3. Base de Datos
- **MySQL**: Necesitas tener un servidor MySQL instalado y en ejecución en tu máquina (puerto por defecto 3306).
- **Credenciales del proyecto**:
  - El proyecto está configurado por defecto para apuntar al puerto `3306` con el usuario `root` y la contraseña `8245`.
  - *Nota: Si tu contraseña o usuario de MySQL es diferente, tendrás que modificar el archivo de configuración del backend ubicado en: `backend/src/main/resources/application.yml`.*

---

## Pasos para Inicializar y Ejecutar el Proyecto

### Paso 1: Inicializar la Base de Datos
1. Abre tu cliente de MySQL preferido (por ejemplo, MySQL Workbench, DBeaver, o la consola de MySQL).
2. Crea una base de datos (schema) llamada `clinica_aviva`.
3. Ejecuta el script de inicialización que viene incluido en el proyecto para crear las tablas y poblar datos iniciales. El archivo se encuentra en:
   - `database/clinica_aviva_schema.sql`

### Paso 2: Levantar el Backend (Java / Spring Boot)
1. Abre una terminal y navega hacia la carpeta del backend:
   ```bash
   cd backend
   ```
2. Descarga las dependencias y compila el proyecto usando Maven:
   ```bash
   mvn clean install
   ```
3. Inicia el servidor de Spring Boot:
   ```bash
   mvn spring-boot:run
   ```
   *El backend se iniciará y estará escuchando peticiones en `http://localhost:8080`.*

### Paso 3: Levantar el Frontend (Angular)
1. Abre una **nueva** ventana de terminal y navega hacia la carpeta del frontend:
   ```bash
   cd frontend
   ```
2. Instala las dependencias del proyecto usando `pnpm`:
   ```bash
   pnpm install
   ```
3. Inicia el servidor de desarrollo de Angular:
   ```bash
   pnpm start
   ```
   *(También puedes usar `ng serve` si tienes Angular CLI).*
   *El frontend compilará y estará disponible en el navegador en `http://localhost:4200`.*

---

## Consejos para Antigravity IDE

Dado que ambos trabajan con **Antigravity IDE**, el flujo de trabajo es muy sencillo:
1. Abre la carpeta principal del proyecto (`clinica-aviva`) directamente en el IDE.
2. Abre dos terminales integradas en el IDE.
3. En la primera terminal, navega a `backend` y ejecuta el comando de Spring Boot (`mvn spring-boot:run`).
4. En la segunda terminal, navega a `frontend` y ejecuta el comando de Angular (`pnpm start`).
5. ¡Listo! Puedes editar el código de ambas partes sin salir del IDE.
