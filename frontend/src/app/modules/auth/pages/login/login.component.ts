import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../../core/services/auth.service';
import { NotificationService } from '../../../../core/services/notification.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  form!: FormGroup;
  cargando = false;
  mostrarContrasena = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private notificationService: NotificationService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Si ya está autenticado, redirigir
    if (this.authService.isAuthenticated()) {
      this.authService.redirigirSegunRol();
      return;
    }
    this.form = this.fb.group({
      credencial: ['', [Validators.required, Validators.minLength(3)]],
      contrasena: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  get f() { return this.form.controls; }

  onSubmit(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.cargando = true;
    this.authService.login(this.form.value).subscribe({
      next: auth => {
        this.notificationService.exito(`¡Bienvenido, ${auth.nombres}!`);
        this.authService.redirigirSegunRol();
      },
      error: err => {
        this.cargando = false;
        const msg = err?.error?.message || 'Credenciales incorrectas. Verifique sus datos.';
        this.notificationService.error(msg);
      }
    });
  }
}
