import { Component, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { LoaderState } from './loader-state';
import { SessionService } from '../../services/session.service';
import { PersonalizationService } from '../../services/personalization.service';

@Component({
    selector: 'app-loader',
    templateUrl: 'loader.component.html',
    styleUrls: ['loader.component.scss']
})
export class LoaderComponent implements OnInit, OnDestroy {

    show = false;
    title: string = LoaderState.LOADING_TITLE;
    message: string = null;

    connected = true;
    loading = false;

    private subscription: any;

    constructor(
        private personalization: PersonalizationService,
        private session: SessionService,
        private changeRef: ChangeDetectorRef
    ) { }

    ngOnInit() {
        this.subscription = this.session.loaderState.observable
            .subscribe((state: LoaderState) => {
                // console.log(`Got new LoaderState: show=${state.show}`)
                const stateChanging = this.show !== state.show;
                this.title = state.title ? state.title : LoaderState.LOADING_TITLE;
                this.message = state.message;
                this.show = state.show;
                this.connected = !state.show;
                this.loading = state.show;
                // For iOS when returning from the background, angular doesn't
                // always run a change detection.
                if (stateChanging) {
                    this.changeRef.detectChanges();
                }
            });
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

    public getHiddenClass(): string {
        if (this.show) {
            return '';
        } else {
            return 'loader-hidden';
        }
    }

    public getLocalTheme(): string {
        return this.personalization.getTheme();
    }
}
