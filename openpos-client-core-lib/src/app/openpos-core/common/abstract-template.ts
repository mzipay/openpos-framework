import { IScreen } from './iscreen';
import { OverlayContainer } from '@angular/cdk/overlay';
import { ViewChild, ComponentFactory, ViewContainerRef } from '@angular/core';
import { ScreenDirective } from './screen.directive';
import { AbstractApp } from '../common/abstract-app';
import { SessionService } from '../services/session.service';
import { AfterViewInit } from '@angular/core/src/metadata/lifecycle_hooks';

export abstract class AbstractTemplate implements IScreen, AfterViewInit {

  @ViewChild(ScreenDirective) host: ScreenDirective;

    constructor(
        public overlayContainer: OverlayContainer) {
    }

    public installScreen(screenComponentFactory: ComponentFactory<IScreen>, session: SessionService, app: AbstractApp): IScreen {
        const viewContainerRef = this.host.viewContainerRef;
        viewContainerRef.clear();
        return viewContainerRef.createComponent(screenComponentFactory).instance;
    }

    ngAfterViewInit(): void {
      if ( this.host != null ) {
        console.log('YAY');
      } else {
        console.log('boooo');
      }
    }

    show(session: SessionService, app: AbstractApp) {


    }
}
