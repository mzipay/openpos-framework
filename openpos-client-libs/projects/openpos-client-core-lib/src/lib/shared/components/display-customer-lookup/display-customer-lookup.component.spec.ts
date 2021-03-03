import { TestBed, ComponentFixture } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { DisplayCustomerLookupComponent } from "./display-customer-lookup.component";
import { ICustomerDetails } from "../../../screens-with-parts/customer-search-result-dialog/customer-search-result-dialog.interface";
import {validateText} from "../../../utilites/test-utils";

describe('DisplayCustomerLookupComponent', () => {
    let component: DisplayCustomerLookupComponent;
    let fixture: ComponentFixture<DisplayCustomerLookupComponent>;

    let testCustomer = {name: 'testName', loyaltyNumber: '12345', phoneNumber: '6142345678',
        email: 'testUser@test.com', address:{
            line1: 'testAddressLine1', line2: 'testAddressLine2',
            city: 'testCity', state: 'testState', postalCode: '12345'
        }
    } as ICustomerDetails;

    describe('non mobile', () => {
        beforeEach( () => {
            TestBed.configureTestingModule({
                imports: [],
                declarations: [
                    DisplayCustomerLookupComponent,
                ],
                providers: [],
                schemas: [
                    NO_ERRORS_SCHEMA,
                ]
            }).compileComponents();
            fixture = TestBed.createComponent(DisplayCustomerLookupComponent);
            component = fixture.componentInstance;
            component.customer = testCustomer;
            fixture.detectChanges();
        });
        it('renders', () => {
            expect(component).toBeDefined();
        });

        describe('template', () => {
            it('should display customer name', function () {
                validateText(fixture, '.customer-name', testCustomer.name);
            });
            it('should display customer loyalty number', function () {
                validateText(fixture, '.customer-loyaltyNumber', testCustomer.loyaltyNumber);
            });
            it('should display customer email address', function () {
                validateText(fixture, '.customer-email', testCustomer.email);
            });
            it('should display customer phone number', function () {
                validateText(fixture, '.customer-phoneNumber', testCustomer.phoneNumber);
            });
            it('should display customer address', function () {
                const address = testCustomer.address;

                validateText(fixture, '.customer-line1', address.line1);

                validateText(fixture, '.customer-line2', address.line2);

                const expected = `${address.city}, ${address.state} ${address.postalCode}`;
                validateText(fixture, '.customer-cityStateZip', expected);
            });
        });
    });
});