import { IScreen } from '../interfaces';
import { ViewChild, ComponentFactory, ComponentRef, OnDestroy } from '@angular/core';
import { ScreenDirective } from '../../shared';

export abstract class AbstractTemplate implements IScreen, OnDestroy {

    @ViewChild(ScreenDirective) host: ScreenDirective;

    private currentScreenRef: ComponentRef<IScreen>;

    private actionDisablers = new Map<string, actionShouldBeDisabled>();

    constructor() {
    }

    public installScreen(screenComponentFactory: ComponentFactory<IScreen>): IScreen {
        const viewContainerRef = this.host.viewContainerRef;
        viewContainerRef.clear();
        if (this.currentScreenRef) {
            this.currentScreenRef.destroy();
        }
        this.currentScreenRef = viewContainerRef.createComponent(screenComponentFactory);
        return this.currentScreenRef.instance;
    }

    ngOnDestroy(): void {
        if (this.currentScreenRef) {
            this.currentScreenRef.destroy();
        }
    }

    abstract show(screen: any);

    // tslint:disable-next-line:no-shadowed-variable
    registerActionDisabler(action: string, actionShouldBeDisabled) {
        this.actionDisablers.set(action, actionShouldBeDisabled);
    }

    actionIsDisabled(action: string): boolean {
        if (this.actionDisablers.has(action)) {
            return this.actionDisablers.get(action)();
        }

        return false;
    }
}

export type actionShouldBeDisabled = () => boolean;
