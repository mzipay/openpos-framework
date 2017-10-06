import { InquiryComponent } from './screens/inquiry.component';
import { PosComponent } from './pos/pos.component';
import { KioskComponent } from './kiosk/kiosk.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  { path: '', redirectTo: '/pos', pathMatch: 'full' },
  { path: 'pos',  component: PosComponent },
  { path: 'kiosk',  component: KioskComponent },
  { path: 'inquiry', component: InquiryComponent }
];

@NgModule({
  imports: [ RouterModule.forRoot(routes, { enableTracing: true }) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
