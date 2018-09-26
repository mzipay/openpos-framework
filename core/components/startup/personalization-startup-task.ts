import { IStartupTask } from '../../interfaces';
import { PersonalizationService } from '../../services/personalization.service';
import { Observable, Subject } from 'rxjs';
import { MatDialog } from '@angular/material';
import { PersonalizationComponent } from '../personalization/personalization.component';
import { StartupTaskData } from './startup-task-data';

export class PersonalizationStartupTask implements IStartupTask {

    name = 'Personalization';

    order = 500;

    constructor(protected personalization: PersonalizationService, protected matDialog: MatDialog) {

    }

    execute( data: StartupTaskData ): Observable<string> {
        return Observable.create( (message: Subject<string>) => {
            message.next('Checking if device is personalized.');
            if (!this.personalization.isPersonalized()) {
                message.next('Launching personalization screen.');
                this.matDialog.open(PersonalizationComponent);
            }  else  {
                message.next('Device is already personalized.');
            }
            message.complete();
        });
    }


}
