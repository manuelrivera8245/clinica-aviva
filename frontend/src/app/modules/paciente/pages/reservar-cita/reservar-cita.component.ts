import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CitaPacienteService } from '../../services/cita-paciente.service';
import { NotificationService } from '../../../../core/services/notification.service';
import { TurnoDisponible } from '../../models/paciente.model';
import { SidebarItem } from '../../../../shared/components/sidebar/sidebar.component';

@Component({
  selector: 'app-reservar-cita',
  templateUrl: './reservar-cita.component.html',
  styleUrls: ['./reservar-cita.component.css']
})
export class ReservarCitaComponent implements OnInit {
  pasoActual = 1;
  cargando = false;

  especialidades: any[] = [];
  medicos: any[] = [];
  turnosDisponibles: TurnoDisponible[] = [];

  especialidadSeleccionada: any = null;
  medicoSeleccionado: any = null;
  turnoSeleccionado: TurnoDisponible | null = null;
  fechaBusqueda = '';

  readonly menuItems: SidebarItem[] = [
    { label: 'Dashboard',     icon: 'fa-th-large',       ruta: '/paciente/dashboard' },
    { label: 'Reservar Cita', icon: 'fa-calendar-plus',  ruta: '/paciente/reservar'  },
    { label: 'Mis Citas',     icon: 'fa-calendar-check', ruta: '/paciente/mis-citas' },
    { label: 'Historial',     icon: 'fa-history',        ruta: '/paciente/historial' },
    { label: 'Mi Perfil',     icon: 'fa-user',           ruta: '/paciente/perfil'    }
  ];

  readonly pasos = [
    { num: 1, label: 'Especialidad' },
    { num: 2, label: 'Médico' },
    { num: 3, label: 'Turno' },
    { num: 4, label: 'Confirmar' }
  ];

  constructor(
    private citaService: CitaPacienteService,
    private notificationService: NotificationService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.fechaBusqueda = new Date().toISOString().split('T')[0];
    this.cargarEspecialidades();
  }

  cargarEspecialidades(): void {
    this.cargando = true;
    this.citaService.getEspecialidades().subscribe({
      next: esp => { this.especialidades = esp; this.cargando = false; },
      error: () => { this.cargando = false; }
    });
  }

  seleccionarEspecialidad(esp: any): void {
    this.especialidadSeleccionada = esp;
    this.medicos = [];
    this.medicoSeleccionado = null;
    this.turnoSeleccionado = null;
    this.cargando = true;
    this.citaService.getMedicosPorEspecialidad(esp.idEspecialidad).subscribe({
      next: m => { this.medicos = m; this.cargando = false; this.pasoActual = 2; },
      error: () => { this.cargando = false; }
    });
  }

  seleccionarMedico(medico: any): void {
    this.medicoSeleccionado = medico;
    this.turnoSeleccionado = null;
    this.buscarTurnos();
  }

  buscarTurnos(): void {
    if (!this.medicoSeleccionado) return;
    this.cargando = true;
    this.turnosDisponibles = [];
    this.citaService.getTurnosDisponibles(this.medicoSeleccionado.idMedico, this.fechaBusqueda).subscribe({
      next: t => { this.turnosDisponibles = t; this.cargando = false; this.pasoActual = 3; },
      error: () => { this.cargando = false; }
    });
  }

  seleccionarTurno(turno: TurnoDisponible): void {
    this.turnoSeleccionado = turno;
    this.pasoActual = 4;
  }

  confirmarReserva(): void {
    if (!this.turnoSeleccionado) return;
    this.cargando = true;
    this.citaService.reservar({ idTurno: this.turnoSeleccionado.idTurno }).subscribe({
      next: resultado => {
        this.cargando = false;
        if (resultado === 'RESERVA_EXITOSA') {
          this.notificationService.exito('¡Cita reservada exitosamente! Recibirás una confirmación.');
          this.router.navigate(['/paciente/mis-citas']);
        } else {
          this.notificationService.warning('El turno ya no está disponible. Seleccione otro.');
          this.pasoActual = 3;
          this.turnoSeleccionado = null;
        }
      },
      error: () => {
        this.cargando = false;
        this.notificationService.error('Error al reservar. Intente nuevamente.');
      }
    });
  }

  irAPaso(paso: number): void {
    if (paso < this.pasoActual) this.pasoActual = paso;
  }

  getClasePaso(num: number): string {
    if (num < this.pasoActual) return 'completado';
    if (num === this.pasoActual) return 'activo';
    return '';
  }
}
