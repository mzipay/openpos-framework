import { SessionService } from './../../services/session.service';
import { IStartupTask } from '../../interfaces';
import { StartupComponent } from './startup.component';
import { PersonalizationService } from '../../services/personalization.service';

export class PersonalizationStartupTask implements IStartupTask {

    name = 'Personalization';

    order = 500;

    constructor(private personalization: PersonalizationService, private session: SessionService) {

    }

    execute(startupComponent: StartupComponent): boolean {
        startupComponent.message = 'Checking if device is personalized';
        if (!this.personalization.isPersonalized()) {
            this.session.showScreen(this.personalization.getPersonalizationScreen());
            return false;
        }  else  {
            return true;
        }
    }

}
