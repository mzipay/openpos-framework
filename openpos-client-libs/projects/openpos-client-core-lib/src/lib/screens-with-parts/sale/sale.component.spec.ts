import { TestBed, ComponentFixture } from '@angular/core/testing';
import { SaleComponent } from './sale.component';
import { MatDialog, MatBottomSheet } from '@angular/material';
import { OpenposMediaService } from '../../core/media/openpos-media.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { KeyPressProvider } from '../../shared/providers/keypress.provider';
import { NO_ERRORS_SCHEMA } from '@angular/core'
import { ActionService } from '../../core/actions/action.service';
import { By } from '@angular/platform-browser';
import { IActionItem } from '../../core/actions/action-item.interface';
import { ElectronService } from 'ngx-electron';
import { AccentButtonComponent } from '../../shared/components/accent-button/accent-button.component';
import { BaconStripComponent } from '../../shared/screen-parts/bacon-strip/bacon-strip.component';
import { ImageUrlPipe } from '../../shared/pipes/image-url.pipe';
import { TimeZoneContext } from '../../core/client-context/time-zone-context';
import { CLIENTCONTEXT } from '../../core/client-context/client-context-provider.interface';
import { SaleInterface } from './sale.interface';
import { Configuration } from './../../configuration/configuration';
import { Subscription, Observable, of } from 'rxjs';

class MockMatDialog {};
class MockActionService {};
class MockMatBottomSheet {};
class MockKeyPressProvider {
    subscribe(): Subscription {
        return new Subscription();
    }
};
class MockElectronService {};
class ClientContext {};

describe('SaleComponent', () => {
    let component: SaleComponent;
    let fixture: ComponentFixture<SaleComponent>;
    let openposMediaSerivce: OpenposMediaService;

    describe('non mobile', () => {
        class MockOpenposMediaServiceMobile {
            observe(): Observable<boolean> {
                return of(false);
            }
        };
        beforeEach( () => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule, ],
                declarations: [
                    SaleComponent, AccentButtonComponent, BaconStripComponent,
                    ImageUrlPipe
                ],
                providers: [
                    { provide: ActionService, useClass: MockActionService },
                    { provide: MatDialog, useClass: MockMatDialog},
                    { provide: OpenposMediaService, useClass: MockOpenposMediaServiceMobile },
                    { provide: MatBottomSheet, useClass: MockMatBottomSheet},
                    { provide: KeyPressProvider, useClass: MockKeyPressProvider },
                    { provide: ElectronService, useClass: MockElectronService },
                    { provide: ClientContext, useValue: {}},
                    { provide: CLIENTCONTEXT, useClass: TimeZoneContext}
                ],
                schemas: [
                    NO_ERRORS_SCHEMA,
                ]
            }).compileComponents();
            fixture = TestBed.createComponent(SaleComponent);
            component = fixture.componentInstance;
            component.screen = {} as SaleInterface;
            component.screen.orders = [];
            openposMediaSerivce = TestBed.get(OpenposMediaService);
            fixture.detectChanges();
        });

        it('renders', () => {
            expect(component).toBeDefined();
        });

        describe('component', () => {
            describe('keybindsEnabled', () => {
                let menuItem;
                beforeEach(() => {
                    Configuration.enableKeybinds = true;
                    menuItem = {
                        keybind: "F5"
                    };
                })
                it('returns false when Configuration.enableKeybinds is false', () => {
                    Configuration.enableKeybinds = false;
                    expect(component.keybindsEnabled(menuItem)).toBe(false);
                });

                it('returns false when Configuration.enableKeybinds is true and the menuItem.keybind is falsey', () => {
                    menuItem.keybind = undefined;
                    expect(component.keybindsEnabled(menuItem)).toBe(false);
                });

                it('returns false when Configuration.enableKeybinds is true and the menuItem.keybind is truthy and is "Enter"', () => {
                    menuItem.keybind = 'Enter';
                    expect(component.keybindsEnabled(menuItem)).toBe(false);
                });

                it('returns true when the Configuration.enableKeybinds is true and the menuItem.keybind is truthy and not "Enter"', () => {
                    expect(component.keybindsEnabled(menuItem)).toBe(true);
                });
            });
        });

        describe('template', () => {
            describe('sale-header', () => {
                const configureComponent = (readOnly: boolean, linkedCustomerButton: IActionItem, loyaltyButton: IActionItem, customer = undefined) => {
                    component.screen.readOnly = readOnly;
                    component.screen.linkedCustomerButton = linkedCustomerButton;
                    component.screen.loyaltyButton = loyaltyButton;
                    component.screen.customer = customer;
                    fixture.detectChanges();
                };

                const validateDoesNotExist = (selector: string) => {
                    const button = fixture.debugElement.query(By.css(selector));
                    expect(button).toBeNull();
                };

                describe('when read only', () => {
                    beforeEach(() => {
                        configureComponent(true, undefined, {} as IActionItem);
                    });

                    it('does not show the link customer button', () => {
                        validateDoesNotExist('.sale-header .link-customer');
                    });
                });
                describe('when there is no screen data for the loyaltyButton', () => {
                    beforeEach(() => {
                        configureComponent(false, undefined, undefined);
                    });
                    it('does not show the link customer button', () => {
                        validateDoesNotExist('.sale-header .link-customer');
                    });
                });
                describe('when no customer is linked', () => {
                    beforeEach(() => {
                        configureComponent(false, undefined, {
                            keybindDisplayName: "F5",
                            title: "Link customer to apply rewards!"
                        } as IActionItem);
                    });
                    it('it shows the link customer button', () => {
                        const button = fixture.debugElement.query(By.css('.sale-header .link-customer'));
                        expect(button.nativeElement.textContent).toContain('Link customer to apply rewards!');
                    });

                    it('does not show the linked customer button', () => {
                        validateDoesNotExist('.sale-header .linked-customer-summary');
                    });

                    it('calls doAction with the loyaltyButton when an actionClick event is triggered', () => {
                        spyOn(component, 'doAction');
                        const button = fixture.debugElement.query(By.css('.sale-header .link-customer'));
                        button.nativeElement.dispatchEvent(new Event('actionClick'));
                        expect(component.doAction).toHaveBeenCalledWith(component.screen.loyaltyButton);
                    });

                    it('calls doAction with the loyaltyButton when an click event is triggered', () => {
                        spyOn(component, 'doAction');
                        const button = fixture.debugElement.query(By.css('.sale-header .link-customer'));
                        button.nativeElement.click();
                        expect(component.doAction).toHaveBeenCalledWith(component.screen.loyaltyButton);
                    });

                    it('shows the keybinding when keybinds are enabled', () => {
                        spyOn(component, 'keybindsEnabled').and.returnValue(true);
                        fixture.detectChanges();
                        const button = fixture.debugElement.query(By.css('.sale-header .link-customer .loyalty-keybind'));
                        expect(button.nativeElement.textContent).toContain('F5');
                    });

                    it('hides the keybinding when keybinds are disabled', () => {
                        spyOn(component, 'keybindsEnabled').and.returnValue(false);
                        fixture.detectChanges();
                        validateDoesNotExist('.sale-header .link-customer .loyalty-keybind');
                    });
                });
                describe('when a customer is linked', () => {
                    beforeEach(() => {
                        configureComponent(false, { keybindDisplayName: "F7" } as IActionItem, {} as IActionItem, {
                            name: 'bob',
                            label: 'manager',
                            icon: undefined,
                            id: '123'
                        });
                    });

                    it('does not show the link customer button', () => {
                        validateDoesNotExist('.sale-header .link-customer');
                    });

                    it('shows the linked customer button', () => {
                        const button = fixture.debugElement.query(By.css('.sale-header button.linked-customer-summary'));
                        expect(button.nativeElement).toBeDefined();
                    });

                    describe('linked customer button', () => {
                        let button;
                        beforeEach(() => {
                            button = fixture.debugElement.query(By.css('.sale-header button.linked-customer-summary'));
                        });

                        it('displays a account-circle icon', () => {
                            const icon = fixture.debugElement.query(By.css('.sale-header button.account-profile-button'));
                            expect(icon.nativeElement).toBeDefined();
                        });

                        it('displays the customers name', () => {
                            expect(button.nativeElement.textContent).toContain('bob');
                        });

                        it('displays the loyalty id', () => {
                            expect(button.nativeElement.textContent).toContain('Loyalty ID:');
                            expect(button.nativeElement.textContent).toContain('123');
                        });

                        it('calls doAction with the account-profile-button when an actionClick event is triggered', () => {
                            spyOn(component, 'doAction');
                            const profileButton = fixture.debugElement.query(By.css('.sale-header button.account-profile-button'));
                            profileButton.nativeElement.dispatchEvent(new Event('actionClick'));
                            expect(component.doAction).toHaveBeenCalledWith(component.screen.linkedCustomerButton);
                        });

                        it('calls doAction with the account-profile-button when an click event is triggered', () => {
                            spyOn(component, 'doAction');
                            const profileButton = fixture.debugElement.query(By.css('.sale-header button.account-profile-button'));
                            profileButton.nativeElement.click();
                            expect(component.doAction).toHaveBeenCalledWith(component.screen.linkedCustomerButton);
                        });

                        it('calls doAction with the linkedCustomerButton when an actionClick event is triggered', () => {
                            spyOn(component, 'doAction');
                            button.nativeElement.dispatchEvent(new Event('actionClick'));
                            expect(component.doAction).toHaveBeenCalledWith(component.screen.linkedCustomerButton);
                        });

                        it('calls doAction with the linkedCustomerButton when an click event is triggered', () => {
                            spyOn(component, 'doAction');
                            button.nativeElement.click();
                            expect(component.doAction).toHaveBeenCalledWith(component.screen.linkedCustomerButton);
                        });

                        it('displays a left arrow carrot icon', () => {
                            const icon = fixture.debugElement.query(By.css('.sale-header .line-item-caret'));
                            expect(icon.nativeElement).toBeDefined();
                        });

                        it('shows the keybinding when keybinds are enabled', () => {
                            spyOn(component, 'keybindsEnabled').and.returnValue(true);
                            fixture.detectChanges();
                            const button = fixture.debugElement.query(By.css('.sale-header .linked-customer-summary .loyalty-keybind'));
                            expect(button.nativeElement.textContent).toContain('F7');
                        });

                        it('hides the keybinding when keybinds are disabled', () => {
                            spyOn(component, 'keybindsEnabled').and.returnValue(false);
                            fixture.detectChanges();
                            validateDoesNotExist('.sale-header .linked-customer-summary .loyalty-keybind');
                        });
                    });
                });
            });
        });
    });
});