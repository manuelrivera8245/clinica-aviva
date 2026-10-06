import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';
import { AuthService } from '../../../core/services/auth.service';
import { UsuarioSesion } from '../../../core/models/usuario.model';
import { NotificacionService, NotificacionResponse } from '../../../core/services/notificacion.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  usuario: UsuarioSesion | null = null;
  isAuthenticated = false;
  
  notificaciones: NotificacionResponse[] = [];
  unreadCount: number = 0;

  constructor(
    public authService: AuthService,
    private notificacionService: NotificacionService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.usuario = this.authService.getUsuarioActual();
    this.isAuthenticated = this.authService.isAuthenticated();
    if (this.isAuthenticated && this.esPaciente) {
      this.cargarNotificaciones();
    }

    // Refrescar notificaciones al cambiar de ruta (ej: luego de reservar una cita)
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(() => {
      this.usuario = this.authService.getUsuarioActual();
      this.isAuthenticated = this.authService.isAuthenticated();
      if (this.isAuthenticated && this.esPaciente) {
        this.cargarNotificaciones();
      }
    });
  }

  cargarNotificaciones(): void {
    this.notificacionService.obtenerMisNotificaciones().subscribe({
      next: (response) => {
        if (response.exito) {
          this.notificaciones = Array.isArray(response.datos) ? response.datos : [];
          this.unreadCount = this.notificaciones?.filter(n => !n.leido)?.length || 0;
        } else {
          this.notificaciones = [];
          this.unreadCount = 0;
        }
      },
      error: (err) => {
        console.error('Error cargando notificaciones', err);
        this.notificaciones = [];
        this.unreadCount = 0;
      }
    });
  }

  abrirNotificaciones(): void {
    if (this.unreadCount > 0) {
      this.notificacionService.marcarComoLeidas().subscribe({
        next: () => {
          this.unreadCount = 0;
          this.notificaciones.forEach(n => n.leido = true);
        },
        error: (err) => console.error('Error al marcar notificaciones como leídas', err)
      });
    }
  }

  get esPaciente(): boolean { return this.usuario?.rol === 'PACIENTE'; }
  get esMedico():   boolean { return this.usuario?.rol === 'MEDICO'; }
  get esAdmin():    boolean { return this.usuario?.rol === 'ADMINISTRADOR'; }

  get nombreMostrado(): string {
    return this.usuario ? `${this.usuario.nombres} ${this.usuario.apellidos}` : '';
  }

  logout(): void {
    this.authService.logout();
  }

  irAlDashboard(): void {
    this.authService.redirigirSegunRol();
  }
}
