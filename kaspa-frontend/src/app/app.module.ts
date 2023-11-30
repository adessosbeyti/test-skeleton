import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { NavbarComponent } from './header/navbar/navbar.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { NavbarLinkComponent } from './header/navbar/navbar-link/navbar-link.component';
import {DividerModule} from "primeng/divider";
import {CardModule} from "primeng/card";
import {ButtonModule} from "primeng/button";
import {MessagesModule} from "primeng/messages";
import {MessageComponent} from "./header/navbar/message/message.component";
import {ToastModule} from "primeng/toast";
import {MessageService} from "primeng/api";
import {InputMaskModule} from "primeng/inputmask";
import {FormsModule} from "@angular/forms";

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    NavbarComponent,
    DashboardComponent,
    NavbarLinkComponent,
    MessageComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    DividerModule,
    CardModule,
    ButtonModule,
    MessagesModule,
    ToastModule,
    InputMaskModule,
    FormsModule
  ],
  providers: [MessageService],
  bootstrap: [AppComponent]
})
export class AppModule { }
