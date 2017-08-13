import { Component, ViewChild, AfterViewInit, DoCheck } from '@angular/core';
import { SessionService } from '../session.service';
import { FocusDirective } from './focus';

@Component({
  selector: 'app-dialog',
  templateUrl: './dialog.component.html'
})
export class DialogComponent implements AfterViewInit, DoCheck {

  @ViewChild('content') vcDialog;

  @ViewChild('test') vcTest;

  initialized = false;

  public buttons: IMenuItem[] = [];

  constructor(public session: SessionService) {
  }

  ngDoCheck(): void {
    if (typeof this.session.screen !== 'undefined') {
      this.buttons = this.session.screen.buttons;
    }
  }

  show(content) {
    //  this.modalService.open(content, {keyboard: false}).result.then((result) => {
    //    console.log('result: ' + result);
    //  }, (reason) => {
    //    console.log('reason: ' + reason);
    //  });

     if (this.vcTest) {
       console.log('found test');
     } else {
       console.log('did not find test');
     }
  }

  ngAfterViewInit(): void {
    this.initialized = true;
    setTimeout(() => this.show(this.vcDialog), 0);    
  }

}

export interface IMenuItem {
  enabled: boolean;
  action: string;
  title: string;
  text: string;
  icon: string;
}
