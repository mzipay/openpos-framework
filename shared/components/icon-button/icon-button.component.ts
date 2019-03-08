import { Component, Input, EventEmitter, Output, HostListener } from '@angular/core';
import { KeyPressProvider } from '../../providers/keypress.provider';
import { Subscription } from 'rxjs';
import { Configuration } from '../../../configuration/configuration';

@Component({
  selector: 'app-icon-button',
  templateUrl: './icon-button.component.html',
  styleUrls: ['./icon-button.component.scss']
})
export class IconButtonComponent {

  @Input() disabled: boolean;
  @Input() iconName: string;
  @Input() color: string;
  @Input() keyBinding: string;
  @Output() buttonClick = new EventEmitter();

  private subscription: Subscription;

  constructor(private keyPresses: KeyPressProvider) {
    this.disabled = false;
  }

  ngOnInit(): void {
    this.subscription = this.keyPresses.getKeyPresses().subscribe( event => {
        // ignore repeats
        if ( event.repeat || !Configuration.enableKeybinds ) {
            return;
        }
        if (event.type === 'keydown') {
            this.onKeydown(event);
        } 
    });
}

ngOnDestroy(): void {
    this.subscription.unsubscribe();
}     

  clickFn() {
    this.buttonClick.emit(true);
  }

  public onKeydown(event: KeyboardEvent) {
    let bound = false;
    if (this.keyBinding && this.keyBinding === event.key ) {
        bound = true;
        this.clickFn()
    }
    if (bound) {
      event.preventDefault();
    }
  }   
}
