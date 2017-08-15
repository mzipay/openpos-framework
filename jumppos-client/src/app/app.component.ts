import { DialogComponent } from './screens/dialog.component';
import { IMenuItem } from './screens/imenuitem';
import { Component, OnInit, OnDestroy, DoCheck } from '@angular/core';
import { SessionService } from './session.service';
// import { PromptComponent } from './screens/prompt.component';
// import { SellComponent } from './screens/sell.component';
// import { FormComponent } from './screens/form.component';
// import { HomeComponent } from './screens/home.component';
import { StatusBarComponent } from './screens/statusbar.component';
import { FocusDirective } from './screens/focus';
import { MdDialog, MdDialogRef } from '@angular/material';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html'
})
export class AppComponent implements OnInit, OnDestroy, DoCheck {

  // TODO these should be conflated or removed perhaps.
  public menuItems: IMenuItem[] = [];
  public menuActions: IMenuItem[] = [];
  public backButton: IMenuItem;
  public isCollapsed = true;
  private dialogRef: MdDialogRef<DialogComponent>;

  constructor(public session: SessionService, public dialog: MdDialog) {
  }

  ngOnInit(): void {
    this.session.subscribe();
    console.log('init');
  }

  ngDoCheck(): void {
    if (typeof this.session.screen !== 'undefined') {
      this.menuItems = this.session.screen.menuItems;
      this.menuActions = this.session.screen.menuActions;
      this.backButton = this.session.screen.backButton;
      if (!this.menuActions || this.menuActions.length === 0) {
        this.isCollapsed = true;
      }
    }

    if (this.session.dialog && !this.dialogRef) {
      setTimeout(() => this.openDialog(), 0);
    }

  }

  ngOnDestroy(): void {
    this.session.unsubscribe();
  }

  openDialog() {
    console.log('openDialog');
    this.dialogRef = this.dialog.open(DialogComponent, {disableClose: true});
    this.dialogRef.afterClosed().subscribe(result => {
      this.session.onAction(result);
      this.dialogRef = null;
    });
  }
}
