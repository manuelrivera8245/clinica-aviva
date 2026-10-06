import { Pipe, PipeTransform } from '@angular/core';

export type EstadoCita = 'Programada' | 'Atendida' | 'No Asistio' | 'Cancelada';

@Pipe({
  name: 'estadoCita'
})
export class EstadoCitaPipe implements PipeTransform {

  private readonly formatos: Record<string, string> = {
    'Programada':  '📅 Programada',
    'Atendida':    '✅ Atendida',
    'No Asistio':  '⚠️ No Asistió',
    'Cancelada':   '❌ Cancelada'
  };

  transform(value: string): string {
    return this.formatos[value] ?? value;
  }
}
