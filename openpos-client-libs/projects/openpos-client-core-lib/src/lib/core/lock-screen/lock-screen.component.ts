import {Component, Inject, InjectionToken, OnDestroy} from '@angular/core';
import {Observable, Subject} from 'rxjs';
import {takeUntil, tap} from 'rxjs/operators';
import {ActionService} from '../actions/action.service';
import {LockScreenMessage} from '../messages/lock-screen-message';

export const LOCK_SCREEN_DATA = new InjectionToken<Observable<LockScreenMessage>>('LOCK_SCREEN_DATA');
@Component({
  selector: 'app-lock-screen',
  templateUrl: './lock-screen.component.html',
  styleUrls: ['./lock-screen.component.scss'],
  providers: [ActionService]
})
export class LockScreenComponent implements OnDestroy {
  data: LockScreenMessage;
  password = '';
  username = '';
  override = false;
  destroy = new Subject();

  constructor(
      @Inject(LOCK_SCREEN_DATA) data: Observable<LockScreenMessage>,
      private actionService: ActionService) {
      data.pipe(
          tap( message => this.data = message),
          takeUntil(this.destroy)
      ).subscribe();
  }

  submit($event: any) {
    $event.stopPropagation();
    if (this.override) {
      this.doOverride();
    } else {
      this.submitPassword();
    }
    this.password = '';
  }

  submitPassword() {
    this.actionService.doAction(this.data.passwordAction, this.password);
  }

  doOverride() {
    this.actionService.doAction(this.data.overrideAction, {username: this.username, password: this.password});
  }

  toggleOverride() {
    this.override = !this.override;
  }

  ngOnDestroy(): void {
    this.destroy.next();
  }

}
