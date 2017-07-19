import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PrototypeComponent } from './components/prototype/prototype.component';
import { RawDataComponent } from './components/rawdata/rawdata.component';

const routes: Routes = [
  { path: '', redirectTo: '/prototype', pathMatch: 'full' },
  { path: 'raw',  component: RawDataComponent },
  { path: 'prototype',  component: PrototypeComponent }
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
