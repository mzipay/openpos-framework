import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { Injectable } from '@angular/core';
import { MatIconRegistry } from '@angular/material';
import { ImageService } from './image.service';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
    providedIn: 'root',
  })
export class IconService {
    private icons = new Map<string, string>();
      private iconBasePath = '${apiServerBaseUrl}/content?contentPath=/icons/';

    constructor(
        private sanitizer: DomSanitizer,
        private imageService: ImageService,
        private iconRegistry: MatIconRegistry ) {

        this.icons.set('Barcode',           'barcode.svg');
        this.icons.set('Percent',           'percent.svg');
        this.icons.set('Search',            'search.svg');
        this.icons.set( 'Help',             'help.svg' );
        this.icons.set( 'Receipt',          'receipt.svg' );
        this.icons.set( 'NoReceipt',        'block.svg' );
        this.icons.set( 'Error',            'error.svg' );
        this.icons.set( 'Login',            'lock.svg' );
        this.icons.set( 'User',             'person.svg' );
        this.icons.set( 'Password',         'lock.svg' );
        this.icons.set( 'Logout',           'exit_to_app.svg' );
        this.icons.set( 'Home',             'home.svg' );
        this.icons.set( 'DayStart',         'brightness_5.svg' );
        this.icons.set( 'DayEnd',           'brightness_3.svg' );
        this.icons.set( 'Sales',            'local_offer.svg' );
        this.icons.set( 'Phone',            'phone.svg' );
        this.icons.set( 'DefaultPrompt',    'question_answer.svg' );
        this.icons.set( 'Forward',          'keyboard_arrow_right.svg' );
        this.icons.set( 'Back',             'keyboard_arrow_left.svg' );
        this.icons.set( 'Customer',         'person.svg' );
        this.icons.set( 'BusinessCustomer', 'business.svg' );
        this.icons.set( 'Employee',         'person.svg' );
        this.icons.set( 'Employees',        'group.svg' );
        this.icons.set( 'AddEmployee',      'person_add.svg' );
        this.icons.set( 'AddCustomer',      'person_add.svg' );
        this.icons.set( 'Email',            'email.svg' );
        this.icons.set( 'LoyaltyProgram',   'loyalty.svg' );
        this.icons.set( 'CustomerLink',     'link.svg' );
        this.icons.set( 'ExternalLink',     'language.svg' );
        this.icons.set( 'Links',            'link.svg' );
        this.icons.set( 'WebOrder',         'computer.svg' );
        this.icons.set( 'Delivery',         'local_shipping.svg' );
        this.icons.set( 'Pickup',           'schedule.svg' );
        this.icons.set( 'Journal',          'book.svg' );
        this.icons.set( 'Training',         'school.svg' );
        this.icons.set( 'Clock',            'watch_later.svg' );
        this.icons.set( 'Calendar',         'today.svg' );
        this.icons.set( 'CalendarError',    'event_busy.svg' );
        this.icons.set( 'CalendarOk',       'event_available.svg' );
        this.icons.set( 'Completed',        'check.svg' );
        this.icons.set( 'DollarSign',       'attach_money.svg' );
        this.icons.set( 'Discount',         'local_offer.svg' );
        this.icons.set( 'Check',            'money.svg' );
        this.icons.set( 'Cash',             'local_atm.svg' );
        this.icons.set( 'Coin',             'monetization_on.svg' );
        this.icons.set( 'CreditCard',       'credit_card.svg' );
        this.icons.set( 'GiftCard',         'card_giftcard.svg' );
        this.icons.set( 'StoreCard',        'card_membership.svg' );
        this.icons.set( 'PurchaseOrder',    'local_atm.svg' );
        this.icons.set( 'Lease',            'account_balance.svg' );
        this.icons.set( 'Tax',              'account_balance.svg' );
        this.icons.set( 'Account',          'account_balance.svg' );
        this.icons.set( 'Store',            'store.svg' );
        this.icons.set( 'EmployeeEnter',    'keyboard.svg' );
        this.icons.set( 'CustomerEnter',    'dialpad.svg' );
        this.icons.set( 'ItemList',         'list.svg' );
        this.icons.set( 'Print',            'print.svg' );
        this.icons.set( 'Add',              'add_circle.svg' );
        this.icons.set( 'Remove',           'remove_circle.svg' );
        this.icons.set( 'Increment',        'add.svg' );
        this.icons.set( 'Decrement',        'remove.svg' );
        this.icons.set( 'AddToCart',        'add_shopping_cart.svg' );
        this.icons.set( 'SuspendAction',    'pause.svg' );
        this.icons.set( 'ResumeAction',     'play_arrow.svg' );
        this.icons.set( 'RetrieveAction',   'replay.svg' );
        this.icons.set( 'CancelAction',     'block.svg' );
        this.icons.set( 'BypassAction',     'low_priority.svg' );
        this.icons.set( 'ViewAction',       'pageview.svg' );
        this.icons.set( 'Return',           'undo.svg' );
        this.icons.set( 'Trash',            'delete.svg' );
        this.icons.set( 'Close',            'close.svg' );
        this.icons.set( 'Till',             'local_mall.svg' );
        this.icons.set( 'Security',         'security.svg' );
        this.icons.set( 'Reports',          'assessment.svg' );
        this.icons.set( 'HamburgerMenu',    'menu.svg' );
        this.icons.set( 'KebabMenu',        'more_vert.svg');
    }

    public addIconMapping( name: string, icon: string ) {
        this.icons.set(name, icon + '.svg');
    }

    public getIconHtml(name: string): Observable<SafeHtml> {
        if (!name) {
            return null;
        }
        let url: string;
        if ( name.includes('${apiServerBaseUrl}/content?contentPath=') ) {
            url = name;
        } else {
            if ( this.icons.has(name)) {
                name = this.icons.get(name);
            } else {
                name += '.svg';
            }
            url =  this.iconBasePath + name;
            url = this.imageService.replaceImageUrl(url);
        }
        const safeUrl = this.sanitizer.bypassSecurityTrustResourceUrl(url);

        return this.iconRegistry.getSvgIconFromUrl(safeUrl).pipe(
            map( i => this.sanitizer.bypassSecurityTrustHtml(i.outerHTML)));
    }
}
