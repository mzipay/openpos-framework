import { Component, OnInit, Input, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-primary-button',
  templateUrl: './primary-button.component.html',
  styleUrls: ['./primary-button.component.scss']
})
export class PrimaryButtonComponent implements OnInit {

  @Input() disable: boolean;
  @Input() inputType: string;
  @Output() buttonClick = new EventEmitter();
  constructor() {
    this.disable = false;
  }

  ngOnInit() {
  }

  clickFn() {
    this.buttonClick.emit(true);
  }
}
