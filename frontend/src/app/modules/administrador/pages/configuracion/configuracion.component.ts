import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AdminService } from '../../services/admin.service';
import { NotificationService } from '../../../../core/services/notification.service';
import { SidebarItem } from '../../../../shared/components/sidebar/sidebar.component';

@Component({ selector: 'app-configuracion', templateUrl: './configuracion.component.html', styleUrls: ['./configuracion.component.css'] })
export class ConfiguracionComponent implements OnInit {
  form!: FormGroup;
  cargando = true;
  guardando = false;

  readonly menuItems: SidebarItem[] = [
    { label: 'Dashboard',     icon: 'fa-chart-bar',   ruta: '/admin/dashboard' },
    { label: 'Médicos',       icon: 'fa-user-md',     ruta: '/admin/medicos'   },
    { label: 'Turnos',        icon: 'fa-clock',       ruta: '/admin/turnos'    },
    { label: 'Pacientes',     icon: 'fa-users',       ruta: '/admin/pacientes' },
    { label: 'Horarios',      icon: 'fa-calendar-alt', ruta: '/admin/horarios'  },
    { label: 'Reportes',      icon: 'fa-file-alt',    ruta: '/admin/reportes'  },
    { label: 'Configuración', icon: 'fa-cog',         ruta: '/admin/configuracion' }
  ];

  constructor(
    private fb: FormBuilder,
    private adminService: AdminService,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      nombreClinica:             ['', [Validators.required, Validators.maxLength(200)]],
      correoContacto:            ['', [Validators.required, Validators.email]],
      telefonoContacto:          ['', [Validators.required]],
      direccion:                 ['', [Validators.required]],
      horarioAperturaGeneral:    ['', [Validators.required]],
      horarioCierreGeneral:      ['', [Validators.required]],
      duracionTurnoMinutos:      [30, [Validators.required, Validators.min(10)]],
      diasAnticipacionReserva:   [7,  [Validators.required, Validators.min(1)]],
      diasMinimosCancelacion:    [1,  [Validators.required, Validators.min(0)]]
    });
    this.adminService.getConfiguracion().subscribe({
      next: config => {
        if (config) this.form.patchValue(config);
        this.cargando = false;
      },
      error: () => { this.cargando = false; }
    });
  }

  get f() { return this.form.controls; }

  onSubmit(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.guardando = true;
    this.adminService.actualizarConfiguracion(this.form.value).subscribe({
      next: () => {
        this.guardando = false;
        this.notificationService.exito('Configuración guardada exitosamente.');
      },
      error: () => {
        this.guardando = false;
        this.notificationService.error('No se pudo guardar la configuración.');
      }
    });
  }
}
