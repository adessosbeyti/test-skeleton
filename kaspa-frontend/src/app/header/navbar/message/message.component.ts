import { Component } from '@angular/core';
import {MessageService} from "primeng/api";

@Component({
  selector: 'app-message',
  templateUrl: './message.component.html',
  styleUrls: ['./message.component.scss'],
  providers: [ MessageService ]
})
export class MessageComponent {
  constructor(private messageService: MessageService) { }

  showMessage() {
    this.messageService.add({ severity: 'info', summary: 'Message Summary', detail: 'Message Detail' });
  }

}
