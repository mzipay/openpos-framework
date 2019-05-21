import { ScannerService } from './../../core/services/scanner.service';
import { Component } from '@angular/core';
import { PosScreen } from '../pos-screen/pos-screen.component';
import { ScreenComponent } from '../../shared/decorators/screen-component.decorator';
import { SessionService } from '../../core/services/session.service';

/**
 * @ignore
 */
@ScreenComponent({
  name: 'DynamicForm'
})
@Component({
  selector: 'app-dynamic-form',
  templateUrl: './dynamic-form.component.html'
})
export class DynamicFormComponent extends PosScreen<any> {

    constructor(private scannerService: ScannerService) {
        super();
    }

    buildScreen() {
        this.scannerService.startScanning().subscribe( m => this.session.onAction('Scan', m));
    }

}
