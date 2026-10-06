import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { PublicRoutingModule } from './public-routing.module';
import { InicioComponent }        from './pages/inicio/inicio.component';
import { NosotrosComponent }      from './pages/nosotros/nosotros.component';
import { EspecialidadesComponent } from './pages/especialidades/especialidades.component';

@NgModule({
  declarations: [
    InicioComponent,
    NosotrosComponent,
    EspecialidadesComponent
  ],
  imports: [
    SharedModule,
    PublicRoutingModule
  ]
})
export class PublicModule { }
