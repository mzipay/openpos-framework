import { SessionService } from './../../services/session.service';
import { Injectable } from '@angular/core';
import { Subject } from 'rxjs/Subject';
import { Observable } from 'rxjs/Observable';

export class LoaderState {

    public static LOADING_TITLE = 'Loading...';
    public static DISCONNECTED_TITLE = 'Reconnecting to Server...';

    private _show = false;
    private _title: string = null;
    private _message: string = null;
    private _enabled = true;

    private loaderSubject = new Subject<LoaderState>();
    observable = this.loaderSubject.asObservable();

    constructor(protected sessionService: SessionService) {
        const timer = Observable.timer(1000, 1000);
        timer.subscribe(t => this.checkConnectionStatus());
    }

    get show() {
        return this._show;
    }

    get title() {
        return this._title;
    }

    get message() {
        return this._message;
    }

    get enabled() {
        return this._enabled;
    }

    setEnabled(enabled: boolean) {
        this._enabled = enabled;
        this.setVisible(false);
    }

    setVisible(visible: boolean, title?: string, message?: string) {
        this.setLoaderText(title, message);
        if (visible && this._enabled) {
            this._show = true;
        } else {
            this._show = false;
            this._message = null;
            this._title = null;
        }
        this.loaderSubject.next(this);
    }

    setLoaderText(title?: string, message?: string) {
        this._message = message;
        this._title = title;
        this.loaderSubject.next(this);
    }

    protected checkConnectionStatus(): void {
        if (!this.sessionService.isPersonalized()) {
            this.setVisible(false);
        } else {
            const sessionConnected = this.sessionService.connected();
            if (!sessionConnected) {
                this.setVisible(true, LoaderState.DISCONNECTED_TITLE);
            } else if (sessionConnected && this._title === LoaderState.DISCONNECTED_TITLE) {
                this.setVisible(false);
            }
        }
    }
}
