import { Directive, HostListener } from '@angular/core';

// Directiva soloNumeros
@Directive({
  selector: '[soloNumeros]'
})
export class SoloNumerosDirective {

  @HostListener('keydown', ['$event'])
  onKeyDown(event: KeyboardEvent): void {
    // Permitir teclas de control
    const teclasTControl = [
      'Backspace', 'Delete', 'Tab', 'Escape', 'Enter',
      'ArrowLeft', 'ArrowRight', 'ArrowUp', 'ArrowDown',
      'Home', 'End'
    ];

    if (teclasTControl.includes(event.key)) {
      return;
    }

    // Permitir atajos
    if (event.ctrlKey && ['a', 'c', 'v', 'x'].includes(event.key.toLowerCase())) {
      return;
    }

    // Permitir digitos
    if (!/^[0-9]$/.test(event.key)) {
      event.preventDefault();
    }
  }

  @HostListener('paste', ['$event'])
  onPaste(event: ClipboardEvent): void {
    const texto = event.clipboardData?.getData('text') || '';
    if (!/^\d+$/.test(texto)) {
      event.preventDefault();
    }
  }
}
