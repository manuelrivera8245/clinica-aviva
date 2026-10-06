import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { PacienteRoutingModule } from './paciente-routing.module';
import { DashboardComponent }    from './pages/dashboard/dashboard.component';
import { ReservarCitaComponent } from './pages/reservar-cita/reservar-cita.component';
import { MisCitasComponent }     from './pages/mis-citas/mis-citas.component';
import { HistorialComponent }    from './pages/historial/historial.component';
import { PerfilComponent }       from './pages/perfil/perfil.component';

@NgModule({
  declarations: [
    DashboardComponent,
    ReservarCitaComponent,
    MisCitasComponent,
    HistorialComponent,
    PerfilComponent
  ],
  imports: [SharedModule, PacienteRoutingModule]
})
export class PacienteModule { }
