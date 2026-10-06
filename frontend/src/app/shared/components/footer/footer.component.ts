import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { ApiResponse } from '../../../core/models/api-response.model';

interface ConfigPublica {
  nombreClinica: string;
  correoContacto: string;
  telefonoContacto: string;
  direccion: string;
  horarioAperturaGeneral: string;
  horarioCierreGeneral: string;
}

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css']
})
export class FooterComponent implements OnInit {
  anioActual = new Date().getFullYear();
  config: ConfigPublica = {
    nombreClinica: 'Clínica Aviva',
    correoContacto: 'contacto@clinicaaviva.pe',
    telefonoContacto: '(01) 712-3456',
    direccion: 'Av. Carlos Izaguirre 1200, Los Olivos, Lima, Perú',
    horarioAperturaGeneral: '08:00',
    horarioCierreGeneral: '20:00'
  };

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    this.http.get<ApiResponse<ConfigPublica>>(
      `${environment.apiUrl}/configuracion`
    ).subscribe({
      next: res => {
        if (res.datos) this.config = res.datos;
      },
      error: () => {}
    });
  }
}
