import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PacienteService } from '../../services/paciente.service';
import { NotificationService } from '../../../../core/services/notification.service';
import { Paciente } from '../../models/paciente.model';
import { SidebarItem } from '../../../../shared/components/sidebar/sidebar.component';

@Component({
  selector: 'app-perfil',
  templateUrl: './perfil.component.html',
  styleUrls: ['./perfil.component.css']
})
export class PerfilComponent implements OnInit {
  form!: FormGroup;
  cargando = true;
  guardando = false;
  paciente: Paciente | null = null;

  readonly menuItems: SidebarItem[] = [
    { label: 'Dashboard',     icon: 'fa-th-large',       ruta: '/paciente/dashboard' },
    { label: 'Reservar Cita', icon: 'fa-calendar-plus',  ruta: '/paciente/reservar'  },
    { label: 'Mis Citas',     icon: 'fa-calendar-check', ruta: '/paciente/mis-citas' },
    { label: 'Historial',     icon: 'fa-history',        ruta: '/paciente/historial' },
    { label: 'Mi Perfil',     icon: 'fa-user',           ruta: '/paciente/perfil'    }
  ];

  constructor(
    private fb: FormBuilder,
    private pacienteService: PacienteService,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      nombres:  ['', [Validators.required, Validators.maxLength(100)]],
      apellidos:['', [Validators.required, Validators.maxLength(100)]],
      correo:   ['', [Validators.required, Validators.email]],
      telefono: ['', [Validators.maxLength(15)]]
    });
    this.pacienteService.getPerfil().subscribe({
      next: p => {
        this.paciente = p;
        this.form.patchValue({ nombres: p.nombres, apellidos: p.apellidos, correo: p.correo, telefono: p.telefono });
        this.cargando = false;
      },
      error: () => { this.cargando = false; }
    });
  }

  get f() { return this.form.controls; }

  onSubmit(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.guardando = true;
    this.pacienteService.actualizarPerfil(this.form.value).subscribe({
      next: () => {
        this.guardando = false;
        this.notificationService.exito('Perfil actualizado correctamente.');
      },
      error: () => {
        this.guardando = false;
        this.notificationService.error('No se pudo actualizar el perfil.');
      }
    });
  }
}
