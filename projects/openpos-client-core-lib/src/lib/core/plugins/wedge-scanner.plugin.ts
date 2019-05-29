import { IScanner } from './scanner.interface';
import { Injectable } from '@angular/core';
import { Observable, fromEvent } from 'rxjs';
import { map, filter, bufferToggle } from 'rxjs/operators';
import { IScanData } from './scan.interface';
import { SessionService } from '../services/session.service';

interface ControlSequence { modifiers: string[]; key: string; }

@Injectable({
    providedIn: 'root'
  })
  export class WedgeScannerPlugin implements IScanner {

    private startSequence = '*';
    private endSequence = 'Enter';
    private codeTypeLength = 0;
    private scannerActive: boolean;
    private startSequenceObj = this.getControlStrings(this.startSequence);
    private endSequenceObj = this.getControlStrings(this.endSequence);

    constructor( sessionService: SessionService ) {

        sessionService.getMessages('ConfigChanged').pipe(
            filter( m => m.configType === 'WedgeScanner')
        ).subscribe( m => {
            if ( m.startSequence ) {
                this.startSequence = m.startSequence;
                this.startSequenceObj = this.getControlStrings(this.startSequence);
            }
            if ( m.endSequence ) {
                this.endSequence = m.endSequence;
                this.endSequenceObj = this.getControlStrings(this.endSequence);
            }
            if ( m.codeTypeLength ) {
                this.codeTypeLength = m.codeTypeLength;
            }
        });
    }

    startScanning(): Observable<IScanData> {
        this.scannerActive = true;


        const startScanBuffer = fromEvent(document, 'keypress').pipe(
            filter( (e: KeyboardEvent) => this.filterForControlSequence(this.startSequenceObj, e)));

        const stopScanBuffer = fromEvent(document, 'keypress').pipe(
            filter((e: KeyboardEvent) => this.filterForControlSequence(this.endSequenceObj, e)));

        return fromEvent(document, 'keypress').pipe(
            bufferToggle(
                    startScanBuffer,
                    () => stopScanBuffer
            ),
            // Make sure we get a full buffer and didn't timout
            filter( (s: KeyboardEvent[]) => this.filterForControlSequence(this.startSequenceObj, s[0]) &&
                                            this.filterForControlSequence(this.endSequenceObj, s[s.length - 1])),
            map( events => this.convertKeyEventsToChars(events) ),
            // Join the buffer into a string and remove the start and stop characters
            map( (s) => s.join('').slice(1, s.length - 1)),
            map( (s: string) => this.getScanData(s)),
        );
    }

    stopScanning() {
        this.scannerActive = false;
    }

    private getControlStrings( sequence: string): ControlSequence {
        let modifiers: string[];
        let key: string;

        if ( sequence.includes('+')) {
            modifiers = sequence.split('+');
            modifiers = modifiers.slice(0, modifiers.length - 1 );
            key = sequence.slice(sequence.lastIndexOf('+') + 1)[0];
        } else {
            key = sequence;
        }

        return { modifiers, key};
    }

    private filterForControlSequence( sequence: ControlSequence, e: KeyboardEvent ): boolean {
        if ( !this.scannerActive ) {
            return false;
        }
        const keyPressed = e.key === sequence.key;
        if ( !!sequence.modifiers ) {
            const modifiersPressed = sequence.modifiers.map( m => this.checkModifier(e, m)).reduce((accum, m) => accum && m );
            return modifiersPressed && keyPressed;
        }
        return keyPressed;
    }

    private checkModifier( e: KeyboardEvent, modifier: string): boolean {
        switch (modifier) {
            case 'ctrl':
                return e.ctrlKey;
            case 'alt':
                return e.altKey;
        }
    }

    private convertKeyEventsToChars( events: KeyboardEvent[] ): string[] {
        // We need to look for 2 character sequences with the alt key pressed and convert them into the
        // special characters they represent
        // Mac gives us the individual key for special characters ie (3 and then 0 with alt held down) for return
        // Where as windows gives us 30;
        const charList = [];
        for ( let i = 0; i < events.length; i++ ) {
            const e = events[i];

            if ( e.altKey && i < events.length - 1 ) {
                // get the next number
                const e2 = events[i + 1];

                // convert the char code into a string
                charList.push(String.fromCharCode( parseInt(e.key + e2.key, 10) ));

                // skip the next value since we already accounted for it.
                i++;
            } else {
                charList.push(e.key);
            }
        }

        return charList;
    }

    private getScanData( s: string ): IScanData {
        return { type: s.slice(0, this.codeTypeLength), data: s.slice(this.codeTypeLength)};
    }

}
