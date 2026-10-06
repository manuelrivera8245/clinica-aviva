import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl, AsyncValidatorFn, ValidationErrors } from '@angular/forms';
import { Router } from '@angular/router';
import { Observable, timer, of } from 'rxjs';
import { map, switchMap, catchError } from 'rxjs/operators';
import { AuthService } from '../../../../core/services/auth.service';
import { NotificationService } from '../../../../core/services/notification.service';

@Component({
  selector: 'app-registro',
  templateUrl: './registro.component.html',
  styleUrls: ['./registro.component.css']
})
export class RegistroComponent implements OnInit {
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
    this.form = this.fb.group({
      dni:       ['', [Validators.required, Validators.pattern('^\\d{8}$')], [this.validarCredencialAsync(this.authService)]],
      nombres:   ['', [Validators.required, Validators.maxLength(100)]],
      apellidos: ['', [Validators.required, Validators.maxLength(100)]],
      correo:    ['', [Validators.required, Validators.email, Validators.maxLength(150)], [this.validarCredencialAsync(this.authService)]],
      telefono:  ['', [Validators.maxLength(15)]],
      contrasena: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(100)]]
    });
  }

  get f() { return this.form.controls; }

  validarCredencialAsync(authService: AuthService): AsyncValidatorFn {
    return (control: AbstractControl): Observable<ValidationErrors | null> => {
      if (!control.value) {
        return of(null);
      }
      // Debounce de 500ms y solicitud al backend
      return timer(500).pipe(
        switchMap(() => authService.verificarCredencial(control.value)),
        map(existe => (existe ? { credencialEnUso: true } : null)),
        catchError(() => of(null))
      );
    };
  }

  onSubmit(): void {
    if (this.form.invalid || this.form.pending) {
      this.form.markAllAsTouched();
      return;
    }
    this.cargando = true;
    this.authService.registrar(this.form.value).subscribe({
      next: auth => {
        this.notificationService.exito(`¡Bienvenido a Clínica Aviva, ${auth.nombres}! Tu cuenta ha sido creada.`);
        this.router.navigate(['/paciente/dashboard']);
      },
      error: err => {
        this.cargando = false;
        const msg = err?.error?.message || 'No se pudo completar el registro. Intente nuevamente.';
        this.notificationService.error(msg);
      }
    });
  }
}
