import { Component, OnInit, ViewChild } from '@angular/core';
import { AbstractTemplate } from '../../common/abstract-template';
import { SessionService } from '../../services/session.service';

@Component({
    selector: 'app-dialog-template',
    templateUrl: './dialog.template.component.html',
    styleUrls: ['./dialog.template.component.scss']
})
export class DialogTemplateComponent extends AbstractTemplate implements OnInit {

    ngOnInit(): void {
    }
}
