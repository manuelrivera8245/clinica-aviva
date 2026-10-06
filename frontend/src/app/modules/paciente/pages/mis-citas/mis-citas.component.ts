import { Component, OnInit } from '@angular/core';
import { CitaPacienteService } from '../../services/cita-paciente.service';
import { NotificationService } from '../../../../core/services/notification.service';
import { CitaResponse } from '../../models/paciente.model';
import { SidebarItem } from '../../../../shared/components/sidebar/sidebar.component';

@Component({
  selector: 'app-mis-citas',
  templateUrl: './mis-citas.component.html',
  styleUrls: ['./mis-citas.component.css']
})
export class MisCitasComponent implements OnInit {
  citas: CitaResponse[] = [];
  cargando = true;
  citaACancelar: CitaResponse | null = null;
  mostrarModal = false;

  readonly menuItems: SidebarItem[] = [
    { label: 'Dashboard',     icon: 'fa-th-large',       ruta: '/paciente/dashboard' },
    { label: 'Reservar Cita', icon: 'fa-calendar-plus',  ruta: '/paciente/reservar'  },
    { label: 'Mis Citas',     icon: 'fa-calendar-check', ruta: '/paciente/mis-citas' },
    { label: 'Historial',     icon: 'fa-history',        ruta: '/paciente/historial' },
    { label: 'Mi Perfil',     icon: 'fa-user',           ruta: '/paciente/perfil'    }
  ];

  constructor(
    private citaService: CitaPacienteService,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void { this.cargarCitas(); }

  cargarCitas(): void {
    this.cargando = true;
    this.citaService.getMisCitas().subscribe({
      next: citas => { this.citas = citas; this.cargando = false; },
      error: () => { this.cargando = false; }
    });
  }

  abrirModalCancelar(cita: CitaResponse): void {
    this.citaACancelar = cita;
    this.mostrarModal = true;
  }

  onConfirmacion(confirmado: boolean): void {
    this.mostrarModal = false;
    if (!confirmado || !this.citaACancelar) return;
    this.citaService.cancelar({ idCita: this.citaACancelar.idCita }).subscribe({
      next: resultado => {
        if (resultado === 'CANCELACION_EXITOSA') {
          this.notificationService.exito('Cita cancelada exitosamente.');
          this.cargarCitas();
        } else if (resultado === 'FUERA_DE_PLAZO') {
          this.notificationService.warning('No puede cancelar con menos de 24 horas de anticipación.');
        }
      },
      error: () => {
        this.notificationService.error('No se pudo cancelar la cita. Intente nuevamente.');
      }
    });
    this.citaACancelar = null;
  }

  descargarPDF(cita: CitaResponse): void {
    this.citaService.descargarComprobante(cita.idCita).subscribe({
      next: (blob: Blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `comprobante_cita_${cita.idCita}.pdf`;
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
        document.body.removeChild(a);
        this.notificationService.exito('Comprobante descargado correctamente.');
      },
      error: () => {
        this.notificationService.error('Error al descargar el comprobante.');
      }
    });
  }
}
