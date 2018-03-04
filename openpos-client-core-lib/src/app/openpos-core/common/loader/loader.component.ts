import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs/Subscription';

import { LoaderService } from './loader.service';
import { LoaderState } from './loader';

import { SessionService } from '../../services/session.service';

import { Observable } from 'rxjs/Observable';

@Component({
    selector: 'app-loader',
    templateUrl: 'loader.component.html',
    styleUrls: ['loader.component.scss']
})
export class LoaderComponent implements OnInit, OnDestroy {

    LOADING_TITLE = 'Loading...';
    DISCONNECTED_TITLE = 'Reconnecting to Server...';

    show = false;
    title: string = this.LOADING_TITLE;
    message: string = null;

    connected = true;
    loading = false;

    private subscription: any;

    constructor(
        private loaderService: LoaderService,
        public session: SessionService,
    ) { }

    ngOnInit() {
        this.subscription = this.loaderService.loaderState
            .subscribe((state: LoaderState) => {
                this.title = state.title ? state.title : this.LOADING_TITLE;
                this.message = state.message;
                this.show = state.show;
                this.connected = !state.show;
                this.loading = state.show;
            });

        if (this.session.isPersonalized()) {
            const timer = Observable.timer(1000, 1000);
            timer.subscribe(t => this.checkConnectionStatus(this.session));
        }
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
        if (localStorage.getItem('theme')) {
            return localStorage.getItem('theme');
        } else {
            return 'openpos-theme';
        }
    }

    protected checkConnectionStatus(session: SessionService): void {
        const sessionConnected = session.connected();
        if (!sessionConnected && this.connected && session.isPersonalized()) {
            this.connected = sessionConnected;
            this.show = true;
            this.title = this.DISCONNECTED_TITLE;
        } else if (!session.isPersonalized() || (sessionConnected && !this.loading && !this.connected)) {
            this.connected = sessionConnected;
            this.show = false;
        }
    }
}
