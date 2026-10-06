import { Pipe, PipeTransform } from '@angular/core';

/**
 * Pipe diaSemana.
 * Convierte el número de día de semana (1-7) al nombre en español.
 * Coincide con el enum DiaSemana.java (1=Lunes, 7=Domingo).
 *
 * Uso: {{ horario.diaSemana | diaSemana }}
 */
@Pipe({
  name: 'diaSemana'
})
export class DiaSemanaPipe implements PipeTransform {

  private readonly dias: Record<number, string> = {
    1: 'Lunes',
    2: 'Martes',
    3: 'Miércoles',
    4: 'Jueves',
    5: 'Viernes',
    6: 'Sábado',
    7: 'Domingo'
  };

  transform(value: number): string {
    return this.dias[value] ?? `Día ${value}`;
  }
}
