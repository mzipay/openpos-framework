import {Component, Input} from '@angular/core';


@Component({
    selector: 'app-membership-display',
    templateUrl: './membership-display.component.html',
    styleUrls: ['./membership-display.component.scss']})
export class MembershipDisplayComponent {
    @Input()
    membership: { id: string, name: string, member: boolean};
    constructor() {}
}
