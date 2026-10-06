import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

// Componentes
import { NavbarComponent }          from './components/navbar/navbar.component';
import { FooterComponent }          from './components/footer/footer.component';
import { SidebarComponent }         from './components/sidebar/sidebar.component';
import { LoadingSpinnerComponent }  from './components/loading-spinner/loading-spinner.component';
import { ConfirmModalComponent }    from './components/confirm-modal/confirm-modal.component';

// Directivas
import { SoloNumerosDirective }  from './directives/solo-numeros.directive';
import { FechaMinimaDirective }  from './directives/fecha-minima.directive';

// Pipes
import { DiaSemanaPipe }   from './pipes/dia-semana.pipe';
import { EstadoCitaPipe }  from './pipes/estado-cita.pipe';

const COMPONENTS = [
  NavbarComponent,
  FooterComponent,
  SidebarComponent,
  LoadingSpinnerComponent,
  ConfirmModalComponent
];

const DIRECTIVES = [
  SoloNumerosDirective,
  FechaMinimaDirective
];

const PIPES = [
  DiaSemanaPipe,
  EstadoCitaPipe
];

/**
 * SharedModule — Módulo compartido.
 * Exporta componentes, directivas y pipes reutilizables.
 * Se importa en cada feature module que los necesite.
 */
@NgModule({
  declarations: [
    ...COMPONENTS,
    ...DIRECTIVES,
    ...PIPES
  ],
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule,
    FormsModule
  ],
  exports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule,
    FormsModule,
    ...COMPONENTS,
    ...DIRECTIVES,
    ...PIPES
  ]
})
export class SharedModule { }
