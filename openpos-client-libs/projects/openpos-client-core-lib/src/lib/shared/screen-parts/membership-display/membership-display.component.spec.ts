import { TestBed, ComponentFixture } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core'
import {ActionService} from "../../../core/actions/action.service";
import {MembershipDisplayComponent} from "./membership-display.component";
import {validateText} from "../../../utilites/test-utils";
import {By} from "@angular/platform-browser";

class MockActionService {};

describe('MembershipDisplayComponent', () => {
    let component: MembershipDisplayComponent;
    let fixture: ComponentFixture<MembershipDisplayComponent>;
    let membership;
    beforeEach( () => {
        TestBed.configureTestingModule({
            declarations: [
                MembershipDisplayComponent
            ],
            providers: [
                { provide: ActionService, useClass: MockActionService }
            ],
            schemas: [
                NO_ERRORS_SCHEMA,
            ]
        }).compileComponents();
        fixture = TestBed.createComponent(MembershipDisplayComponent);
        component = fixture.componentInstance;
        membership = {
            id: '1',
            name: 'My Membership',
            member: true
        };
        component.membership = membership
        fixture.detectChanges();
    });

    it('renders', () => {
        expect(component).toBeDefined();
    });

    describe('component', () => {
    });

    describe('template', () => {
        it('shows the name of the membership', () => {
            validateText(fixture, 'mat-chip', component.membership.name);
        });

        it('has the "in" class when the membership.member is true', () => {
            component.membership.member = true;
            fixture.detectChanges();
            const chip = fixture.debugElement.query(By.css('mat-chip'));
            expect(chip.nativeElement.classList).toContain('in');
        });

        it('has the "not-in" class when the membership.member is false', () => {
            component.membership.member = false;
            fixture.detectChanges();
            const chip = fixture.debugElement.query(By.css('mat-chip'));
            expect(chip.nativeElement.classList).toContain('not-in');
        });
    });
});