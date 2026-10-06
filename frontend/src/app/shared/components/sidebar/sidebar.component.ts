import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';

export interface SidebarItem {
  label: string;
  icon: string;       // clase FontAwesome, ej: "fa-calendar-day"
  ruta: string;
}

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent {
  @Input() titulo = 'Panel';
  @Input() items: SidebarItem[] = [];

  constructor(private router: Router) {}

  esActivo(ruta: string): boolean {
    return this.router.url.startsWith(ruta);
  }

  navegar(ruta: string): void {
    this.router.navigate([ruta]);
  }
}
