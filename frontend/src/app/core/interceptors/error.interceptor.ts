import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';
import { TokenService } from '../services/token.service';
import { NotificationService } from '../services/notification.service';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {

  constructor(
    private tokenService: TokenService,
    private notificationService: NotificationService,
    private router: Router
  ) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        switch (error.status) {
          case 401:
            this.tokenService.clearSession();
            this.notificationService.warning('Sesión expirada. Por favor inicie sesión nuevamente.');
            this.router.navigate(['/auth/login']);
            break;
          case 403:
            this.notificationService.error('No tiene permisos para realizar esta acción.');
            break;
          case 404:
            this.notificationService.warning('El recurso solicitado no fue encontrado.');
            break;
          case 409:
            this.notificationService.warning(
              error.error?.message || 'Ya existe un registro con esos datos.'
            );
            break;
          case 0:
            this.notificationService.error('No se pudo conectar con el servidor. Verifique que el backend esté activo en localhost:8080.');
            break;
          default:
            if (error.status >= 500) {
              this.notificationService.error('Error interno del servidor. Intente nuevamente en unos momentos.');
            }
        }
        return throwError(() => error);
      })
    );
  }
}
