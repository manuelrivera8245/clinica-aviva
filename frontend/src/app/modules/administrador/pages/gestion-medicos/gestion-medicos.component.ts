import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AdminService } from '../../services/admin.service';
import { NotificationService } from '../../../../core/services/notification.service';
import { SidebarItem } from '../../../../shared/components/sidebar/sidebar.component';

@Component({ selector: 'app-gestion-medicos', templateUrl: './gestion-medicos.component.html', styleUrls: ['./gestion-medicos.component.css'] })
export class GestionMedicosComponent implements OnInit {
  medicos: any[] = [];
  especialidades: any[] = [];
  cargando = true;
  form!: FormGroup;
  editando: any = null;
  mostrarForm = false;
  mostrarModal = false;
  medicoADesactivar: any = null;

  readonly menuItems: SidebarItem[] = [
    { label: 'Dashboard',     icon: 'fa-chart-bar',   ruta: '/admin/dashboard' },
    { label: 'Médicos',       icon: 'fa-user-md',     ruta: '/admin/medicos'   },
    { label: 'Turnos',        icon: 'fa-clock',       ruta: '/admin/turnos'    },
    { label: 'Pacientes',     icon: 'fa-users',       ruta: '/admin/pacientes' },
    { label: 'Horarios',      icon: 'fa-calendar-alt', ruta: '/admin/horarios'  },
    { label: 'Reportes',      icon: 'fa-file-alt',    ruta: '/admin/reportes'  },
    { label: 'Configuración', icon: 'fa-cog',         ruta: '/admin/configuracion' }
  ];

  constructor(private adminService: AdminService, private notificationService: NotificationService, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.initForm();
    this.cargar();
    this.adminService.getEspecialidades().subscribe(e => this.especialidades = e);
  }

  initForm(): void {
    this.form = this.fb.group({
      nombres:        ['', [Validators.required]],
      apellidos:      ['', [Validators.required]],
      correo:         ['', [Validators.required, Validators.email]],
      usuario:        ['', [Validators.required]],
      contrasena:     ['', this.editando ? [] : [Validators.required, Validators.minLength(6)]],
      idEspecialidad: [null, [Validators.required]]
    });
  }

  cargar(): void {
    this.cargando = true;
    this.adminService.getMedicos().subscribe({ next: m => { this.medicos = m; this.cargando = false; }, error: () => { this.cargando = false; } });
  }

  abrirCrear(): void { this.editando = null; this.mostrarForm = true; this.initForm(); }
  abrirEditar(m: any): void {
    this.editando = m; this.mostrarForm = true;
    this.initForm();
    // Extraer ID de la especialidad anidada
    this.form.patchValue({
      nombres: m.nombres,
      apellidos: m.apellidos,
      correo: m.correo,
      usuario: m.usuario,
      idEspecialidad: m.especialidad?.idEspecialidad ?? null
    });
    this.form.get('contrasena')?.clearValidators();
  }

  guardar(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    const obs = this.editando
      ? this.adminService.actualizarMedico(this.editando.idMedico, this.form.value)
      : this.adminService.crearMedico(this.form.value);
    obs.subscribe({
      next: () => {
        this.notificationService.exito(this.editando ? 'Médico actualizado.' : 'Médico creado exitosamente.');
        this.mostrarForm = false; this.cargar();
      },
      error: () => this.notificationService.error('Error al guardar el médico.')
    });
  }

  confirmarDesactivar(m: any): void { this.medicoADesactivar = m; this.mostrarModal = true; }
  onConfirmacion(ok: boolean): void {
    this.mostrarModal = false;
    if (!ok || !this.medicoADesactivar) return;
    this.adminService.desactivarMedico(this.medicoADesactivar.idMedico).subscribe({
      next: () => { this.notificationService.exito('Médico desactivado.'); this.cargar(); },
      error: () => this.notificationService.error('No se pudo desactivar.')
    });
    this.medicoADesactivar = null;
  }

  get f() { return this.form.controls; }
}
