import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardAdminComponent }     from './pages/dashboard/dashboard.component';
import { GestionMedicosComponent }     from './pages/gestion-medicos/gestion-medicos.component';
import { GestionTurnosComponent }      from './pages/gestion-turnos/gestion-turnos.component';
import { GestionPacientesComponent }   from './pages/gestion-pacientes/gestion-pacientes.component';
import { ReportesComponent }           from './pages/reportes/reportes.component';
import { ConfiguracionComponent }      from './pages/configuracion/configuracion.component';
import { NuevaCitaComponent }          from './pages/nueva-cita/nueva-cita.component';
import { GestionEspecialidadesComponent } from './pages/gestion-especialidades/gestion-especialidades.component';
import { GestionHorariosComponent }     from './pages/gestion-horarios/gestion-horarios.component';

const routes: Routes = [
  { path: 'dashboard',  component: DashboardAdminComponent },
  { path: 'medicos',    component: GestionMedicosComponent },
  { path: 'turnos',     component: GestionTurnosComponent },
  { path: 'pacientes',  component: GestionPacientesComponent },
  { path: 'pacientes/nueva-cita/:id', component: NuevaCitaComponent },
  { path: 'especialidades', component: GestionEspecialidadesComponent },
  { path: 'horarios',      component: GestionHorariosComponent },
  { path: 'reportes',   component: ReportesComponent },
  { path: 'configuracion', component: ConfiguracionComponent },
  { path: '',           redirectTo: 'dashboard', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
