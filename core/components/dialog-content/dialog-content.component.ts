import { DynamicScreenComponent } from './../dynamic-screen/dynamic-screen.component';
import { ScreenDirective } from './../../../shared/directives/screen.directive';
import { IScreen } from './../dynamic-screen/screen.interface';
import { OverlayContainer } from '@angular/cdk/overlay';
import { Component, OnInit, OnDestroy, ViewChild, ComponentRef, ComponentFactory } from '@angular/core';
import { AbstractTemplate } from '../../../core';

@Component({
  selector: 'app-dialog-content',
  templateUrl: './dialog-content.component.html',
  styleUrls: ['./dialog-content.component.scss']
})
export class DialogContentComponent implements OnDestroy, IScreen {

    @ViewChild(ScreenDirective) host: ScreenDirective;

    private currentScreenRef: ComponentRef<IScreen>;

    private content: IScreen;

    constructor() {
    }

    public installScreen(screenComponentFactory: ComponentFactory<IScreen>): void {
        const viewContainerRef = this.host.viewContainerRef;
        viewContainerRef.clear();
        if (this.currentScreenRef) {
            this.currentScreenRef.destroy();
        }
        this.currentScreenRef = viewContainerRef.createComponent(screenComponentFactory);
        this.content = this.currentScreenRef.instance;
    }

    show(screen: any): void {
        this.content.show(screen);
    }

    ngOnDestroy(): void {
        if (this.currentScreenRef) {
            this.currentScreenRef.destroy();
        }
    }

}
