import { PersonalizationComponent } from '../screens/personalization.component';
import { WarrantyCoverageComponent } from '../screens/warranty-coverage.component';
import { SaleRetrievalComponent } from '../screens/sale-retrieval.component';
import { BasicItemSearchComponent } from '../screens/basic-item-search.component';
import { CartComponent } from '../kiosk/cart.component';
import { EmbeddedWebPageComponent } from '../screens/embedded-web-page.component';
import { PromptComponent } from '../screens/prompt.component';
import { SellComponent } from '../screens/sell.component';
import { SellItemDetailComponent } from '../screens/sell-item-detail.component';
import { SignatureCaptureComponent } from '../screens/signature-capture.component';
import { PaymentStatusComponent } from '../screens/payment-status.component';
import { FormComponent } from '../screens/form.component';
import { HomeComponent } from '../screens/home.component';
import { ItemListComponent } from '../screens/item-list.component';
import { ChooseOptionsComponent } from '../screens/choose-options.component';
import { PromptWithOptionsComponent } from '../screens/prompt-with-options.component';
import { StaticTableComponent } from '../screens/static-table.component';
import { IScreen } from '../common/iscreen';
import { IDialog } from '../common/idialog';
import { Observable } from 'rxjs/Observable';
import { Message } from '@stomp/stompjs';
import { Subscription } from 'rxjs/Subscription';
import { Injectable, Type, ComponentFactoryResolver, ComponentFactory } from '@angular/core';
import { StompService, StompState } from '@stomp/ng2-stompjs';
import { Location } from '@angular/common';
import { Router } from '@angular/router';
import { TenderingComponent } from '../screens/tendering.component';

@Injectable()
export class ScreenService {

  private screens = new Map<string, Type<IScreen>>();

  constructor(private componentFactoryResolver: ComponentFactoryResolver) {
    // To make a screen available add it here and in entryComponents in the app.module.ts
    this.screens.set('BasicItemSearch', BasicItemSearchComponent);
    this.screens.set('Cart', CartComponent);
    this.screens.set('ChooseOptions', ChooseOptionsComponent);
    this.screens.set('EmbeddedWebPage', EmbeddedWebPageComponent);
    this.screens.set('Form', FormComponent);
    this.screens.set('Home', HomeComponent);
    this.screens.set('ItemList', ItemListComponent);
    this.screens.set('PaymentStatus', PaymentStatusComponent);
    this.screens.set('Prompt', PromptComponent);
    this.screens.set('PromptWithOptions', PromptWithOptionsComponent);
    this.screens.set('Sell', SellComponent);
    this.screens.set('SellItemDetail', SellItemDetailComponent);
    this.screens.set('SignatureCapture', SignatureCaptureComponent);
    this.screens.set('Table', StaticTableComponent);
    this.screens.set('SaleRetrieval', SaleRetrievalComponent);
    this.screens.set('Tendering', TenderingComponent);
    this.screens.set('WarrantyCoverage', WarrantyCoverageComponent);
    this.screens.set('Personalization', PersonalizationComponent);
  }

  public resolveScreen(type: string): ComponentFactory<IScreen> {
    const screenType: Type<IScreen> = this.screens.get(type);
    if (screenType) {
      return this.componentFactoryResolver.resolveComponentFactory(screenType);
    } else {
      return null;
    }
  }

}


