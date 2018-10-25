import { Component, Input, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-secondary-button',
  templateUrl: './secondary-button.component.html',
  styleUrls: ['./secondary-button.component.scss']
})
export class SecondaryButtonComponent {

  @Input() disable: boolean;
  @Input() inputType: string;
  @Output() buttonClick = new EventEmitter();
  constructor() {
    this.disable = false;
  }

  clickFn() {
    this.buttonClick.emit(true);
  }
}
