import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './core/guards/auth.guard';
import { PacienteGuard } from './core/guards/paciente.guard';
import { MedicoGuard } from './core/guards/medico.guard';
import { AdminGuard } from './core/guards/admin.guard';

const routes: Routes = [
  { path: '', redirectTo: '/inicio', pathMatch: 'full' },

  {
    path: '',
    loadChildren: () =>
      import('./modules/publico/public.module').then(m => m.PublicModule)
  },

  {
    path: 'auth',
    loadChildren: () =>
      import('./modules/auth/auth.module').then(m => m.AuthModule)
  },

  {
    path: 'paciente',
    loadChildren: () =>
      import('./modules/paciente/paciente.module').then(m => m.PacienteModule),
    canActivate: [AuthGuard, PacienteGuard]
  },

  {
    path: 'medico',
    loadChildren: () =>
      import('./modules/medico/medico.module').then(m => m.MedicoModule),
    canActivate: [AuthGuard, MedicoGuard]
  },

  {
    path: 'admin',
    loadChildren: () =>
      import('./modules/administrador/admin.module').then(m => m.AdminModule),
    canActivate: [AuthGuard, AdminGuard]
  },

  { path: '**', redirectTo: '/inicio' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { scrollPositionRestoration: 'top' })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
