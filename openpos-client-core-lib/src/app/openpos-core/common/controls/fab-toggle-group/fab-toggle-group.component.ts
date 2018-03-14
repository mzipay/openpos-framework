import { Component, ContentChildren, QueryList, AfterViewInit, Input, Output, EventEmitter, Directive } from '@angular/core';
import { FabToggleChange, FabToggleButtonComponent } from '../fab-toggle-button/fab-toggle-button.component';

@Component({
  selector: 'app-fab-toggle-group',
  templateUrl: './fab-toggle-group.component.html',
  styleUrls: ['./fab-toggle-group.component.scss']
})
export class FabToggleGroupComponent implements AfterViewInit {


  @Input() value;
  @Output() valueChange = new EventEmitter();

  @ContentChildren(FabToggleButtonComponent) toggleButtons : QueryList<FabToggleButtonComponent>;

  constructor() { 
      
  }

  ngAfterViewInit(): void {
    this.toggleButtons.forEach( button => button.change.subscribe(event => this.onToggleChange(event.source, event.value)))
  }

  onToggleChange( source: FabToggleButtonComponent, value: any){
    if( source.selected ){
      this.valueChange.emit(value);
      this.toggleButtons.filter(button => button.name != source.name).forEach(button => button.selected = false);
    } else if (this.toggleButtons.filter(button => button.selected == true).length < 1) {
      this.valueChange.emit(null);
    }

  }
}
