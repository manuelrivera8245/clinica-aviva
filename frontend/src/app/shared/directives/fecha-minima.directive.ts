import { Directive, HostListener, Input, OnInit } from '@angular/core';
import { NgControl } from '@angular/forms';

/**
 * Directiva fechaMinima.
 * Valida que la fecha ingresada en un campo no sea anterior a hoy.
 * Uso: <input type="date" fechaMinima [formControl]="...">
 */
@Directive({
  selector: '[fechaMinima]'
})
export class FechaMinimaDirective implements OnInit {
  @Input() fechaMinima = '';   // Permite sobrescribir la fecha mínima si se necesita

  constructor(private control: NgControl) {}

  ngOnInit(): void {
    // Establecer el atributo min en el input nativo
    const hoy = new Date().toISOString().split('T')[0];
    const min = this.fechaMinima || hoy;
    // Se aplica en la vista mediante el setValidators del control
    if (this.control?.control) {
      this.control.control.addValidators(ctrl => {
        const valor = ctrl.value;
        if (!valor) return null;
        return valor >= min ? null : { fechaMinima: { min, actual: valor } };
      });
      this.control.control.updateValueAndValidity();
    }
  }

  @HostListener('change', ['$event.target'])
  onChange(input: HTMLInputElement): void {
    const hoy = new Date().toISOString().split('T')[0];
    const min = this.fechaMinima || hoy;
    if (input.value && input.value < min) {
      input.value = min;
      this.control?.control?.setValue(min, { emitEvent: true });
    }
  }
}
