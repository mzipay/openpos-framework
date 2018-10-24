import { Component } from '@angular/core';
import { AbstractTemplate, OpenposMediaService } from '../../../core';
import { ISideNavTemplate } from './side-nav.interface';
import { Observable } from 'rxjs';

@Component({
    templateUrl: './side-nav.component.html',
    styleUrls: ['./side-nav.component.scss']
  })
  export class SideNavComponent extends AbstractTemplate<ISideNavTemplate> {

    public drawerOpen: Observable<boolean>;
    public drawerMode: Observable<string>;

    constructor( private mediaService: OpenposMediaService ) {
        super();
    }

    buildTemplate() {
        const openMap = new Map([
            ['xs', false],
            ['sm', true],
            ['md', true],
            ['lg', true],
            ['xl', true]
        ]);

        const modeMap = new Map([
            ['xs', 'over'],
            ['sm', 'side'],
            ['md', 'side'],
            ['lg', 'side'],
            ['xl', 'side']
          ]);

        this.drawerOpen = this.mediaService.mediaObservableFromMap(openMap);
        this.drawerMode = this.mediaService.mediaObservableFromMap(modeMap);
    }

}
