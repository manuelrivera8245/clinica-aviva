# CLINICA AVIVA - Estructura de Modulos y Componentes Angular

## Arquitectura: Modulos por Funcionalidad (Feature Modules) + Lazy Loading

```
clinica-aviva-frontend/
├── src/
│   ├── app/
│   │   ├── core/                                   # NUCLEO DE LA APLICACION
│   │   │   ├── guards/
│   │   │   │   ├── auth.guard.ts                   # Proteccion de rutas autenticadas
│   │   │   │   ├── paciente.guard.ts               # Solo rol PACIENTE
│   │   │   │   ├── medico.guard.ts                 # Solo rol MEDICO
│   │   │   │   └── admin.guard.ts                  # Solo rol ADMINISTRADOR
│   │   │   ├── interceptors/
│   │   │   │   ├── jwt.interceptor.ts              # Inyeccion token en headers
│   │   │   │   └── error.interceptor.ts            # Manejo global de errores HTTP
│   │   │   ├── models/
│   │   │   │   ├── usuario.model.ts                # Modelo generico de usuario
│   │   │   │   ├── auth-response.model.ts
│   │   │   │   └── api-response.model.ts
│   │   │   ├── services/
│   │   │   │   ├── auth.service.ts                 # Login, logout, registro
│   │   │   │   ├── token.service.ts                # Gestión de JWT en localStorage
│   │   │   │   └── notification.service.ts         # Toast/alertas globales
│   │   │   └── core.module.ts                      # Importado una sola vez en AppModule
│   │   │
│   │   ├── shared/                                 # COMPONENTES COMPARTIDOS
│   │   │   ├── components/
│   │   │   │   ├── navbar/
│   │   │   │   │   ├── navbar.component.ts
│   │   │   │   │   ├── navbar.component.html
│   │   │   │   │   └── navbar.component.css
│   │   │   │   ├── footer/
│   │   │   │   │   ├── footer.component.ts
│   │   │   │   │   ├── footer.component.html
│   │   │   │   │   └── footer.component.css
│   │   │   │   ├── sidebar/
│   │   │   │   │   ├── sidebar.component.ts
│   │   │   │   │   ├── sidebar.component.html
│   │   │   │   │   └── sidebar.component.css
│   │   │   │   ├── loading-spinner/
│   │   │   │   │   ├── loading-spinner.component.ts
│   │   │   │   │   ├── loading-spinner.component.html
│   │   │   │   │   └── loading-spinner.component.css
│   │   │   │   └── confirm-modal/
│   │   │   │       ├── confirm-modal.component.ts
│   │   │   │       ├── confirm-modal.component.html
│   │   │   │       └── confirm-modal.component.css
│   │   │   ├── directives/
│   │   │   │   ├── solo-numeros.directive.ts
│   │   │   │   └── fecha-minima.directive.ts
│   │   │   ├── pipes/
│   │   │   │   ├── dia-semana.pipe.ts              # 1 -> "Lunes"
│   │   │   │   └── estado-cita.pipe.ts             # Formato visual de estados
│   │   │   └── shared.module.ts
│   │   │
│   │   ├── modules/
│   │   │   ├── auth/                                 # MODULO AUTENTICACION
│   │   │   │   ├── auth-routing.module.ts
│   │   │   │   ├── auth.module.ts
│   │   │   │   ├── pages/
│   │   │   │   │   ├── login/
│   │   │   │   │   │   ├── login.component.ts
│   │   │   │   │   │   ├── login.component.html      # Formulario login + Bootstrap
│   │   │   │   │   │   └── login.component.css
│   │   │   │   │   └── registro/
│   │   │   │   │       ├── registro.component.ts
│   │   │   │   │       ├── registro.component.html   # Formulario registro paciente
│   │   │   │   │       └── registro.component.css
│   │   │   │   └── components/
│   │   │   │
│   │   │   ├── paciente/                             # MODULO PACIENTE
│   │   │   │   ├── paciente-routing.module.ts
│   │   │   │   ├── paciente.module.ts
│   │   │   │   ├── services/
│   │   │   │   │   ├── paciente.service.ts           # CRUD paciente
│   │   │   │   │   └── cita-paciente.service.ts      # Reserva/cancelacion
│   │   │   │   ├── models/
│   │   │   │   │   ├── paciente.model.ts
│   │   │   │   │   └── perfil-paciente.model.ts
│   │   │   │   └── pages/
│   │   │   │       ├── dashboard/
│   │   │   │       │   ├── dashboard.component.ts
│   │   │   │       │   ├── dashboard.component.html   # Panel principal paciente
│   │   │   │       │   └── dashboard.component.css
│   │   │   │       ├── reservar-cita/
│   │   │   │       │   ├── reservar-cita.component.ts
│   │   │   │       │   ├── reservar-cita.component.html
│   │   │   │       │   │                               # Paso 1: Seleccionar especialidad
│   │   │   │       │   │                               # Paso 2: Seleccionar medico
│   │   │   │       │   │                               # Paso 3: Seleccionar turno
│   │   │   │       │   │                               # Paso 4: Confirmar
│   │   │   │       │   └── reservar-cita.component.css
│   │   │   │       ├── mis-citas/
│   │   │   │       │   ├── mis-citas.component.ts
│   │   │   │       │   ├── mis-citas.component.html    # Lista de citas + cancelar
│   │   │   │       │   └── mis-citas.component.css
│   │   │   │       ├── historial/
│   │   │   │       │   ├── historial.component.ts
│   │   │   │       │   ├── historial.component.html    # Citas atendidas/canceladas
│   │   │   │       │   └── historial.component.css
│   │   │   │       └── perfil/
│   │   │   │           ├── perfil.component.ts
│   │   │   │           ├── perfil.component.html       # Editar datos personales
│   │   │   │           └── perfil.component.css
│   │   │   │
│   │   │   ├── medico/                                 # MODULO MEDICO
│   │   │   │   ├── medico-routing.module.ts
│   │   │   │   ├── medico.module.ts
│   │   │   │   ├── services/
│   │   │   │   │   ├── medico.service.ts
│   │   │   │   │   └── agenda.service.ts
│   │   │   │   ├── models/
│   │   │   │   │   └── medico.model.ts
│   │   │   │   └── pages/
│   │   │   │       ├── dashboard/
│   │   │   │       │   ├── dashboard.component.ts
│   │   │   │       │   ├── dashboard.component.html    # Resumen del dia
│   │   │   │       │   └── dashboard.component.css
│   │   │   │       ├── agenda-dia/
│   │   │   │       │   ├── agenda-dia.component.ts
│   │   │   │       │   ├── agenda-dia.component.html   # Citas del dia (vista_citas_del_dia)
│   │   │   │       │   └── agenda-dia.component.css
│   │   │   │       ├── gestionar-cita/
│   │   │   │       │   ├── gestionar-cita.component.ts
│   │   │   │       │   ├── gestionar-cita.component.html
│   │   │   │       │   │                               # Cambiar estado: Atendido/No Asistio
│   │   │   │       │   └── gestionar-cita.component.css
│   │   │   │       └── historial-medico/
│   │   │   │           ├── historial-medico.component.ts
│   │   │   │           ├── historial-medico.component.html
│   │   │   │           └── historial-medico.component.css
│   │   │   │
│   │   │   ├── administrador/                          # MODULO ADMINISTRADOR
│   │   │   │   ├── admin-routing.module.ts
│   │   │   │   ├── admin.module.ts
│   │   │   │   ├── services/
│   │   │   │   │   ├── admin.service.ts
│   │   │   │   │   ├── medico-admin.service.ts
│   │   │   │   │   ├── especialidad.service.ts
│   │   │   │   │   ├── horario-admin.service.ts
│   │   │   │   │   ├── turno-admin.service.ts
│   │   │   │   │   ├── dashboard-admin.service.ts
│   │   │   │   │   └── configuracion.service.ts      # GET publico + PUT admin
│   │   │   │   ├── models/
│   │   │   │   │   ├── administrador.model.ts
│   │   │   │   │   ├── especialidad.model.ts
│   │   │   │   │   ├── horario-medico.model.ts
│   │   │   │   │   ├── dashboard-kpi.model.ts
│   │   │   │   │   └── configuracion-general.model.ts # Info clinica
│   │   │   │   └── pages/
│   │   │   │       ├── dashboard/
│   │   │   │       │   ├── dashboard.component.ts
│   │   │   │       │   ├── dashboard.component.html    # KPIs + graficos (vista_dashboard_admin)
│   │   │   │       │   └── dashboard.component.css
│   │   │   │       ├── gestion-medicos/
│   │   │   │       │   ├── gestion-medicos.component.ts
│   │   │   │       │   ├── gestion-medicos.component.html
│   │   │   │       │   │                               # Lista + Crear/Editar medico
│   │   │   │       │   └── gestion-medicos.component.css
│   │   │   │       ├── gestion-especialidades/
│   │   │   │       │   ├── gestion-especialidades.component.ts
│   │   │   │       │   ├── gestion-especialidades.component.html
│   │   │   │       │   └── gestion-especialidades.component.css
│   │   │   │       ├── gestion-horarios/
│   │   │   │       │   ├── gestion-horarios.component.ts
│   │   │   │       │   ├── gestion-horarios.component.html
│   │   │   │       │   │                               # Configurar dias/horas medico
│   │   │   │       │   └── gestion-horarios.component.css
│   │   │   │       ├── monitor-citas/
│   │   │   │       │   ├── monitor-citas.component.ts
│   │   │   │       │   ├── monitor-citas.component.html
│   │   │   │       │   │                               # Tabla en tiempo real de citas
│   │   │   │       │   └── monitor-citas.component.css
│   │   │   │       ├── reportes/
│   │   │   │       │   ├── reportes.component.ts
│   │   │   │       │   ├── reportes.component.html     # Reportes de ausentismo
│   │   │   │       │   └── reportes.component.css
│   │   │   │       └── configuracion/
│   │   │   │           ├── configuracion.component.ts
│   │   │   │           ├── configuracion.component.html # Formulario editable info clinica
│   │   │   │           └── configuracion.component.css
│   │   │   │
│   │   │   └── publico/                                # MODULO PUBLICO (Landing)
│   │   │       ├── public-routing.module.ts
│   │   │       ├── public.module.ts
│   │   │       └── pages/
│   │   │           ├── inicio/
│   │   │           │   ├── inicio.component.ts
│   │   │           │   ├── inicio.component.html       # Landing page de la clinica
│   │   │           │   └── inicio.component.css
│   │   │           ├── especialidades/
│   │   │           │   ├── especialidades.component.ts
│   │   │           │   ├── especialidades.component.html
│   │   │           │   └── especialidades.component.css
│   │   │           └── nosotros/
│   │   │               ├── nosotros.component.ts
│   │   │               ├── nosotros.component.html
│   │   │               └── nosotros.component.css
│   │   │
│   │   ├── app-routing.module.ts                     # Rutas principales + lazy loading
│   │   ├── app.component.ts                          # Componente raiz
│   │   ├── app.component.html
│   │   ├── app.component.css
│   │   └── app.module.ts
│   │
│   ├── environments/
│   │   ├── environment.ts                          # Desarrollo (API: localhost:8080)
│   │   └── environment.prod.ts                     # Produccion
│   │
│   ├── assets/
│   │   ├── images/                                 # Logos, iconos, fondos
│   │   │   ├── logo-clinica.png
│   │   │   ├── medico-default.png
│   │   │   └── paciente-default.png
│   │   └── data/                                   # JSONs estaticos (si aplica)
│   │
│   ├── index.html
│   ├── main.ts
│   ├── styles.css                                  # Estilos globales + Bootstrap imports
│   └── polyfills.ts
│
├── angular.json
├── package.json
├── tsconfig.json
├── tsconfig.app.json
└── README.md
```

## Estrategia de Lazy Loading (Rutas)

```typescript
// app-routing.module.ts
const routes: Routes = [
  { path: '', redirectTo: '/inicio', pathMatch: 'full' },
  
  // Modulo Publico (sin autenticacion)
  { 
    path: '', 
    loadChildren: () => import('./modules/publico/public.module')
      .then(m => m.PublicModule) 
  },
  
  // Modulo Auth (login/registro)
  { 
    path: 'auth', 
    loadChildren: () => import('./modules/auth/auth.module')
      .then(m => m.AuthModule) 
  },
  
  // Modulo Paciente (rol PACIENTE requerido)
  { 
    path: 'paciente', 
    loadChildren: () => import('./modules/paciente/paciente.module')
      .then(m => m.PacienteModule),
    canActivate: [AuthGuard, PacienteGuard]
  },
  
  // Modulo Medico (rol MEDICO requerido)
  { 
    path: 'medico', 
    loadChildren: () => import('./modules/medico/medico.module')
      .then(m => m.MedicoModule),
    canActivate: [AuthGuard, MedicoGuard]
  },
  
  // Modulo Administrador (rol ADMIN requerido)
  { 
    path: 'admin', 
    loadChildren: () => import('./modules/administrador/admin.module')
      .then(m => m.AdminModule),
    canActivate: [AuthGuard, AdminGuard]
  },
  
  { path: '**', redirectTo: '/inicio' }
];
```

## Dependencias npm (package.json) - Resumen

```json
{
  "dependencies": {
    "@angular/animations": "^17.x",
    "@angular/common": "^17.x",
    "@angular/compiler": "^17.x",
    "@angular/core": "^17.x",
    "@angular/forms": "^17.x",
    "@angular/platform-browser": "^17.x",
    "@angular/platform-browser-dynamic": "^17.x",
    "@angular/router": "^17.x",
    "bootstrap": "^5.3.x",                // Framework CSS responsivo
    "@ng-bootstrap/ng-bootstrap": "^16.x", // Componentes Angular para Bootstrap
    "@fortawesome/fontawesome-free": "^6.x", // Iconos
    "rxjs": "^7.x",                        // Programacion reactiva
    "tslib": "^2.x",
    "zone.js": "^0.14.x"
  },
  "devDependencies": {
    "@angular-devkit/build-angular": "^17.x",
    "@angular/cli": "^17.x",
    "@angular/compiler-cli": "^17.x",
    "typescript": "^5.x"
  }
}
```

## Integracion Bootstrap (styles.css)

```css
/* Importacion de Bootstrap */
@import '~bootstrap/dist/css/bootstrap.min.css';
@import '~@fortawesome/fontawesome-free/css/all.min.css';

/* Variables de tema Clinica Aviva */
:root {
  --color-primario: #0077B6;      /* Azul salud */
  --color-secundario: #00B4D8;    /* Turquesa */
  --color-exito: #2ECC71;         /* Verde confirmacion */
  --color-peligro: #E74C3C;       /* Rojo cancelacion */
  --color-advertencia: #F39C12;   /* Naranja alerta */
  --color-fondo: #F8F9FA;         /* Gris claro fondo */
  --color-texto: #2C3E50;         /* Texto principal */
}

body {
  font-family: 'Segoe UI', Roboto, 'Helvetica Neue', sans-serif;
  background-color: var(--color-fondo);
  color: var(--color-texto);
}
```

## Flujo de Navegacion por Actor

```
+-----------+        +------------+        +------------------+
|  PUBLICO  |        |  PACIENTE  |        |    MEDICO        |
+-----------+        +------------+        +------------------+
| /inicio   |        | /paciente/ |        | /medico/dashboard|
| /especiali|        |   dashboard|        | /medico/agenda   |
| /nosotros |        | /paciente/ |        | /medico/gestionar|
| /auth/    |        |   reservar |        | /medico/historial|
|   login   |        | /paciente/ |        +------------------+
| /auth/    |        |   mis-citas|
|   registro|        | /paciente/ |
+-----------+        |   historial|
                     | /paciente/ |
                     |   perfil   |
                     +------------+
                     
+-------------------+
|  ADMINISTRADOR    |
+-------------------+
| /admin/dashboard  |
| /admin/medicos    |
| /admin/especialid.|
| /admin/horarios   |
| /admin/monitor    |
| /admin/reportes   |
| /admin/configurac.|
+-------------------+
```

## Notas de Implementacion

1. **Responsive Design**: Todos los componentes usan clases Bootstrap (`container`, `row`, `col-md-*`, `d-flex`, etc.) para garantizar visualizacion correcta en movil y escritorio.

2. **Interceptores HTTP**: El `JwtInterceptor` inyecta automaticamente el token en cada peticion. El `ErrorInterceptor` maneja 401 (redirige a login), 403 (sin permisos), y errores de servidor.

3. **Guards**: Los guards verifican el rol del usuario decodificando el JWT antes de permitir acceso a rutas protegidas.

4. **Servicios**: Cada modulo tiene sus propios servicios que comunican con los endpoints REST del backend Spring Boot.
