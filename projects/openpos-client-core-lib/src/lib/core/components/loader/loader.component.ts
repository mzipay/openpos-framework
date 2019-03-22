import { Logger } from './../../services/logger.service';
import { IMessageHandler } from './../../interfaces/message-handler.interface';
import { Component, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { LoaderState } from './loader-state';
import { SessionService } from '../../services/session.service';
import { PersonalizationService } from '../../services/personalization.service';
import { ILoading } from '../../interfaces/loading.interface';

@Component({
    selector: 'app-loader',
    templateUrl: 'loader.component.html',
    styleUrls: ['loader.component.scss']
})
export class LoaderComponent implements OnInit, OnDestroy, IMessageHandler<ILoading> {
    show = false;
    title: string = LoaderState.LOADING_TITLE;
    message: string = null;

    loading = false;

    constructor(
        private log: Logger,
        private personalization: PersonalizationService,
        private session: SessionService,
        private changeRef: ChangeDetectorRef
    ) { }

    ngOnInit() {
        this.session.registerMessageHandler(this, 'Loading');
    }

    handle(message: ILoading) {
        this.log.info(`received loading message.  queue: ${message.queue}, cancel: ${message.cancel}, title: ${message.title}`);
        if (message.queue) {
            this.loading = true;
            setTimeout(() => {
                this.updateLoading(message, false);
            }, 1000);
        } else {
            this.updateLoading(message, true);
        }
    }

    private updateLoading(message: ILoading, force: boolean) {
        let stateChanging = false;
        if (message.cancel) {
            stateChanging = this.loading;
            this.loading = false;
            this.show = false;
        } else if ((this.loading && !this.show) || force) {
            stateChanging = !this.show;
            this.log.info(`showing the loading dialog NOW with a title of: ${message.title} `);
            this.loading = true;
            this.title = message.title;
            this.message = message.message;
            this.show = true;
        }

        if (stateChanging) {
            this.changeRef.detectChanges();
        }
    }

    ngOnDestroy() {
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
