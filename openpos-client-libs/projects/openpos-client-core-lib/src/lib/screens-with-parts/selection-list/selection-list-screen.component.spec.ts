import {ComponentFixture, TestBed} from '@angular/core/testing';
import {NO_ERRORS_SCHEMA} from '@angular/core'
import {ActionService} from "../../core/actions/action.service";
import {IActionItem} from "../../core/actions/action-item.interface";
import {By} from "@angular/platform-browser";
import {SelectionListInterface} from "../selection-list/selection-list.interface";
import {ElectronService} from "ngx-electron";
import {CLIENTCONTEXT} from "../../core/client-context/client-context-provider.interface";
import {TimeZoneContext} from "../../core/client-context/time-zone-context";
import {MatDialog} from "@angular/material";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {compilePipeFromMetadata} from "@angular/compiler";
import {SelectableItemListComponentConfiguration} from "../../shared/components/selectable-item-list/selectable-item-list.component";
import {SelectionListScreenComponent} from "./selection-list-screen.component";
import {ISelectionListItem} from "./selection-list-item.interface";
import {SelectionMode} from "../../core/interfaces/selection-mode.enum";
import { ImageUrlPipe } from '../../shared/pipes/image-url.pipe';

class MockActionService {
    doAction(action: IActionItem){}
};
class MockMatDialog {};
class MockElectronService {};
class ClientContext {};

describe('SelectionListScreenComponent', () => {
    let component: SelectionListScreenComponent;
    let fixture: ComponentFixture<SelectionListScreenComponent>;

    let testAddress = {line1: 'testStreet', line2: 'testStreetLine2', city: 'testCity', state: 'testState', postalCode: '12345'};
    let testCustomer = {enabled: true, selected: false} as ISelectionListItem;
    let testCustomerDisabled = {...testCustomer} as ISelectionListItem;
    testCustomerDisabled.enabled = false;
    let threeResults = [testCustomer, testCustomer, testCustomerDisabled];
    let fourResults = [testCustomer, testCustomer, testCustomerDisabled, testCustomer];


    describe('non mobile', () => {
        beforeEach( () => {
            let ClientContext;
            TestBed.configureTestingModule({
                imports: [ HttpClientTestingModule ],
                declarations: [
                    SelectionListScreenComponent,
                    ImageUrlPipe
                ],
                providers: [
                    { provide: ActionService, useClass: MockActionService },
                    { provide: MatDialog, useClass: MockMatDialog },
                    { provide: ElectronService, useClass: MockElectronService },
                    { provide: ClientContext, useValue: {}},
                    { provide: CLIENTCONTEXT, useClass: TimeZoneContext}
                ],
                schemas: [
                    NO_ERRORS_SCHEMA,
                ]
            }).compileComponents();
            fixture = TestBed.createComponent(SelectionListScreenComponent);
            component = fixture.componentInstance;
            component.screen = {nonSelectionButtons: [{title: 'NOTSHOWN'}], selectionButtons: [{title: 'Select'}, {title: 'View'}] } as SelectionListInterface<ISelectionListItem>;
            fixture.detectChanges();
        });

        it('renders', () => {
            expect(component).toBeDefined();
        });

        describe('component', () => {
            beforeEach(() => {
                fixture = TestBed.createComponent(SelectionListScreenComponent);
                component = fixture.componentInstance;
                component.screen = {nonSelectionButtons: [{title: 'NOTSHOWN'}], selectionButtons: [{title: 'Select'}, {title: 'View'}] } as SelectionListInterface<ISelectionListItem>;
                fixture.detectChanges();
            });
            it('transformResultsToMap',(done) => {
                component.screen.selectionList = threeResults
                component.buildScreen();
                component.listData.subscribe(
                    result => {
                        expect(result.items).toBeTruthy();
                        expect(result.disabledItems).toBeTruthy();
                        expect(result.items.size).toBe(3);
                        expect(result.disabledItems.size).toBe(1);
                        expect(result.items.get(0)).toBe(testCustomer);
                        expect(result.disabledItems.get(2)).toBe(testCustomerDisabled)
                        done();
                    },
                    () => {fail(); done();},
                    () => {fail(); done();}
                );
                // expect(result.size).toBe(3);
                // expect(result.get(0)).toBe(component.screen.selectionList[0]);
                // expect(result.get(2)).toBe(component.screen.selectionList[2]);
            });

            it('mutliSelect functionality', () => {
                component.screen.selectionList = threeResults
                component.screen.fetchDataAction = "Something";
                component.buildScreen();
                expect(component.selectedItems).toBeUndefined();

                let nonSelectedCustomer = {...testCustomer} as ISelectionListItem;
                nonSelectedCustomer.selected = true;
                let listIn = [testCustomer, nonSelectedCustomer]
                component.screen.selectionList = listIn
                component.screen.fetchDataAction = undefined
                component.screen.multiSelect = true;
                component.buildScreen();
                expect(component.selectedItems[0]).toBe(nonSelectedCustomer);
                expect(component.indexes.length).toBe(1);

                component.screen.selectionList = listIn
                component.screen.fetchDataAction = undefined
                component.screen.multiSelect = false;
                component.buildScreen();
                expect(component.selectedItem).toBe(nonSelectedCustomer);
                expect(component.index).not.toBe(-1);

            });
            it('defineConfiguration', () => {
                component.screen.numberItemsPerPage = 0;
                component.screen.selectionList = [];
                component.screen.multiSelect = false;
                component.buildScreen();
                expect(component.listConfig instanceof SelectableItemListComponentConfiguration).toBeTruthy();
                expect(component.listConfig.numItemsPerPage).toBe(Number.MAX_VALUE);
                expect(component.listConfig.totalNumberOfItems).toBe(component.screen.numberTotalItems);
                expect(component.listConfig.defaultSelectItemIndex).toBe(component.screen.defaultSelectItemIndex);
                expect(component.listConfig.selectionMode).toBe(SelectionMode.Single);
                expect(component.listConfig.fetchDataAction).toBe(component.screen.fetchDataAction);

                component.screen.numberItemsPerPage = 1000;
                component.screen.selectionList = threeResults;
                component.screen.multiSelect = true;
                component.buildScreen();
                expect(component.listConfig instanceof SelectableItemListComponentConfiguration).toBeTruthy();
                expect(component.listConfig.numItemsPerPage).toBe(component.screen.numberItemsPerPage);
                expect(component.listConfig.totalNumberOfItems).toBe(component.screen.selectionList.length);
                expect(component.listConfig.defaultSelectItemIndex).toBe(component.screen.defaultSelectItemIndex);
                expect(component.listConfig.selectionMode).toBe(SelectionMode.Multiple);
                expect(component.listConfig.fetchDataAction).toBe(component.screen.fetchDataAction);


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
                const secondaryButton = fixture.debugElement.queryAll(By.css('app-secondary-button'));
                expect(secondaryButton[1].nativeElement.textContent).toBeDefined();
                spyOn(component, 'doSelectionButtonAction');
                secondaryButton[0].nativeElement.click();
                secondaryButton[1].nativeElement.click();
                expect(component.doSelectionButtonAction).toHaveBeenCalled();
            });

            it('primary-button', () => {
                const primaryButton = fixture.debugElement.query(By.css('app-primary-button'));
                expect(primaryButton.nativeElement.textContent).toBeDefined();
                spyOn(component, 'doSelectionButtonAction');
                primaryButton.nativeElement.click()
                expect(component.doSelectionButtonAction).toHaveBeenCalled();
            });
        });
    });
});