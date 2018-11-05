import {
    ComponentRef,
    Directive,
    ViewContainerRef,
    OnInit,
    OnDestroy,
    Output,
    EventEmitter,
    Input
} from '@angular/core';
import { Renderer2 } from '@angular/core';
import { OverlayContainer } from '@angular/cdk/overlay';
import {
    ScreenService,
    DialogService,
    SessionService,
    PersonalizationService,
    Logger,
    AbstractTemplate,
    IScreen
} from '../../core';

// tslint:disable-next-line:directive-selector
@Directive({selector: '[openposScreenOutlet]'})
export class OpenposScreenOutletDirective implements OnInit, OnDestroy {

    private _componentRef: ComponentRef<any>|null = null;
    @Output() componentEmitter = new EventEmitter<{componentRef: ComponentRef<any>, screen: any}>();
    @Input() unsubscribe = true;

    public templateTypeName: string;
    public screenTypeName: string;
    private screenName: string;

    public classes = '';

    public currentTheme: string;

    private installedScreen: IScreen;
    private installedTemplate: AbstractTemplate<any>;

    constructor(
        private log: Logger,
        private personalization: PersonalizationService,
        public screenService: ScreenService,
        private _viewContainerRef: ViewContainerRef,
        public session: SessionService,
        public overlayContainer: OverlayContainer,
        private dialogService: DialogService,
        public renderer: Renderer2) {
    }

    ngOnInit(): void {
        this.session.getMessages('Screen').subscribe( (message) => this.handle(message) );
    }

    ngOnDestroy(): void {
        if (this.unsubscribe === true) {
            this.session.unsubscribe();
        }
        if (this._componentRef) {
            this._componentRef.destroy();
        }
    }

    handle(message: any) {
        if ( (!message.template || !message.template.dialog) &&
            message.screenType !== 'NoOp' &&
            message.screenType !== 'Loading' &&
            message.screenType !== 'Toast') {
            this.updateTemplateAndScreen(message);
        }
    }

    protected updateTemplateAndScreen(screen?: any): void {
        if (!screen) {
            screen = { screenType: 'Blank', template: { type: 'Blank', dialog: false } };
        }

        if (screen &&
            (screen.refreshAlways
                || screen.screenType !== this.screenTypeName
                || screen.name !== this.screenName)
        ) {
            this.logSwitchScreens(screen);

            this.screenName = screen.name;
            let screenToCreate = this.screenTypeName = screen.screenType;
            // If we have a template we'll create that instead of the screen and
            // later we'll install the screen in the temlate
            if ( screen.template ) {
                screenToCreate = this.templateTypeName = screen.template.type;
            }

            this._viewContainerRef.clear();
            this._componentRef = null;

            // Create our screen component
            const componentFactory = this.screenService.resolveScreen(screenToCreate, this.theme);
            this._componentRef = this._viewContainerRef.createComponent(componentFactory, this._viewContainerRef.length, this._viewContainerRef.parentInjector);

            // If we accept an inner screen meaning we are a template, install the screen
            if ( this._componentRef.instance.installScreen ) {
                this.installedTemplate = this._componentRef.instance as AbstractTemplate<any>;
                this.installedScreen = this.installedTemplate.installScreen(this.screenService.resolveScreen(this.screenTypeName, this.theme)) as IScreen;
            }

            const parent = this.renderer.parentNode( this._componentRef.location.nativeElement );
            this.renderer.removeClass(parent, this.currentTheme );

            if (this.personalization.getTheme() !== this.currentTheme) {

                this.overlayContainer.getContainerElement().classList.remove(this.currentTheme);
                this.overlayContainer.getContainerElement().classList.add(this.personalization.getTheme());
                this.currentTheme = this.personalization.getTheme();
            }

            // Add the new theme
            this.renderer.addClass(parent, this.currentTheme );
        }

        if ( this._componentRef.instance.show ) {
            this._componentRef.instance.show( screen );
        }

        if ( this.installedScreen ) {
            this.installedScreen.show( screen, this.installedTemplate);
        }


        this.updateClasses(screen);
        this.dialogService.closeDialog(true);

        // Output the componentRef and screen to the training-wrapper
        this.componentEmitter.emit({componentRef: this._componentRef, screen: screen});
    }

    protected logSwitchScreens(screen: any) {
        let msg = `>>> Switching screens from "${this.screenTypeName}" to "${screen.screenType}"`;
        let nameLogged = false;
        let sequenceLogged = false;
        if (screen.name && screen.name !== screen.screenType) {
            nameLogged = true;
            msg += ` (name "${screen.name}"`;
        }
        if (screen.sequenceNumber) {
            sequenceLogged = true;
            if (!nameLogged) {
                msg += ` (`;
            } else {
                msg += `, `;
            }
            msg += `sequence ${screen.sequenceNumber})`;
        }
        if (nameLogged && !sequenceLogged) {
            msg += `)`;
        }

        this.log.info(msg);
    }

    protected updateClasses(screen: any) {
        if (screen) {
            // remove old classes
            if ( this.classes ) {
                this.classes.split(' ').forEach( c => this.renderer.removeClass(this._componentRef.location.nativeElement, c));
            }
            this.classes = '';
            switch (this.session.getAppId()) {
                case 'pos':
                    if (screen.screenType === 'Home') {
                        this.classes = 'pos main-background';
                    } else {
                        this.classes = 'pos';
                    }
                    break;
                case 'selfcheckout':
                    if (screen.screenType === 'SelfCheckoutHome') {
                        this.classes = 'self-checkout-home selfcheckout';
                    } else {
                        this.classes = 'selfcheckout';
                    }
                    break;
                case 'customerdisplay':
                    this.classes = 'selfcheckout';
                    break;
            }

            // Add new classes
            if ( this.classes ) {
                this.classes.split(' ').forEach( c => this.renderer.addClass(this._componentRef.location.nativeElement, c));
            }
        }
    }

    public get theme() {
        return this.personalization.getTheme();
    }
}
