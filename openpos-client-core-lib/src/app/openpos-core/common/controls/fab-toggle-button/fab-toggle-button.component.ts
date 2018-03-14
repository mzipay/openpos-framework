import { Component, Input, Output } from '@angular/core';

@Component({
  selector: 'app-fab-toggle-button',
  templateUrl: './fab-toggle-button.component.html',
  styleUrls: ['./fab-toggle-button.component.scss']
})
export class FabToggleButtonComponent {

    @Input()
    name: string;

    @Input()
    icon: string;

    @Input()
    value: string;

    @Output()
    selected: boolean;

  constructor() { 
      
  }

  onClick(){
    this.selected = !this.selected;
  }

}
