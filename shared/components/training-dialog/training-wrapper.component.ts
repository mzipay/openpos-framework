import { Component, Inject, Injector, ReflectiveInjector, ComponentRef } from '@angular/core';
import { TrainingOverlayRef } from './training-overlay-ref';
import { INSTRUCTIONS_DATA } from './training-token';
import { TrainingDialogComponent } from './training-dialog.component';
import { TrainingElement } from './training-element';

@Component({
    selector: 'app-training-wrapper',
    templateUrl: './training-wrapper.component.html'
})
export class TrainingWrapperComponent {

    trainingDialogComponent = TrainingDialogComponent;
    trainingInjectors: Map<string, Injector> = new Map<string, Injector>();
    trainingData: TrainingElement[];

    public currentTheme: string;

    constructor(public injector: Injector, @Inject(TrainingOverlayRef) public dialogRef: TrainingOverlayRef) {
    }

    handleScreenChange(event: { componentRef: ComponentRef<any>, screen: any }) {
        this.updateTrainingData(event.componentRef, event.screen);
    }

    updateTrainingData(componentRef: ComponentRef<any>, screen: any) {
        this.trainingData = [
            {
                key: 'training1',
                instructions: 'These are instructions on how to use this component 1',
                projectableNodes: [[componentRef.location.nativeElement.querySelector('[training1]')]]
            },
            {
                key: 'training2',
                instructions: 'These are instructions on how to use this component 2',
                projectableNodes: [[componentRef.location.nativeElement.querySelector('[training2]')]]
            },
        ];

        if (this.trainingData && this.trainingData.length > 0) {
            for (const element of this.trainingData) {
                this.trainingInjectors.set(element.key,
                    ReflectiveInjector.resolveAndCreate([
                        { provide: INSTRUCTIONS_DATA, useValue: element.instructions }
                    ], this.injector));
            }
        }
    }

    getInjector(element: TrainingElement): Injector {
        return this.trainingInjectors.get(element.key);
    }

    close() {
        this.dialogRef.close();
    }

}
