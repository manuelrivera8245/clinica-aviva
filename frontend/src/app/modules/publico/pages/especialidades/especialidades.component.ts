import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../../environments/environment';
import { ApiResponse } from '../../../../core/models/api-response.model';

interface Especialidad { idEspecialidad: number; nombre: string; descripcion: string; activo: boolean; }

@Component({ selector: 'app-especialidades', templateUrl: './especialidades.component.html', styleUrls: ['./especialidades.component.css'] })
export class EspecialidadesComponent implements OnInit {
  especialidades: Especialidad[] = [];
  cargando = true;
  readonly iconos = ['fa-heart', 'fa-child', 'fa-heartbeat', 'fa-spa', 'fa-venus', 'fa-brain', 'fa-eye', 'fa-bone', 'fa-smile-beam', 'fa-running'];

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    this.http.get<ApiResponse<Especialidad[]>>(`${environment.apiUrl}/especialidades`)
      .subscribe({ next: res => { this.especialidades = res.datos || []; this.cargando = false; }, error: () => { this.cargando = false; } });
  }

  getIcono(index: number): string { return this.iconos[index % this.iconos.length]; }
}
