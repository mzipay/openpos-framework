import { SessionService } from '../../services';
import { Subject, timer, Subscription } from 'rxjs';
import { PersonalizationService } from '../../services/personalization.service';


export class LoaderState {

    public static LOADING_TITLE = 'Loading...';
    public static DISCONNECTED_TITLE = 'Reconnecting to Server...';

    private _show = false;
    private _title: string = null;
    private _message: string = null;

    private loaderSubject = new Subject<LoaderState>();
    observable = this.loaderSubject.asObservable();
    private timerSubscription: Subscription = null;

    public loading = false;

    constructor(private sessionService: SessionService, private personalization: PersonalizationService) {
    }

    public monitorConnection() {
        if (this.timerSubscription) {
            this.timerSubscription.unsubscribe();
        }
        const t = timer(1000, 1000);
        this.timerSubscription = t.subscribe(n => this.checkConnectionStatus());
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

    set message(msg: string) {
        this._message = msg;
    }

    setVisible(visible: boolean, title?: string, message?: string) {
        if (visible && (!this._show || title === LoaderState.DISCONNECTED_TITLE)) {
            this.setLoaderText(title, message);
            this._show = true;
        } else if (!visible) {
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
        if (!this.personalization.isPersonalized()) {
            this.setVisible(false);
        } else {
            const sessionConnected = this.sessionService.connected();
            if (!sessionConnected) {
                this.setVisible(true, LoaderState.DISCONNECTED_TITLE, this.message);
            } else if (sessionConnected && this._title === LoaderState.DISCONNECTED_TITLE) {
                this.setVisible(false);
            }
        }
    }
}
