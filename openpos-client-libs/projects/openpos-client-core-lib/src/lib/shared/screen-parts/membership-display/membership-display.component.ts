import {Component, Input} from '@angular/core';
import {Membership} from "./memebership-display.interface";


@Component({
    selector: 'app-membership-display',
    templateUrl: './membership-display.component.html',
    styleUrls: ['./membership-display.component.scss']})
export class MembershipDisplayComponent {
    @Input()
    membership: Membership;
    constructor() {}
}
