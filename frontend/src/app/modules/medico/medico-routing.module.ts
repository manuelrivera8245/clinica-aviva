import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardMedicoComponent }    from './pages/dashboard/dashboard.component';
import { AgendaDiaComponent }          from './pages/agenda-dia/agenda-dia.component';
import { GestionarCitaComponent }      from './pages/gestionar-cita/gestionar-cita.component';
import { HistorialMedicoComponent }    from './pages/historial-medico/historial-medico.component';
import { PerfilMedicoComponent }       from './pages/perfil-medico/perfil-medico.component';

const routes: Routes = [
  { path: 'dashboard', component: DashboardMedicoComponent },
  { path: 'agenda',    component: AgendaDiaComponent },
  { path: 'gestionar', component: GestionarCitaComponent },
  { path: 'historial', component: HistorialMedicoComponent },
  { path: 'perfil',    component: PerfilMedicoComponent },
  { path: '',          redirectTo: 'dashboard', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MedicoRoutingModule { }
