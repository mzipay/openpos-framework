import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material';
import { SessionService } from './session.service';
import { IToastScreen, ToastType } from '..';
import { filter } from 'rxjs/operators';

@Injectable({
    providedIn: 'root',
  })
export class ToastService {
    constructor( private snackBar: MatSnackBar, private sessionService: SessionService ) {
        sessionService.getMessages('Screen').pipe(
            filter( s => s.screenType === 'Toast')
        ).subscribe(m => this.showToast(m));
    }

    private showToast( message: any) {
        const toast = message as IToastScreen;
        this.snackBar.open(toast.message, toast.duration === 0 ? 'X' : null, {
            duration: toast.duration,
            panelClass: this.getToastClass(toast.toastType)
        });
    }

    private getToastClass(type: ToastType): string {
        switch (type) {
            case ToastType.Success:
                return 'toast-success';
            case ToastType.Warn:
                return 'toast-warn';
        }

        return null;
    }
}
