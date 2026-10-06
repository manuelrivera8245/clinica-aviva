import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent }    from './pages/dashboard/dashboard.component';
import { ReservarCitaComponent } from './pages/reservar-cita/reservar-cita.component';
import { MisCitasComponent }     from './pages/mis-citas/mis-citas.component';
import { HistorialComponent }    from './pages/historial/historial.component';
import { PerfilComponent }       from './pages/perfil/perfil.component';

const routes: Routes = [
  { path: 'dashboard', component: DashboardComponent },
  { path: 'reservar',  component: ReservarCitaComponent },
  { path: 'mis-citas', component: MisCitasComponent },
  { path: 'historial', component: HistorialComponent },
  { path: 'perfil',    component: PerfilComponent },
  { path: '',          redirectTo: 'dashboard', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PacienteRoutingModule { }
