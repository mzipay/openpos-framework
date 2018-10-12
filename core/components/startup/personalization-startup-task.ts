import { IStartupTask } from '../../interfaces';
import { PersonalizationService } from '../../services/personalization.service';
import { Observable, Subject } from 'rxjs';
import { MatDialog } from '@angular/material';
import { PersonalizationComponent } from '../personalization/personalization.component';
import { StartupTaskNames } from './startup-task-names';
import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root',
})
export class PersonalizationStartupTask implements IStartupTask {

    name = StartupTaskNames.PERSONALIZATION;

    order = 500;

    constructor(protected personalization: PersonalizationService, protected matDialog: MatDialog) {

    }

    execute(): Observable<string> {
        return Observable.create( (message: Subject<string>) => {
            message.next('Checking if device is personalized.');
            if (!this.personalization.isPersonalized()) {
                message.next('Launching personalization screen.');
                this.matDialog.open(
                    PersonalizationComponent, {
                        disableClose: true,
                        hasBackdrop: false,
                        panelClass: 'openpos-default-theme'
                    }
                    ).afterClosed().subscribe( () => {
                        message.complete();
                     } );
            }  else  {
                message.next('Device is already personalized.');
                message.complete();
            }
        });
    }


}
