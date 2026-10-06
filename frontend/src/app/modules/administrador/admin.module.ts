import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { AdminRoutingModule } from './admin-routing.module';
import { DashboardAdminComponent }     from './pages/dashboard/dashboard.component';
import { GestionMedicosComponent }     from './pages/gestion-medicos/gestion-medicos.component';
import { GestionTurnosComponent }      from './pages/gestion-turnos/gestion-turnos.component';
import { GestionPacientesComponent }   from './pages/gestion-pacientes/gestion-pacientes.component';
import { ReportesComponent }           from './pages/reportes/reportes.component';
import { ConfiguracionComponent }      from './pages/configuracion/configuracion.component';
import { NuevaCitaComponent }          from './pages/nueva-cita/nueva-cita.component';
import { GestionEspecialidadesComponent } from './pages/gestion-especialidades/gestion-especialidades.component';
import { GestionHorariosComponent }     from './pages/gestion-horarios/gestion-horarios.component';

@NgModule({
  declarations: [
    DashboardAdminComponent,
    GestionMedicosComponent,
    GestionTurnosComponent,
    GestionPacientesComponent,
    ReportesComponent,
    ConfiguracionComponent,
    NuevaCitaComponent,
    GestionEspecialidadesComponent,
    GestionHorariosComponent
  ],
  imports: [SharedModule, AdminRoutingModule]
})
export class AdminModule { }
