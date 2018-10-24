import { Component } from '@angular/core';
import { OpenposMediaService } from '../../../core';
import { Observable } from 'rxjs';

@Component({
    selector: 'app-side-nav',
    templateUrl: './side-nav.component.html',
    styleUrls: ['./side-nav.component.scss']
  })
  export class SideNavComponent {

    public drawerOpen: Observable<boolean>;
    public drawerMode: Observable<string>;

    constructor( private mediaService: OpenposMediaService ) {
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
