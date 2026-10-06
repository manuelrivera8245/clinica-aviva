import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {
  AdminService,
  HorarioMedicoItem,
  HorarioMedicoRequest,
  GenerarTurnosRequest
} from '../../services/admin.service';
import { NotificationService } from '../../../../core/services/notification.service';
import { SidebarItem } from '../../../../shared/components/sidebar/sidebar.component';

@Component({
  selector: 'app-gestion-horarios',
  templateUrl: './gestion-horarios.component.html',
  styleUrls: ['./gestion-horarios.component.css']
})
export class GestionHorariosComponent implements OnInit {

  medicos: any[] = [];
  horarios: HorarioMedicoItem[] = [];
  medicoSeleccionado: number | null = null;
  cargando = false;

  // Formulario de nuevo horario
  formHorario!: FormGroup;
  mostrarFormHorario = false;

  // Formulario de generación de turnos
  formGenerar!: FormGroup;
  mostrarFormGenerar = false;
  generando = false;

  // Modal de confirmación de eliminación
  mostrarModal = false;
  horarioAEliminar: HorarioMedicoItem | null = null;

  readonly diasSemana = [
    { valor: 1, nombre: 'Lunes' },
    { valor: 2, nombre: 'Martes' },
    { valor: 3, nombre: 'Miércoles' },
    { valor: 4, nombre: 'Jueves' },
    { valor: 5, nombre: 'Viernes' },
    { valor: 6, nombre: 'Sábado' },
    { valor: 7, nombre: 'Domingo' },
  ];

  readonly menuItems: SidebarItem[] = [
    { label: 'Dashboard',     icon: 'fa-chart-bar',   ruta: '/admin/dashboard'       },
    { label: 'Médicos',       icon: 'fa-user-md',     ruta: '/admin/medicos'         },
    { label: 'Turnos',        icon: 'fa-clock',       ruta: '/admin/turnos'          },
    { label: 'Horarios',      icon: 'fa-calendar-alt',ruta: '/admin/horarios'        },
    { label: 'Pacientes',     icon: 'fa-users',       ruta: '/admin/pacientes'       },
    { label: 'Reportes',      icon: 'fa-file-alt',    ruta: '/admin/reportes'        },
    { label: 'Configuración', icon: 'fa-cog',         ruta: '/admin/configuracion'   },
  ];

  constructor(
    private adminService: AdminService,
    private notificationService: NotificationService,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.formHorario = this.fb.group({
      idMedico:        [null, Validators.required],
      diaSemana:       [null, Validators.required],
      horaInicio:      ['', Validators.required],
      horaFin:         ['', Validators.required],
      duracionTurnoMin:[30,  [Validators.required, Validators.min(15), Validators.max(120)]]
    });

    this.formGenerar = this.fb.group({
      idMedico:   [null, Validators.required],
      fechaDesde: ['',   Validators.required],
      fechaHasta: ['',   Validators.required]
    });

    this.adminService.getMedicos().subscribe(m => this.medicos = m.filter((x: any) => x.activo));
  }

  seleccionarMedico(idMedico: number): void {
    this.medicoSeleccionado = idMedico;
    this.cargarHorarios();
  }

  cargarHorarios(): void {
    if (!this.medicoSeleccionado) return;
    this.cargando = true;
    this.adminService.getHorarios(this.medicoSeleccionado).subscribe({
      next: h => { this.horarios = h; this.cargando = false; },
      error: () => { this.cargando = false; }
    });
  }

  guardarHorario(): void {
    if (this.formHorario.invalid) { this.formHorario.markAllAsTouched(); return; }
    const req: HorarioMedicoRequest = this.formHorario.value;
    this.adminService.crearHorario(req).subscribe({
      next: () => {
        this.notificationService.exito('Horario creado correctamente.');
        this.mostrarFormHorario = false;
        this.formHorario.reset({ duracionTurnoMin: 30 });
        this.medicoSeleccionado = req.idMedico;
        this.cargarHorarios();
      },
      error: err => this.notificationService.error(err?.error?.message || 'Error al crear horario.')
    });
  }

  confirmarEliminar(h: HorarioMedicoItem): void {
    this.horarioAEliminar = h;
    this.mostrarModal = true;
  }

  onConfirmacion(ok: boolean): void {
    this.mostrarModal = false;
    if (!ok || !this.horarioAEliminar) return;
    this.adminService.eliminarHorario(this.horarioAEliminar.idHorario).subscribe({
      next: () => { this.notificationService.exito('Horario eliminado.'); this.cargarHorarios(); },
      error: () => this.notificationService.error('No se pudo eliminar el horario.')
    });
    this.horarioAEliminar = null;
  }

  generarTurnos(): void {
    if (this.formGenerar.invalid) { this.formGenerar.markAllAsTouched(); return; }
    this.generando = true;
    const req: GenerarTurnosRequest = this.formGenerar.value;
    this.adminService.generarTurnosEnLote(req).subscribe({
      next: msg => {
        this.generando = false;
        this.notificationService.exito(msg || 'Turnos generados correctamente.');
        this.mostrarFormGenerar = false;
        this.formGenerar.reset();
      },
      error: err => {
        console.error('Error detallado al generar turnos:', err);
        this.generando = false;
        this.notificationService.error(err?.error?.message || 'Error al generar turnos.');
      }
    });
  }

  get fh() { return this.formHorario.controls; }
  get fg() { return this.formGenerar.controls; }
}
