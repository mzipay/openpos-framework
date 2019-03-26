import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

const routes: Routes = [
    { path: '', redirectTo: 'componentdemo', pathMatch: 'full' },
    {
        path: 'componentdemo',
        loadChildren: './component-demo/component-demo.module#ComponentDemoModule'
      },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
