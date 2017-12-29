import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs/Subscription';

import { LoaderService } from './loader.service';
import { LoaderState } from './loader';

import { SessionService } from '../../services/session.service';

import { Observable } from 'rxjs/Observable';

@Component({
    selector: 'app-loader',
    templateUrl: 'loader.component.html',
    styleUrls: ['loader.component.css']
})
export class LoaderComponent implements OnInit, OnDestroy {

    LOADING_TITLE : string = "Loading...";
    DISCONNECTED_TITLE : string = "Reconnecting to Server...";

    show = false;
    title : string = this.LOADING_TITLE;

    connected = true;
    loading = false;

    private subscription: Subscription;

    constructor(
        private loaderService: LoaderService,
        public session: SessionService,
    ) { }

    ngOnInit() {

        this.subscription = this.loaderService.loaderState
            .subscribe((state: LoaderState) => {
                this.title = this.LOADING_TITLE;
                this.show = state.show;
                this.connected = !state.show;
                this.loading = state.show;
            });

        const connected = this.session.connected();

        const timer = Observable.timer(1000, 1000);
        timer.subscribe(t => this.checkConnectionStatus(this.session));
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
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

        if (!sessionConnected && this.connected) {
            this.connected = sessionConnected;
            this.show = true;
            this.title = this.DISCONNECTED_TITLE;
        } else if (sessionConnected && !this.loading && !this.connected) {
            this.connected = sessionConnected;
            this.show = false;
        }
    }
}
