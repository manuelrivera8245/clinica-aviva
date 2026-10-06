import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../../environments/environment';
import { ApiResponse } from '../../../core/models/api-response.model';
import { MedicoResponse } from '../models/medico.model';

@Injectable({ providedIn: 'root' })
export class MedicoService {
  private readonly url = `${environment.apiUrl}/medicos`;
  constructor(private http: HttpClient) { }

  getPerfil(): Observable<MedicoResponse> {
    return this.http.get<ApiResponse<MedicoResponse>>(`${this.url}/perfil`)
      .pipe(map(r => r.datos));
  }
}
