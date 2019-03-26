import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ComponentDemoRoutingModule } from './component-demo-routing.module';
import { DashboardComponent } from './dashboard/dashboard.component';
import { ButtonsComponent } from './buttons/buttons.component';
import { FormsComponent } from './forms/forms.component';
import { SharedModule } from 'openpos-client-core-lib';

@NgModule({
  declarations: [DashboardComponent, ButtonsComponent, FormsComponent],
  imports: [
    CommonModule,
    ComponentDemoRoutingModule,
    SharedModule
  ]
})
export class ComponentDemoModule { }
