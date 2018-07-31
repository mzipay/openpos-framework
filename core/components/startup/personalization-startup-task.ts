import { SessionService } from './../../services/session.service';
import { IStartupTask } from '../../interfaces';
import { StartupComponent } from './startup.component';

export class PersonalizationStartupTask implements IStartupTask {

    name = 'Personalization';

    order = 500;

    constructor(protected session: SessionService) {

    }

    execute(startupComponent: StartupComponent): boolean {
        startupComponent.message = 'Checking if device is personalized';
        if (!this.session.isPersonalized()) {
            this.session.showScreen(this.session.getPersonalizationScreen());
            return false;
        }  else  {
            return true;
        }
    }

}
