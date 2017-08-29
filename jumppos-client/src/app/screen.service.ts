import { PromptComponent } from './screens/prompt.component';
import { SellComponent } from './screens/sell.component';
import { SellItemDetailComponent } from './screens/sell-item-detail.component';
import { PaymentStatusComponent } from './screens/payment-status.component';
import { FormComponent } from './screens/form.component';
import { HomeComponent } from './screens/home.component';
import { ChooseOptionsComponent } from './screens/choose-options.component';
import { IScreen } from './screens/iscreen';
import { IDialog } from './screens/idialog';
import { Observable } from 'rxjs/Observable';
import { Message } from '@stomp/stompjs';
import { Subscription } from 'rxjs/Subscription';
import { Injectable, Type, ComponentFactoryResolver, ComponentFactory } from '@angular/core';
import { StompService, StompState } from '@stomp/ng2-stompjs';
import { Location } from '@angular/common';
import { Router } from '@angular/router';

@Injectable()
export class ScreenService {

  private screens = new Map<string, Type<IScreen>>();

  constructor(private componentFactoryResolver: ComponentFactoryResolver) {
    // To make a screen available add it here and in entryComponents in the app.module.ts
    this.screens.set('ChooseOptions', ChooseOptionsComponent);
    this.screens.set('Prompt', PromptComponent);
    this.screens.set('Sell', SellComponent);
    this.screens.set('SellItemDetail', SellItemDetailComponent);
    this.screens.set('PaymentStatus', PaymentStatusComponent);
    this.screens.set('Form', FormComponent);
    this.screens.set('Home', HomeComponent);
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


