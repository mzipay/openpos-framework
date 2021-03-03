import { TestBed, ComponentFixture } from '@angular/core/testing';
import { CustomerSearchResultDialogComponent } from './customer-search-result-dialog.component';
import { CustomerSearchResultDialogInterface, ICustomerDetails } from "./customer-search-result-dialog.interface";
import { NO_ERRORS_SCHEMA } from '@angular/core'
import { ActionService } from "../../core/actions/action.service";
import { SelectionMode } from "../../core/interfaces/selection-mode.enum";
import { IActionItem } from "../../core/actions/action-item.interface";
import { By } from "@angular/platform-browser";
class MockActionService {};

describe('CustomerSearchResultDialogComponent', () => {
    let component: CustomerSearchResultDialogComponent;
    let fixture: ComponentFixture<CustomerSearchResultDialogComponent>;

    let testAddress = {line1: 'testStreet', line2: 'testStreetLine2', city: 'testCity', state: 'testState', postalCode: '12345'}
    let testCustomer = {name: 'test', loyaltyNumber: '7327', email: 'testUser@test.com',
        phoneNumber: '614 234 5678', address: testAddress } as ICustomerDetails
    let threeResults = [testCustomer, testCustomer, testCustomer];
    let fourResults = [testCustomer, testCustomer, testCustomer, testCustomer];

    describe('non mobile', () => {
        beforeEach( () => {
            TestBed.configureTestingModule({
                imports: [],
                declarations: [
                    CustomerSearchResultDialogComponent,
                ],
                providers: [
                    { provide: ActionService, useClass: MockActionService }
                ],
                schemas: [
                    NO_ERRORS_SCHEMA,
                ]
            }).compileComponents();
            fixture = TestBed.createComponent(CustomerSearchResultDialogComponent);
            component = fixture.componentInstance;
            component.screen = {viewButton: {title: 'View'}, selectButton: {title: 'Select'} } as CustomerSearchResultDialogInterface;
            fixture.detectChanges();
        });

        it('renders', () => {
            expect(component).toBeDefined();
        });

        describe('component', () => {
            beforeEach(() => {
                fixture = TestBed.createComponent(CustomerSearchResultDialogComponent);
                component = fixture.componentInstance;
                component.screen = {viewButton: {title: 'View'}, selectButton: {title: 'Select'} } as CustomerSearchResultDialogInterface;
                fixture.detectChanges();
            });
            it('transformResultsToMap', () => {
                component.screen.results = threeResults
                let result = component.transformResultsToMap();
                expect(result.size).toBe(3);
                expect(result.get(0)).toBe(component.screen.results[0]);
                expect(result.get(2)).toBe(component.screen.results[2]);
            });

            it('populateListData', () => {
                component.screen.results = threeResults
                component.populateListData();
                component.listData.subscribe(x => {
                    expect(x.items).toBeTruthy();
                    expect(x.disabledItems).toBeTruthy();
                });
            });
            it('defineConfiguration', () => {
                component.screen.results = threeResults
                component.defineConfiguration()
                expect(component.listConfig).toBeDefined()
                expect(component.listConfig.selectionMode).toBe(SelectionMode.Single);
                expect(component.listConfig.totalNumberOfItems).toBe(3);

                component.screen.results = fourResults
                component.defineConfiguration()
                expect(component.listConfig.totalNumberOfItems).toBe(4);
            });
            it('onItemChange', () => {
                component.onItemChange(5);
                expect(component.index).toBe(5);
                component.onItemChange(6);
                expect(component.index).toBe(6);
            });
            it('doSelectionButtonAction ',  () =>{
                spyOn(component, 'doAction')
                component.index = 1;
                component.doSelectionButtonAction({} as IActionItem);
                expect(component.doAction).toHaveBeenCalledWith({} as IActionItem, 1);
            });
        });

        describe('template', () => {
            it('secondary-button', () => {
                const secondaryButton = fixture.debugElement.query(By.css('app-secondary-button'));
                expect(secondaryButton.nativeElement.textContent).toBeDefined();
                spyOn(component, 'doSelectionButtonAction');
                secondaryButton.nativeElement.click()
                expect(component.doSelectionButtonAction).toHaveBeenCalled();
            });

            it('primary-button', () => {
                const secondaryButton = fixture.debugElement.query(By.css('app-primary-button'));
                expect(secondaryButton.nativeElement.textContent).toBeDefined();
                spyOn(component, 'doSelectionButtonAction');
                secondaryButton.nativeElement.click()
                expect(component.doSelectionButtonAction).toHaveBeenCalled();
            });
        });
    });
});