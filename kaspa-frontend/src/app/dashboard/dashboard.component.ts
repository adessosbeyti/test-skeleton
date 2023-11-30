import { Component } from '@angular/core';
import {MessageService} from "primeng/api";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
  providers: [ MessageService ]
})
export class DashboardComponent {
  constructor(private messageService: MessageService) {}

  addMessage() {
    this.messageService.add({ key: 'toast1', severity: 'warn', summary: 'Warning', detail: 'key: toast2' });
  }
}
