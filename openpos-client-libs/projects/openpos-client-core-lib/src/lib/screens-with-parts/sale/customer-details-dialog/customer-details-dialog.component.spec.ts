import { TestBed, ComponentFixture } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core'
import {CustomerDetailsDialogComponent} from "./customer-details-dialog.component";
import {CustomerDetailsDialogInterface} from "./customer-details-dialog.interface";
import {ActionService} from "../../../core/actions/action.service";
import {validateDoesNotExist, validateIcon, validateText} from "../../../utilites/test-utils";
import {By} from "@angular/platform-browser";
import {IActionItem} from "../../../core/actions/action-item.interface";

class MockActionService {};

describe('CustomerDetailsDialog', () => {
  let component: CustomerDetailsDialogComponent;
  let fixture: ComponentFixture<CustomerDetailsDialogComponent>;
  let customer;
  beforeEach( () => {
    TestBed.configureTestingModule({
      declarations: [
        CustomerDetailsDialogComponent
      ],
      providers: [
        { provide: ActionService, useClass: MockActionService }
      ],
      schemas: [
        NO_ERRORS_SCHEMA,
      ]
    }).compileComponents();
    fixture = TestBed.createComponent(CustomerDetailsDialogComponent);
    component = fixture.componentInstance;
    customer = {
      name: 'Bob bobert',
      email: 'b.bobert@gmail.com',
      phoneNumber: '(111)-879-8322',
      loyaltyNumber: 's321111111',
      address: {
        line1: '123 Mockingbird Lane',
        city: 'Columbus',
        state: 'OH',
        postalCode: '11111'
      }
    };
    component.screen = { customer } as CustomerDetailsDialogInterface;
    fixture.detectChanges();
  });

  it('renders', () => {
    expect(component).toBeDefined();
  });

  describe('component', () => {
  });

  describe('template', () => {
    describe('user details', () => {
      it('displays an account icon', () => {
        const icon = fixture.debugElement.query(By.css('.grid-container .icon'));
        expect(icon.nativeElement).toBeDefined();
      });

      it('displays the customer name', () => {
        const nameElement = fixture.debugElement.query(By.css('.grid-container .details .customer-name'));
        expect(nameElement.nativeElement).toBeDefined();
        expect(nameElement.nativeElement.textContent).toContain(component.screen.customer.name);
      });

      it('displays the customer email and icon', () => {
        validateText(fixture, '.details .email', component.screen.customer.email);
        validateIcon(fixture, '.details .email app-icon', 'mail_outline');
      });

      it('displays the customer phone number and icon', () => {
        validateText(fixture, '.details .phone-number', component.screen.customer.phoneNumber);
        validateIcon(fixture, '.details .phone-number app-icon', 'phone');
      });

      it('displays the customer loyalty number and icon', () => {
        validateText(fixture, '.details .loyalty-number', component.screen.customer.loyaltyNumber);
        validateIcon(fixture, '.details .loyalty-number app-icon', 'account_heart');
      });

      describe('customer address', () => {
        it('displays the icon', () => {
          validateIcon(fixture, '.details .address app-icon', 'place');
        });

        it('shows line 1, city, state, and postal code when there is no line 2', () => {
          const address = component.screen.customer.address;
          const expectedText = address.line1 + address.city + ', ' + address.state + ' ' + address.postalCode;
          validateText(fixture, '.details .address .address-details', expectedText);
        });

        it('shows line1, line2, city, state, and postal code when there is a line2', () => {
          component.screen.customer.address.line2 = 'some new information';
          fixture.detectChanges();
          const address = component.screen.customer.address;
          const expectedText = address.line1 + address.line2 + address.city + ', ' + address.state + ' ' + address.postalCode;
          validateText(fixture, '.details .address .address-details', expectedText);
        });
      })

    });
    describe('membership details', () => {
      describe('when membership is disabled', () => {
        beforeEach(() => {
          component.screen.membershipEnabled = false;
          fixture.detectChanges();
        });

        it('does not render the details', () => {
          validateDoesNotExist(fixture, '.memberships');
        });
      });
      describe('when membership is enabled', () => {
        let memberships;
        beforeEach(() => {
          memberships = [
            {}, {}, {}
          ]
          component.screen.customer.memberships = memberships;
          component.screen.membershipEnabled = true;
          fixture.detectChanges();
        });

        it('renders the details section', () => {
          const membershipDetailsElement = fixture.debugElement.query(By.css('.memberships'));
          expect(membershipDetailsElement.nativeElement).toBeDefined();
        });

        it('shows the membership label', () => {
          component.screen.membershipLabel = 'some value';
          fixture.detectChanges();
          const membershipLabelElement = fixture.debugElement.query(By.css('.memberships .title'));
          expect(membershipLabelElement.nativeElement.textContent).toContain(component.screen.membershipLabel);
        });

        it('shows a membership-display component for each membership', () => {
          const membershipDisplayComponents = fixture.debugElement.queryAll(By.css('app-membership-display'));
          expect(membershipDisplayComponents.length).toBe(memberships.length);
        });
      });
    })
    describe('actions', () => {
      describe('edit button', () => {
        let button;
        let configuration;
        const selector = 'mat-dialog-actions .edit';
        const setButtonConfiguration = (conf) => {
          component.screen.editButton = conf;
        };

        beforeEach(() => {
          configuration = {
            title: 'Some Title'
          } as IActionItem;
          setButtonConfiguration(configuration);
          fixture.detectChanges();
          button = fixture.debugElement.query(By.css(selector));
        });

        it('renders when the button configuration is set', () => {
          expect(button.nativeElement).toBeDefined();
        });

        it('does not render when the configuration is undefined', () => {
          setButtonConfiguration(undefined);
          fixture.detectChanges();
          validateDoesNotExist(fixture, selector);
        });

        it('displays the configured text', () => {
          expect(button.nativeElement.innerText).toContain(configuration.title);
        });

        it('calls doAction with the configuration when an actionClick event is triggered', () => {
          spyOn(component, 'doAction');
          const button = fixture.debugElement.query(By.css(selector));
          button.nativeElement.dispatchEvent(new Event('actionClick'));
          expect(component.doAction).toHaveBeenCalledWith(configuration);
        });

        it('calls doAction with the configuration when the button is clicked', () => {
          spyOn(component, 'doAction');
          const button = fixture.debugElement.query(By.css(selector));
          button.nativeElement.click();
          expect(component.doAction).toHaveBeenCalledWith(configuration);
        });
      });
      describe('loyalty promotion button', () => {
        let button;
        let configuration;
        const selector = 'mat-dialog-actions .loyalty-promotion';
        const setButtonConfiguration = (conf) => {
          component.screen.loyaltyPromotions = conf;
        };

        beforeEach(() => {
          configuration = {
            title: 'Some Title'
          } as IActionItem;
          setButtonConfiguration(configuration);
          fixture.detectChanges();
          button = fixture.debugElement.query(By.css(selector));
        });

        it('renders when the button configuration is set', () => {
          expect(button.nativeElement).toBeDefined();
        });

        it('does not render when the configuration is undefined', () => {
          setButtonConfiguration(undefined);
          fixture.detectChanges();
          validateDoesNotExist(fixture, selector);
        });

        it('displays the configured text', () => {
          expect(button.nativeElement.innerText).toContain(configuration.title);
        });

        it('calls doAction with the configuration when an actionClick event is triggered', () => {
          spyOn(component, 'doAction');
          const button = fixture.debugElement.query(By.css(selector));
          button.nativeElement.dispatchEvent(new Event('actionClick'));
          expect(component.doAction).toHaveBeenCalledWith(configuration);
        });

        it('calls doAction with the configuration when the button is clicked', () => {
          spyOn(component, 'doAction');
          const button = fixture.debugElement.query(By.css(selector));
          button.nativeElement.click();
          expect(component.doAction).toHaveBeenCalledWith(configuration);
        });
      });
      describe('unlink button', () => {
        let button;
        let configuration;
        const selector = 'mat-dialog-actions .unlink';
        const setButtonConfiguration = (conf) => {
          component.screen.unlinkButton = conf;
        };

        beforeEach(() => {
          configuration = {
            title: 'Some Title'
          } as IActionItem;
          setButtonConfiguration(configuration);
          fixture.detectChanges();
          button = fixture.debugElement.query(By.css(selector));
        });

        it('renders when the button configuration is set', () => {
          expect(button.nativeElement).toBeDefined();
        });

        it('does not render when the configuration is undefined', () => {
          setButtonConfiguration(undefined);
          fixture.detectChanges();
          validateDoesNotExist(fixture, selector);
        });

        it('displays the configured text', () => {
          expect(button.nativeElement.innerText).toContain(configuration.title);
        });

        it('calls doAction with the configuration when an actionClick event is triggered', () => {
          spyOn(component, 'doAction');
          const button = fixture.debugElement.query(By.css(selector));
          button.nativeElement.dispatchEvent(new Event('actionClick'));
          expect(component.doAction).toHaveBeenCalledWith(configuration);
        });

        it('calls doAction with the configuration when the button is clicked', () => {
          spyOn(component, 'doAction');
          const button = fixture.debugElement.query(By.css(selector));
          button.nativeElement.click();
          expect(component.doAction).toHaveBeenCalledWith(configuration);
        });
      });
      describe('done button', () => {
        let button;
        let configuration;
        const selector = 'mat-dialog-actions .done';
        const setButtonConfiguration = (conf) => {
          component.screen.doneButton = conf;
        };

        beforeEach(() => {
          configuration = {
            title: 'Some Title'
          } as IActionItem;
          setButtonConfiguration(configuration);
          fixture.detectChanges();
          button = fixture.debugElement.query(By.css(selector));
        });

        it('renders when the button configuration is set', () => {
          expect(button.nativeElement).toBeDefined();
        });

        it('does not render when the configuration is undefined', () => {
          setButtonConfiguration(undefined);
          fixture.detectChanges();
          validateDoesNotExist(fixture, selector);
        });

        it('displays the configured text', () => {
          expect(button.nativeElement.innerText).toContain(configuration.title);
        });

        it('calls doAction with the configuration when an actionClick event is triggered', () => {
          spyOn(component, 'doAction');
          const button = fixture.debugElement.query(By.css(selector));
          button.nativeElement.dispatchEvent(new Event('actionClick'));
          expect(component.doAction).toHaveBeenCalledWith(configuration);
        });

        it('calls doAction with the configuration when the button is clicked', () => {
          spyOn(component, 'doAction');
          const button = fixture.debugElement.query(By.css(selector));
          button.nativeElement.click();
          expect(component.doAction).toHaveBeenCalledWith(configuration);
        });
      });
    });
  });
});