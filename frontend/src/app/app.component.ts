import { Component, OnInit } from '@angular/core';
import { NotificationService, ToastMessage } from './core/services/notification.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'Clínica Aviva';
  toasts: ToastMessage[] = [];

  constructor(private notificationService: NotificationService) {}

  ngOnInit(): void {
    this.notificationService.toasts$.subscribe(toasts => {
      this.toasts = toasts;
    });
  }

  removeToast(index: number): void {
    this.notificationService.remove(index);
  }
}
