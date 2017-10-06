
import { IconComponent } from './../common/controls/icon.component';
import { IScreen } from '../common/iscreen';
import { IMenuItem } from '../common/imenuitem';
// import {MdButtonModule, MdCheckboxModule} from '@angular/material';
import { Component, ViewChild, AfterViewInit, DoCheck } from '@angular/core';
import { SessionService } from '../session.service';
import {MediaChange, ObservableMedia} from '@angular/flex-layout';

@Component({
  selector: 'app-inquiry',
  templateUrl: './inquiry.component.html'
})
export class InquiryComponent {
    departments = [
        {
          name: '108 CANDYSNACK',
        },
        {
          name: '109 CANSNPASTA',
        },
        {
          name: '110 BEVGROCERY',
        },
        {
          name: '111 DIRECT STORE DELIVERIES',
        },
        {
          name: '112 ALCHOHOL',
        },
        {
            name: '114 SPECIALTY FOODS',
        }
      ];
      itemClasses = [
        {
          name: '11001 CANDY',
        },
        {
          name: '11002 SEASONAL CANDY',
        },
        {
           name: '11008 SALTY SNACKS & NUTS',
        },
        {
           name: '11009 COOKIES/CRACKERS'
        }
      ];
      merchandiseCategories = [
        {
          name: '1 POTATOCHIP',
        },
        {
          name: '2 PRETZELS',
        },
        {
           name: '3 POPCORN',
        },
        {
           name: '4 NUTS'
        },
        {
           name: '5 MEATSNACKS'
        },
        {
           name: '6 RICE/POPCN'
        }
      ];
}
