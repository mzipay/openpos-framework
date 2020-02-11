import {Injectable} from "@angular/core";
import {MessageTypes} from "../../../core/messages/message-types";
import {filter} from "rxjs/operators";
import {SessionService} from "../../../core/services/session.service";

@Injectable({
    providedIn: "root",})
export class ScanOrSearchProvider {

    private keypressAutoFocusPattern: string;

    constructor( sessionService:SessionService) {
        sessionService.getMessages(MessageTypes.CONFIG_CHANGED).pipe(
            filter( m => m.configType === 'ScanOrSearch')
        ).subscribe( m => {
            if ( m.keypressAutoFocusPattern ) {
                this.keypressAutoFocusPattern = m.keypressAutoFocusPattern;
            }
        });
    }

    getKeyPressAutofocusPattern() : string {
        return this.keypressAutoFocusPattern;
    }
}