import { Component, Input } from '@angular/core';
import { IconDefinition, IconService } from '../../../core/services/icon.service';

@Component({
    selector: 'app-icon',
    templateUrl: './icon.component.html',
    styleUrls: ['./icon.component.scss']
})

export class IconComponent {

    @Input()
    set iconName(iconName: string) {
        this.iconDef = this.iconService.resolveIcon(iconName);
        this.isLocalIcon = (iconName && (iconName.startsWith('openpos_')) || (this.iconDef && this.iconDef.iconType === 'svg'));

        if (this.iconDef && this.iconDef.iconType !== 'svg') {
            this._iconName = this.iconDef.iconName;
        } else {
            this._iconName = iconName;
        }
    }

    @Input() iconClass = 'material-icons mat-24';

    private _iconName: string;

    isLocalIcon: boolean;

    iconDef: IconDefinition;

    constructor(private iconService: IconService) {}

    get iconName(): string {
        return this._iconName;
    }
}
