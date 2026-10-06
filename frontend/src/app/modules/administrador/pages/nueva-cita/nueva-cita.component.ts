import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { AdminService, TurnoDisponibleAdmin } from '../../services/admin.service';
import { NotificationService } from '../../../../core/services/notification.service';
import { SidebarItem } from '../../../../shared/components/sidebar/sidebar.component';

@Component({
  selector: 'app-nueva-cita',
  templateUrl: './nueva-cita.component.html',
  styleUrls: ['./nueva-cita.component.css']
})
export class NuevaCitaComponent implements OnInit {
  idPaciente!: number;
  pasoActual = 1;
  cargando = false;

  // Datos de los pasos
  especialidades: any[] = [];
  medicos: any[] = [];
  turnosDisponibles: TurnoDisponibleAdmin[] = [];

  // Selecciones
  especialidadSeleccionada: any = null;
  medicoSeleccionado: any = null;
  turnoSeleccionado: TurnoDisponibleAdmin | null = null;
  fechaBusqueda = '';

  readonly menuItems: SidebarItem[] = [
    { label: 'Dashboard',     icon: 'fa-chart-bar',   ruta: '/admin/dashboard' },
    { label: 'Médicos',       icon: 'fa-user-md',     ruta: '/admin/medicos'   },
    { label: 'Turnos',        icon: 'fa-clock',       ruta: '/admin/turnos'    },
    { label: 'Pacientes',     icon: 'fa-users',       ruta: '/admin/pacientes' },
    { label: 'Horarios',      icon: 'fa-calendar-alt', ruta: '/admin/horarios'  },
    { label: 'Reportes',      icon: 'fa-file-alt',    ruta: '/admin/reportes'  },
    { label: 'Configuración', icon: 'fa-cog',         ruta: '/admin/configuracion' }
  ];

  readonly pasos = [
    { num: 1, label: 'Especialidad' },
    { num: 2, label: 'Médico' },
    { num: 3, label: 'Turno' },
    { num: 4, label: 'Confirmar' }
  ];

  constructor(
    private adminService: AdminService,
    private notificationService: NotificationService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (!id) {
      this.notificationService.error('Paciente no seleccionado.');
      this.router.navigate(['/admin/pacientes']);
      return;
    }
    this.idPaciente = +id;
    this.fechaBusqueda = new Date().toISOString().split('T')[0];
    this.cargarEspecialidades();
  }

  cargarEspecialidades(): void {
    this.cargando = true;
    this.adminService.getEspecialidades().subscribe({
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
    this.adminService.getMedicosPorEspecialidad(esp.idEspecialidad).subscribe({
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
    this.adminService.getTurnosDisponibles(this.medicoSeleccionado.idMedico, this.fechaBusqueda).subscribe({
      next: t => { this.turnosDisponibles = t; this.cargando = false; this.pasoActual = 3; },
      error: () => { this.cargando = false; }
    });
  }

  seleccionarTurno(turno: TurnoDisponibleAdmin): void {
    this.turnoSeleccionado = turno;
    this.pasoActual = 4;
  }

  confirmarReserva(): void {
    if (!this.turnoSeleccionado || !this.idPaciente) return;
    this.cargando = true;
    this.adminService.reservarCitaParaPaciente(this.idPaciente, this.turnoSeleccionado.idTurno).subscribe({
      next: resultado => {
        this.cargando = false;
        if (resultado === 'RESERVA_EXITOSA') {
          this.notificationService.exito('¡Cita agendada exitosamente por el administrador!');
          this.router.navigate(['/admin/pacientes']);
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
