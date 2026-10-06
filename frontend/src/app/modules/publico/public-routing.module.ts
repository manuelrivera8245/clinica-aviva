import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { InicioComponent }        from './pages/inicio/inicio.component';
import { NosotrosComponent }      from './pages/nosotros/nosotros.component';
import { EspecialidadesComponent } from './pages/especialidades/especialidades.component';

const routes: Routes = [
  { path: 'inicio',        component: InicioComponent },
  { path: 'nosotros',      component: NosotrosComponent },
  { path: 'especialidades', component: EspecialidadesComponent },
  { path: '',              redirectTo: 'inicio', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PublicRoutingModule { }
