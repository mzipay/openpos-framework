import { Component, Input, Output, EventEmitter } from '@angular/core';
import { MatButtonToggleChange } from '@angular/material';

@Component({
  selector: 'app-fab-toggle-button',
  templateUrl: './fab-toggle-button.component.html',
  styleUrls: ['./fab-toggle-button.component.scss']
})
export class FabToggleButtonComponent{

    @Input()
    name: string;

    @Input()
    icon: string;

    @Input()
    value: string;

    @Output()
    selected: boolean;

    @Output()
    change: EventEmitter<FabToggleChange> = new EventEmitter<FabToggleChange>();

  constructor() { 
      
  }

  onClick(){
    this.selected = !this.selected;
    this.change.emit(new FabToggleChange(this, this.value));
  }

}

export class FabToggleChange {
  constructor( public source: FabToggleButtonComponent, public value: any) {}
}
