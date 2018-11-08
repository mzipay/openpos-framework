import { Component} from '@angular/core';
import { PersonalizationService } from '../../services';
@Component({
    selector: 'app-dynamic-screen',
    templateUrl: './dynamic-screen.component.html',
    styleUrls: ['./dynamic-screen.component.scss'],
})
export class DynamicScreenComponent {

    constructor(private personalization: PersonalizationService) {
    }

    getTheme() {
        return this.personalization.getTheme();
    }
}
