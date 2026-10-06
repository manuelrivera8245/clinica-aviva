import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-confirm-modal',
  templateUrl: './confirm-modal.component.html',
  styleUrls: ['./confirm-modal.component.css']
})
export class ConfirmModalComponent {
  @Input() titulo   = 'Confirmar acción';
  @Input() mensaje  = '¿Está seguro de realizar esta acción?';
  @Input() textoSi  = 'Confirmar';
  @Input() textoNo  = 'Cancelar';
  @Input() tipo: 'peligro' | 'advertencia' | 'primario' = 'peligro';
  @Input() visible  = false;

  @Output() confirmado = new EventEmitter<boolean>();

  confirmar(): void {
    this.confirmado.emit(true);
    this.visible = false;
  }

  cancelar(): void {
    this.confirmado.emit(false);
    this.visible = false;
  }
}
