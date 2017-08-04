import { Component, ViewChild, AfterViewInit, DoCheck } from '@angular/core';
import { NgbModule, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { SessionService } from '../session.service';

@Component({
  selector: 'app-dialog',
  templateUrl: './dialog.component.html'
})
export class DialogComponent implements AfterViewInit, DoCheck {

  @ViewChild('content') vcDialog;

  initialized = false;

  public navActions: IMenuItem[] = [];

  constructor(public session: SessionService, private modalService: NgbModal) {
  }

  ngDoCheck(): void {
    if (typeof this.session.screen !== 'undefined') {
      this.navActions = this.session.screen.navActions;
    }
  }

  show(content) {
     this.modalService.open(content, {keyboard: false}).result.then((result) => {
       console.log('result: ' + result);
     }, (reason) => {
       console.log('reason: ' + reason);
     });
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
