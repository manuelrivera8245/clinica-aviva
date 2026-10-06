import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AdminService } from '../../services/admin.service';
import { NotificationService } from '../../../../core/services/notification.service';
import { SidebarItem } from '../../../../shared/components/sidebar/sidebar.component';

@Component({
  selector: 'app-gestion-especialidades',
  templateUrl: './gestion-especialidades.component.html',
  styleUrls: ['./gestion-especialidades.component.css']
})
export class GestionEspecialidadesComponent implements OnInit {
  especialidades: any[] = [];
  cargando = true;
  textoBusqueda = '';

  // Formulario para crear
  mostrarFormulario = false;
  form!: FormGroup;
  guardando = false;

  readonly menuItems: SidebarItem[] = [
    { label: 'Dashboard',     icon: 'fa-chart-bar',   ruta: '/admin/dashboard' },
    { label: 'Médicos',       icon: 'fa-user-md',     ruta: '/admin/medicos'   },
    { label: 'Turnos',        icon: 'fa-clock',       ruta: '/admin/turnos'    },
    { label: 'Pacientes',     icon: 'fa-users',       ruta: '/admin/pacientes' },
    { label: 'Especialidades',icon: 'fa-stethoscope', ruta: '/admin/especialidades' },
    { label: 'Horarios',      icon: 'fa-calendar-alt', ruta: '/admin/horarios'  },
    { label: 'Reportes',      icon: 'fa-file-alt',    ruta: '/admin/reportes'  },
    { label: 'Configuración', icon: 'fa-cog',         ruta: '/admin/configuracion' }
  ];

  constructor(
    private adminService: AdminService,
    private notificationService: NotificationService,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      nombre: ['', [Validators.required, Validators.maxLength(100)]],
      descripcion: ['', [Validators.maxLength(255)]]
    });
    this.cargar();
  }

  cargar(): void {
    this.cargando = true;
    this.adminService.getEspecialidades().subscribe({
      next: esp => { this.especialidades = esp; this.cargando = false; },
      error: () => { this.cargando = false; }
    });
  }

  get especialidadesFiltradas(): any[] {
    if (!this.textoBusqueda.trim()) return this.especialidades;
    const q = this.textoBusqueda.toLowerCase();
    return this.especialidades.filter(e =>
      e.nombre?.toLowerCase().includes(q) ||
      e.descripcion?.toLowerCase().includes(q)
    );
  }

  abrirFormulario(): void {
    this.form.reset();
    this.mostrarFormulario = true;
  }

  cerrarFormulario(): void {
    this.mostrarFormulario = false;
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    
    this.guardando = true;
    const val = this.form.value;
    
    this.adminService.crearEspecialidad(val.nombre, val.descripcion).subscribe({
      next: () => {
        this.guardando = false;
        this.notificationService.exito('Especialidad creada exitosamente.');
        this.cerrarFormulario();
        this.cargar();
      },
      error: (err) => {
        this.guardando = false;
        this.notificationService.error(err?.error?.message || 'Error al crear la especialidad.');
      }
    });
  }
}
