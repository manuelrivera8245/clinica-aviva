import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../../environments/environment';
import { ApiResponse } from '../../../../core/models/api-response.model';

interface ConfigPublica {
  nombreClinica: string; correoContacto: string;
  telefonoContacto: string; direccion: string;
  horarioAperturaGeneral: string; horarioCierreGeneral: string;
}

@Component({
  selector: 'app-nosotros',
  templateUrl: './nosotros.component.html',
  styleUrls: ['./nosotros.component.css']
})
export class NosotrosComponent implements OnInit {
  config: ConfigPublica = {
    nombreClinica: 'Clínica Aviva',
    correoContacto: 'contacto@clinicaaviva.pe',
    telefonoContacto: '(01) 712-3456',
    direccion: 'Av. Carlos Izaguirre 1200, Los Olivos, Lima, Perú',
    horarioAperturaGeneral: '08:00',
    horarioCierreGeneral: '20:00'
  };

  readonly valores = [
    { titulo: 'Excelencia', desc: 'Atención médica de la más alta calidad con profesionales certificados.', icono: 'fa-award' },
    { titulo: 'Accesibilidad', desc: 'Sistema 24/7 para que gestiones tu salud desde cualquier lugar.', icono: 'fa-universal-access' },
    { titulo: 'Confianza', desc: 'Más de 500 pacientes confían en nosotros para su cuidado médico.', icono: 'fa-handshake' },
    { titulo: 'Innovación', desc: 'Tecnología de vanguardia para reducir el ausentismo médico.', icono: 'fa-lightbulb' }
  ];

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    this.http.get<ApiResponse<ConfigPublica>>(`${environment.apiUrl}/configuracion`)
      .subscribe({ next: res => { if (res.datos) this.config = res.datos; } });
  }
}
