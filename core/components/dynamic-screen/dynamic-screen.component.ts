import { HttpClient } from '@angular/common/http';
import { ChangeDetectorRef, Renderer2, ElementRef } from '@angular/core';
import { Component, ViewChild, HostListener, ComponentRef, OnDestroy, OnInit, ComponentFactory } from '@angular/core';
import { MatDialog, MatDialogRef, MatSnackBar,  MatSnackBarRef, SimpleSnackBar, MatExpansionPanel } from '@angular/material';
import { OverlayContainer } from '@angular/cdk/overlay';
import { Router } from '@angular/router';
import { AbstractTemplate } from '../abstract-template';
import { Configuration } from '../../../configuration/configuration';
import { IPlugin } from '../../plugins';
import {
    ScreenService,
    DialogService,
    SessionService,
    DeviceService,
    IconService,
    PluginService,
    FileUploadService
} from '../../services';
import { IScreen } from './screen.interface';
import { Element, OpenPOSDialogConfig, ActionMap, IMenuItem } from '../../interfaces';
import { FileViewerComponent, TemplateDirective } from '../../../shared';

@Component({
    selector: 'app-dynamic-screen',
    templateUrl: './dynamic-screen.component.html',
    styleUrls: ['./dynamic-screen.component.scss'],
})
export class DynamicScreenComponent implements OnDestroy, OnInit {

    NodeElements: Element[];
    SessElements: Element[];
    ConvElements: Element[];
    ConfElements: Element[];
    FlowElements: Element[];


    savePoints: string[];

    public backButton: IMenuItem;

    firstClickTime = Date.now();

    clickCount = 0;

    devClicks = 0;

    currentSelectedLogfilename: string;
    logFilenames: string[];

    logPlugin: IPlugin;

    showDevMenu = false;

    logsAvailable = false;

    keyCount = 0;

    savePointFileName: string;

    private displaySavePoints = false;

    private showUpdating = false;

    private currentStateActions: ActionMap[];

    private currentStateClass: string;

    private currentState: string;

    private stackTrace: string;

    private selected: string;

    private displayStackTrace = false;

    private dialogRef: MatDialogRef<IScreen>;

    private previousScreenType: string;

    private dialogOpening: boolean;

    private previousScreenName: string;

    private snackBarRef: MatSnackBarRef<SimpleSnackBar>;

    private personalized: boolean;

    private registered: boolean;

    private installedScreen: IScreen;

    private currentTemplateRef: ComponentRef<IScreen>;

    private installedTemplate: AbstractTemplate;

    private lastDialogType: string;

    public classes = '';

    private currentTheme: string;

    private disableDevMenu = false;

    @ViewChild(TemplateDirective) host: TemplateDirective;
    @ViewChild('myPanel') myPanel: MatExpansionPanel;
    matIcon = 'keyboard_arrow_down' || 'keyboard_arrow_up';

    screen: any;
    private lastSequenceNum: number;
    private lastScreenName: string;

    constructor(public screenService: ScreenService, public dialogService: DialogService, public session: SessionService,
        public deviceService: DeviceService, public dialog: MatDialog,
        public iconService: IconService, public snackBar: MatSnackBar, public overlayContainer: OverlayContainer,
        protected router: Router, private pluginService: PluginService,
        private fileUploadService: FileUploadService,
        private httpClient: HttpClient, private cd: ChangeDetectorRef,
        private elRef: ElementRef, public renderer: Renderer2
        /* private devTableService: DevTableService */) {
            if (Configuration.useTouchListener) {
                this.renderer.listen(elRef.nativeElement, 'touchstart', (event) => {
                    this.documentClick(event);
                });
            }
    }

    ngOnInit(): void {
        const self = this;

        this.session.subscribeForScreenUpdates((screen: any): void => self.updateTemplateAndScreen(screen));
        this.session.subscribeForDialogUpdates((dialog: any): void => self.updateDialog(dialog));
        this.updateDialog({ type: 'Startup', template: { type: 'Blank', dialog: true, dialogProperties: { width: '60%' } }});
        this.session.obsNode$.subscribe(NodeElements => this.NodeElements = NodeElements);
        this.session.obsSess$.subscribe(SessElements => this.SessElements = SessElements);
        this.session.obsConv$.subscribe(ConvElements => this.ConvElements = ConvElements);
        this.session.obsConf$.subscribe(ConfElements => this.ConfElements = ConfElements);
        this.session.obsFlow$.subscribe(FlowElements => this.FlowElements = FlowElements);
        this.session.obsState$.subscribe(currentState => this.currentState = currentState);
        this.session.obsStateClass$.subscribe(currentStateClass => this.currentStateClass = currentStateClass);
        this.session.obsStateActions$.subscribe(currentStateActions => this.currentStateActions = currentStateActions);
        this.session.obsSave$.subscribe(savePoints => this.savePoints = savePoints);

    }


    ngOnDestroy(): void {
        this.session.unsubscribe();
    }

    @HostListener('document:keydown', ['$event'])
    handleKeydownEvent(event: any) {
    const key = event.key;
    // console.log(key);
        if (key === 'ArrowUp' && this.keyCount !== 1) {
            this.keyCount = 1;
        } else if (key === 'ArrowUp' && this.keyCount === 1) {
            this.keyCount = 2;
        } else if (key === 'ArrowDown' && this.keyCount === 2) {
            this.keyCount = 3;
        } else if (key === 'ArrowDown' && this.keyCount === 3) {
            this.keyCount = 4;
        } else if (key === 'ArrowLeft' && this.keyCount === 4) {
            this.keyCount = 5;
        }   else if (key === 'ArrowRight' && this.keyCount === 5) {
            this.keyCount = 6;
        } else if (key === 'ArrowLeft' && this.keyCount === 6) {
            this.keyCount = 7;
        } else if (key === 'ArrowRight' && this.keyCount === 7) {
            this.keyCount = 8;
        } else if ((key === 'b' || key === 'B') && this.keyCount === 8) {
            this.keyCount = 9;
        } else if ((key === 'a' || key === 'A') && this.keyCount === 9) {
            this.onDevMenuClick();
            this.keyCount = 0;
        } else {
            this.keyCount = 0;
        }
        // if ((key === 'Shift' || key === 'Alt' || key === 'Option' || key === 'd' || key === 'D') && this.keyCount === 0) {
        //     this.keyCount = 1;
        // } else if ((key === 'Shift' || key === 'Alt' || key === 'Option' || key === 'd' || key === 'D') && this.keyCount === 1) {
        //     this.keyCount = 2;
        // } else if ((key === 'Shift' || key === 'Alt' || key === 'Option' || key === 'd' || key === 'D') && this.keyCount === 2) {
        //     this.keyCount = 0;
        //     this.onDevMenuClick();
        // } else {
        //     this.keyCount = 0;
        // }
    }

    // @HostListener('document:keyup', ['$event'])
    // handleKeyupEvent(event: any) {
    //     let key = event.key;
    //     if (key === 'Shift' || key === 'Alt' || key === 'Option' || key === 'd' || key === 'D') {
    //         this.keyCount = 0;
    //     }
    // }

    @HostListener('document:click', ['$event'])
    documentClick(event: any) {
        const screenWidth = window.innerWidth;
        const screenHeight = window.innerHeight;
        let x = event.clientX;
        let y = event.clientY;
        if (event.type === 'touchstart') {
            // console.log(event);
            x = event.changedTouches[0].pageX;
            y = event.changedTouches[0].pageY;
        }
        // console.log(`${screenWidth} ${x} ${y}`);
        if (this.clickCount === 0 || Date.now() - this.firstClickTime > 1000 ||
            (y > 100) || this.disableDevMenu) {
            this.firstClickTime = Date.now();
            this.clickCount = 0;
        }

        if (y < 100) {
            this.clickCount = ++this.clickCount;
        }

        if (y < 200 && x < 200 ) {
            this.devClicks = 1;
        } else if ( (y < 200 && x > screenWidth - 200) && (this.devClicks === 1 || this.devClicks === 2)) {
            this.devClicks = 2;
        } else if ( (y > screenHeight - 200 && x > screenWidth - 200) && (this.devClicks === 2 || this.devClicks === 3)) {
            this.devClicks = 3;
        } else if ( (y > screenHeight - 200 && x < 200) && this.devClicks === 3) {
            this.onDevMenuClick();
            this.devClicks = 0;
        } else {
            this.devClicks = 0;
        }

        // console.log(this.devClicks + " y="+y + ",x="+x+",h="+screenHeight+",w="+screenWidth);

    }

    protected onStackTraceClose() {
        this.displayStackTrace = false;
    }

    expandPannel() {
        this.myPanel.expanded = !this.myPanel.expanded;
      }

    protected onDevMenuClick(): void {
        if (!this.showDevMenu) {
            this.pluginService.getPlugin('openPOSCordovaLogPlugin').then(
                (plugin: IPlugin) => {
                    this.logPlugin = plugin;
                    if (this.logPlugin && this.logPlugin.impl) {
                        this.logsAvailable = true;
                        this.logPlugin.impl.listLogFiles('DESC',
                            (fileNames) => {
                                this.logFilenames = fileNames;
                            },
                            (error) => {
                                this.logFilenames = [];
                            }
                        );
                    } else {
                        this.logsAvailable = false;
                    }
                }
            ).catch(error => {
                this.logsAvailable = false;
            });
        }
        this.session.onAction('DevTools::Get');
        this.showDevMenu = !this.showDevMenu;
    }

    protected useSavePoints(): boolean {
        return Configuration.useSavePoints;
    }

    protected onDevMenuRefresh() {
        console.log('refreshing tools... ');
        this.displayStackTrace = false;
        this.currentState = 'Updating State... ';
        this.currentStateClass = 'Updating State...';
        this.showUpdating = true;
        this.currentStateActions = [];
        this.NodeElements = [];
        this.ConvElements = [];
        this.SessElements = [];
        this.ConfElements = [];
        this.FlowElements = [];
        setTimeout( () => {
            this.session.onAction('DevTools::Get');
            this.showUpdating = false;
            }, 500
        );
        this.onCreateSavePoint(null);

    }

    protected onNodeRemove(element: Element) {
        this.session.removeNodeElement(element);
    }

    protected onSessRemove(element: Element) {
        this.session.removeSessionElement(element);
    }

    protected onConvRemove(element: Element) {
        this.session.removeConversationElement(element);
    }

    protected onConfRemove(element: Element) {
        this.session.removeConfigElement(element);
    }

    protected onFlowRemove(element: Element) {
        this.session.removeFlowElement(element);
    }

    protected onStackTrace(element: Element) {
        this.displayStackTrace = true;
        this.selected = '\'' + element.ID + '\'';
        this.stackTrace = element.StackTrace;
    }

    protected onLoadSavePoint(savePoint: string) {
        if (this.savePoints.includes(savePoint)) {
            this.session.onAction('DevTools::Load::' + savePoint);
            console.log('Loaded Save Point: \'' + savePoint + '\'');
        } else {
            console.log('Unable to load Save Point: \'' + savePoint + '\'');
        }
    }

    protected onSimulateScan(value: string) {
        if (value) {
            this.session.onAction('DevTools::Scan', value);
        }
    }

    protected onCreateSavePoint(newSavePoint: string) {
        if (newSavePoint) {
            this.session.addSaveFile(newSavePoint);
        }
        if (!this.displaySavePoints && this.savePoints.length > 0) {
            this.displaySavePoints = true;
        }
    }

    protected onSavePointRemove(savePoint: string) {
        this.session.removeSaveFile(savePoint);
        if (this.savePoints.length === 0) {
            this.displaySavePoints = false;
        }
    }

    public onDevRefreshView() {
        this.session.refreshApp();
    }

    public onPersonalize() {
        this.session.dePersonalize();
        this.session.showScreen(this.session.getPersonalizationScreen());
    }

    public onDevClearLocalStorage() {
        localStorage.clear();
        this.session.refreshApp();
    }

    public onDevRestartNode(): Promise<{ success: boolean, message: string }> {

        const prom = new Promise<{ success: boolean, message: string }>((resolve, reject) => {
            const port = this.session.getServerPort();
            const nodeId = this.session.getNodeId().toString();
            const url = `${this.session.getServerBaseURL()}/register/restart/node/${nodeId}`;
            const httpClient = this.httpClient;
            httpClient.get(url).subscribe(response => {
                const msg = `Node '${nodeId}' restarted successfully.`;
                console.log(msg);
                resolve({ success: true, message: msg });
            },
                err => {
                    const msg = `Node restart Error occurred: ${JSON.stringify(err)}`;
                    const statusCode = err.status || (err.error ? err.error.status : null);
                    let errMsg = '';
                    if (err.error) {
                        if (err.error.error) {
                            errMsg += err.error.error;
                        }
                        if (err.error.message) {
                            errMsg += (errMsg ? '; ' : '') + err.error.message;
                        }
                    }
                    const returnMsg = `${statusCode ? statusCode + ': ' : ''}` +
                        (errMsg ? errMsg : 'Restart failed. Check client and server logs.');
                    reject({ success: false, message: returnMsg });
                });

        });
        return prom;
    }

    public onLogfileSelected(logFilename: string): void {
        this.currentSelectedLogfilename = logFilename;
    }

    public onLogfileShare(logFilename?: string): void {
        if (this.logPlugin && this.logPlugin.impl) {
            const targetFilename = logFilename || this.currentSelectedLogfilename;
            this.logPlugin.impl.shareLogFile(
                targetFilename,
                () => {
                },
                (error) => {
                    console.log(error);
                }
            );
        }
    }

    public onLogfileUpload(logFilename?: string): void {
        if (this.logPlugin && this.logPlugin.impl) {
            const targetFilename = logFilename || this.currentSelectedLogfilename;
            this.logPlugin.impl.getLogFilePath(
                targetFilename,
                (logfilePath) => {
                    this.fileUploadService.uploadLocalDeviceFileToServer('log', targetFilename, 'text/plain', logfilePath)
                        .then((result: { success: boolean, message: string }) => {
                            this.snackBar.open(result.message, 'Dismiss', {
                                duration: 8000, verticalPosition: 'top'
                            });
                        })
                        .catch((result: { success: boolean, message: string }) => {
                            this.snackBar.open(result.message, 'Dismiss', {
                                duration: 8000, verticalPosition: 'top'
                            });
                        });
                },
                (error) => {
                    console.log(error);
                }
            );
        }
    }

    public onLogfileView(logFilename?: string): void {
        if (this.logPlugin && this.logPlugin.impl) {
            const targetFilename = logFilename || this.currentSelectedLogfilename;
            this.logPlugin.impl.readLogFileContents(
                targetFilename,
                (logFileContents) => {
                    const dialogRef = this.dialog.open(FileViewerComponent, {
                        panelClass: 'full-screen-dialog',
                        maxWidth: '100vw', maxHeight: '100vh', width: '100vw'
                    });
                    dialogRef.componentInstance.fileName = targetFilename;
                    dialogRef.componentInstance.text = logFileContents;
                },
                (error) => {
                    console.log(error);
                }
            );
        }
    }

    public registerWithServer(): boolean {
        if (!this.registered && this.isPersonalized()) {
            console.log('initializing the application');
            this.session.unsubscribe();
            this.session.subscribe(this.normalizeAppIdFromUrl());
            this.registered = true;
        }
        return this.registered;
    }

    protected normalizeAppIdFromUrl(): string {
        let appId = this.router.url.substring(1);
        if (appId.indexOf('#') > 0) {
            appId = appId.substring(0, appId.indexOf('#'));
        }
        if (appId.indexOf('/') > 0) {
            appId = appId.substring(0, appId.indexOf('/'));
        }
        return appId;
    }

    protected isPersonalized(): boolean {
        if (!this.personalized && this.session.isPersonalized()) {
            this.personalized = true;
            console.log('already personalized.  setting needs personalization to false');
        }
        return this.personalized;
    }

    protected updateDialog(dialog?: any): void {
        // this.registerWithServer();
        if (dialog) {
            const dialogType = this.dialogService.hasDialog(dialog.subType) ? dialog.subType : 'Dialog';
            if (!this.dialogOpening) {
                if (this.dialogRef && (dialog.type !== this.lastDialogType || dialog.type === 'Dialog')) {
                    console.log('closing dialog');
                    this.dialogRef.close();
                    this.dialogRef = null;
                }
                console.log('opening dialog \'' + dialogType + '\'');
                this.dialogOpening = true;
                setTimeout(() => this.openDialog(dialog), 0);
            } else {
                console.log(`Not opening dialog! Here's why: dialogOpening? ${this.dialogOpening}`);
            }
        } else if (!dialog && this.dialogRef) {
            console.log('closing dialog ref');
            this.dialogRef.close();
            this.dialogRef = null;
        }
    }

    protected updateTemplateAndScreen(screen?: any): void {
        // this.registerWithServer();

        if (!this.isPersonalized() && !screen) {
            console.log('setting up the personalization screen');
            screen = this.session.getPersonalizationScreen();
        } else if (!screen) {
            screen = { type: 'Blank', template: { type: 'Blank', dialog: false } };
        }

        if (screen &&
            (screen.refreshAlways
                || screen.type !== this.previousScreenType
                || screen.name !== this.previousScreenName)
        ) {
            this.logSwitchScreens(screen);

            const templateName = screen.template.type;
            const screenType = screen.type;
            const screenName = screen.name;
            const templateComponentFactory: ComponentFactory<IScreen> = this.screenService.resolveScreen(templateName);
            const viewContainerRef = this.host.viewContainerRef;
            viewContainerRef.clear();
            if (this.currentTemplateRef) {
                this.currentTemplateRef.destroy();
            }
            this.currentTemplateRef = viewContainerRef.createComponent(templateComponentFactory);
            this.installedTemplate = this.currentTemplateRef.instance as AbstractTemplate;
            this.previousScreenType = screenType;
            this.previousScreenName = screenName;
            if (this.session.getTheme() !== this.currentTheme) {
                this.overlayContainer.getContainerElement().classList.remove(this.currentTheme);
                this.overlayContainer.getContainerElement().classList.add(this.session.getTheme());
                this.currentTheme = this.session.getTheme();
            }
            this.installedScreen = this.installedTemplate.installScreen(this.screenService.resolveScreen(screenType));
        }
        this.disableDevMenu = screen.template.disableDevMenu;
        this.installedTemplate.show(screen);
        this.installedScreen.show(screen, this, this.installedTemplate);

        this.backButton = screen.backButton;

        this.updateClasses(screen);

    }

    protected logSwitchScreens(screen: any) {
        let msg = `>>> Switching screens from "${this.previousScreenType}" to "${screen.type}"`;
        let nameLogged = false;
        let sequenceLogged = false;
        if (screen.name && screen.name !== screen.type) {
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

        console.log(msg);
    }

    protected updateClasses(screen: any) {
        if (screen) {
            this.classes = '';
            switch (this.session.getAppId()) {
                case 'pos':
                    if (screen.type === 'Home') {
                        this.classes = 'pos main-background';
                    } else {
                        this.classes = 'pos';
                    }
                    break;
                case 'selfcheckout':
                    if (screen.type === 'SelfCheckoutHome') {
                        this.classes = 'self-checkout-home selfcheckout';
                    } else {
                        this.classes = 'selfcheckout';
                    }
                    break;
                case 'customerdisplay':
                    this.classes = 'selfcheckout';
                    break;
            }
        }
    }

    protected openDialog(dialog: any) {
        const dialogComponentFactory: ComponentFactory<IScreen> = this.dialogService.resolveDialog(dialog.type);
        let closeable = false;
        let closeAction = null;
        if (dialog.template.dialogProperties) {
            closeable = dialog.template.dialogProperties.closeable;
            closeAction = dialog.template.dialogProperties.closeAction;
        }
        const dialogProperties: OpenPOSDialogConfig = { disableClose: !closeable, autoFocus: false };
        const dialogComponent = dialogComponentFactory.componentType;
        if (dialog.template.dialogProperties) {
            // Merge in any dialog properties provided on the screen
            for (const key in dialog.template.dialogProperties) {
                if (dialog.template.dialogProperties.hasOwnProperty(key)) {
                    dialogProperties[key] = dialog.template.dialogProperties[key];
                }
            }
            console.log(`Dialog options: ${JSON.stringify(dialogProperties)}`);
        }

        if (!this.dialogRef || dialog.type !== this.lastDialogType || dialog.type === 'Dialog'
            || dialog.refreshAlways) {
            this.dialogRef = this.dialog.open(dialogComponent, dialogProperties);
        } else {
            console.log(`Using previously created dialogRef. current dialog type: ${dialog.type}, last dialog type: ${this.lastDialogType}`);
        }

        this.dialogRef.componentInstance.show(dialog, this);
        this.dialogOpening = false;
        console.log('Dialog \'' + dialog.type + '\' opened');
        if (dialogProperties.executeActionBeforeClose) {
            // Some dialogs may need to execute the chosen action before
            // they close so that actionPayloads can be included with the action
            // before the dialog is destroyed.
            this.dialogRef.beforeClose().subscribe(result => {
                this.session.onAction(closeAction || result);
            });
        } else {
            this.dialogRef.afterClosed().subscribe(result => {
                this.session.onAction(closeAction || result);
            }
            );
        }

        this.lastDialogType = dialog.type;
    }

}
