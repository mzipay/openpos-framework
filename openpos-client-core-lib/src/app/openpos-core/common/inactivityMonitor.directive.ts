import { Directive, HostListener, Input, Output, EventEmitter } from '@angular/core';

@Directive({
    selector: '[InactivityMonitor]'
})
export class InactivityMonitorDirective {
    constructor() {
    }  

    @Input()
    set InactivityMonitor(timeoutActive: boolean){
        this._timeoutActive = timeoutActive;
        if( timeoutActive ) {
            this.resetTimeout();
        } else if( this.timerHandle ) {
            clearTimeout( this.timerHandle );
        }
    }
    get InactivityMonitor(): boolean {
        return this._timeoutActive;
    }

    @Input() timeOut: number = 30000;

    @Output() inactivityTimedOut: EventEmitter<boolean> = new EventEmitter<boolean>();

    private timerHandle: any;
    private _timeoutActive

    @HostListener('window:keydown', ['$event'])
    keyEvent(event: KeyboardEvent) {
        this.resetTimeout();
    }

    @HostListener('window:click', ['$event'])
    mouseEvent(event: MouseEvent) {
        this.resetTimeout();
    }

    @HostListener('window:touchstart', ['$event'])
    touchEvent(event: TouchEvent) {
        this.resetTimeout();
    }

    private resetTimeout(){
        if( this.timerHandle ){
            clearTimeout( this.timerHandle );
        }
        this.timerHandle = setTimeout( () => this.inactivityTimedOut.emit(true), this.timeOut );
    }
}
