import { Component } from '@angular/core';
import { PosScreen } from '../pos-screen/pos-screen.component';
import { SaleInterface } from './sale.interface';
import { Observable } from 'rxjs/internal/Observable';
import { OpenposMediaService } from '../../core';
import { MatDialog } from '@angular/material';

@Component({
  selector: 'app-sale',
  templateUrl: './sale.component.html',
  styleUrls: ['./sale.component.scss']
})
export class SaleComponent extends PosScreen<SaleInterface> {

  overFlowListSize: Observable<number>;
  trainingDrawerOpen = false;

  constructor(private mediaService: OpenposMediaService, protected dialog: MatDialog) {
    super();
    this.overFlowListSize = this.mediaService.mediaObservableFromMap(new Map([
        ['xs', 3],
        ['sm', 3],
        ['md', 4],
        ['lg', 5],
        ['xl', 5]
      ]));

  }

  buildScreen() {
    this.dialog.closeAll();
  }

  onEnter(value: string) {
    this.session.onAction('Next', value);
  }
}
