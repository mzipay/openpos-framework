import { Injectable } from '@angular/core';
import { SessionService } from '../../core/services/session.service';
import { Observable } from 'rxjs';

@Injectable()
export class MessageProvider {

    private messages$: Observable<any>;
    private messageType: string;

    constructor( private sessionService: SessionService ) {

    }

    setMessageType( messageType: string ) {
        this.messages$ = this.sessionService.getMessages( messageType );
        this.messageType = messageType;
    }

    getMessages$(): Observable<any> {
        return this.messages$;
    }
}
