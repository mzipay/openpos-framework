import { SessionService } from './../../services/session.service';
import { IStartupTask } from '../../interfaces';
import { StartupComponent } from './startup.component';
import { PersonalizationService } from '../../services/personalization.service';

export class PersonalizationStartupTask implements IStartupTask {

    name = 'Personalization';

    order = 500;

    constructor(protected personalization: PersonalizationService, protected session: SessionService) {

    }

    execute(startupComponent: StartupComponent): Promise<boolean> {
        return new Promise<boolean>((resolve, reject) => {
            console.log('PersonalizationStartupTask is executing...');
            startupComponent.message = 'Checking if device is personalized';
            if (!this.personalization.isPersonalized()) {
                this.session.showScreen(this.personalization.getPersonalizationScreen());
                resolve(false);
            }  else  {
                resolve(true);
            }
            console.log('PersonalizationStartupTask finished.');
        });
    }

}
