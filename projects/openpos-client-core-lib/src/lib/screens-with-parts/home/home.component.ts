import { Component } from '@angular/core';
import { HomeInterface } from './home.interface';
import { ScreenComponent } from '../../shared/decorators/screen-component.decorator';
import { PosScreen } from '../../screens-deprecated/pos-screen/pos-screen.component';
import { OpenposMediaService } from '../../core/services/openpos-media.service';
import { Configuration } from '../../configuration/configuration';
import { Observable } from 'rxjs';

@ScreenComponent({
    name: 'Home'
})
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent extends PosScreen<HomeInterface> {

  gutterSize: Observable<number>;
  gridColumns: Observable<number>;
  isMobile: Observable<boolean>;

  constructor( media: OpenposMediaService ) {
    super();
    this.gridColumns = media.mediaObservableFromMap(new Map([
        ['xs', 1],
        ['sm', 2],
        ['md', 3],
        ['lg', 3],
        ['xl', 3]
      ]));

    this.gutterSize = media.mediaObservableFromMap(new Map([
        ['xs', 10],
        ['sm', 10],
        ['md', 20],
        ['lg', 20],
        ['xl', 20]
    ]));

    this.isMobile = media.mediaObservableFromMap(new Map([
        ['xs', true],
        ['sm', false],
        ['md', false],
        ['lg', false],
        ['xl', false]
    ]));
  }

  buildScreen() {}

  public keybindsEnabled() {
    return Configuration.enableKeybinds;
  }
}
