import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { MedicoRoutingModule } from './medico-routing.module';
import { DashboardMedicoComponent }  from './pages/dashboard/dashboard.component';
import { AgendaDiaComponent }        from './pages/agenda-dia/agenda-dia.component';
import { GestionarCitaComponent }    from './pages/gestionar-cita/gestionar-cita.component';
import { HistorialMedicoComponent }  from './pages/historial-medico/historial-medico.component';
import { PerfilMedicoComponent }     from './pages/perfil-medico/perfil-medico.component';

@NgModule({
  declarations: [
    DashboardMedicoComponent,
    AgendaDiaComponent,
    GestionarCitaComponent,
    HistorialMedicoComponent,
    PerfilMedicoComponent
  ],
  imports: [SharedModule, MedicoRoutingModule]
})
export class MedicoModule { }
