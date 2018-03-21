import { IScreen } from './iscreen';
import { OverlayContainer } from '@angular/cdk/overlay';
import { ViewChild, ComponentFactory, ViewContainerRef } from '@angular/core';
import { ScreenDirective } from './screen.directive';
import { AbstractApp } from '../common/abstract-app';
import { SessionService } from '../services/session.service';

export abstract class AbstractTemplate implements IScreen {

    @ViewChild(ScreenDirective) host: ScreenDirective;

    private actionDisablers = new Map<string, actionShouldBeDisabled>();

    constructor() {
    }

    public installScreen(screenComponentFactory: ComponentFactory<IScreen>, session: SessionService, app: AbstractApp): IScreen {
        const viewContainerRef = this.host.viewContainerRef;
        viewContainerRef.clear();
        return viewContainerRef.createComponent(screenComponentFactory).instance;
    }

    abstract show(screen: any, app: AbstractApp);

    registerActionDisabler( action: string, actionShouldBeDisabled ){
        this.actionDisablers.set( action, actionShouldBeDisabled);
    }

    actionIsDisabled( action: string ) : boolean {
        if( this.actionDisablers.has(action) ){
            return this.actionDisablers.get(action)();
        }

        return false;
    }
}

export type actionShouldBeDisabled = () => boolean;
