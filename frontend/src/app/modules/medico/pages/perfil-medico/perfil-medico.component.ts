import { Component, OnInit } from '@angular/core';
import { MedicoService } from '../../services/medico.service';
import { MedicoResponse } from '../../models/medico.model';
import { SidebarItem } from '../../../../shared/components/sidebar/sidebar.component';

@Component({
  selector: 'app-perfil-medico',
  templateUrl: './perfil-medico.component.html',
  styleUrls: ['./perfil-medico.component.css']
})
export class PerfilMedicoComponent implements OnInit {
  medico: MedicoResponse | null = null;
  cargando = true;
  error = false;

  readonly menuItems: SidebarItem[] = [
    { label: 'Dashboard',     icon: 'fa-chart-bar',       ruta: '/medico/dashboard' },
    { label: 'Agenda de Hoy', icon: 'fa-calendar-day',    ruta: '/medico/agenda'    },
    { label: 'Historial',     icon: 'fa-history',         ruta: '/medico/historial' },
    { label: 'Mi Perfil',     icon: 'fa-user-md',         ruta: '/medico/perfil'    }
  ];

  constructor(private medicoService: MedicoService) {}

  ngOnInit(): void {
    this.cargarPerfil();
  }

  cargarPerfil(): void {
    this.cargando = true;
    this.error = false;
    this.medicoService.getPerfil().subscribe({
      next: (data) => {
        this.medico = data;
        this.cargando = false;
      },
      error: () => {
        this.error = true;
        this.cargando = false;
      }
    });
  }
}
