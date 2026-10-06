import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

export interface ToastMessage {
  tipo: 'exito' | 'error' | 'info' | 'warning';
  mensaje: string;
}

/**
 * Servicio global de notificaciones tipo toast.
 * El AppComponent suscribe el stream y renderiza los toasts.
 */
@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  private toastsSubject = new BehaviorSubject<ToastMessage[]>([]);
  toasts$: Observable<ToastMessage[]> = this.toastsSubject.asObservable();

  private show(tipo: ToastMessage['tipo'], mensaje: string, duracionMs = 4000): void {
    const current = this.toastsSubject.getValue();
    this.toastsSubject.next([...current, { tipo, mensaje }]);
    // Auto-remover después de la duración
    setTimeout(() => {
      const updated = this.toastsSubject.getValue();
      this.toastsSubject.next(updated.slice(1));
    }, duracionMs);
  }

  exito(mensaje: string):   void { this.show('exito',   mensaje); }
  error(mensaje: string):   void { this.show('error',   mensaje); }
  info(mensaje: string):    void { this.show('info',    mensaje); }
  warning(mensaje: string): void { this.show('warning', mensaje); }

  remove(index: number): void {
    const current = this.toastsSubject.getValue();
    const updated = current.filter((_, i) => i !== index);
    this.toastsSubject.next(updated);
  }
}
