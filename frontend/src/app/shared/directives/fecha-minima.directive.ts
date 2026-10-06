import { Directive, HostListener, Input, OnInit } from '@angular/core';
import { NgControl } from '@angular/forms';

@Directive({
  selector: '[fechaMinima]'
})
export class FechaMinimaDirective implements OnInit {
  @Input() fechaMinima = '';
  constructor(private control: NgControl) {}

  ngOnInit(): void {
    const hoy = new Date().toISOString().split('T')[0];
    const min = this.fechaMinima || hoy;
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
