import { IScanner } from './scanner.interface';
import { Injectable } from '@angular/core';
import { Observable, fromEvent, merge, of } from 'rxjs';
import { map, filter, bufferToggle, timeout, catchError } from 'rxjs/operators';

@Injectable({
    providedIn: 'root'
  })
  export class WedgeScannerPlugin implements IScanner {

    private startChar = '*';
    private endChar = 'Enter';
    private bufferTimeout = 100;
    private scannerActive: boolean;

    startScanning(): Observable<string> {
        this.scannerActive = true;

        const startScanBuffer = fromEvent(document, 'keydown').pipe(
            filter( (e: KeyboardEvent) => this.scannerActive && this.detectedStart(e) ));

        const stopScanBuffer = fromEvent(document, 'keydown').pipe(
            filter((e: KeyboardEvent) => !this.scannerActive || this.detectedEnd(e)));

        const timoutScanBuffer = startScanBuffer.pipe(
            timeout(this.bufferTimeout),
            catchError( e => of(true))
        );

        return fromEvent(document, 'keydown').pipe(
            map( (e: KeyboardEvent) => e.key ),
            filter( key => key !== 'Shift'),
            bufferToggle(
                    startScanBuffer,
                    () => merge( stopScanBuffer, timoutScanBuffer)
            ),
            filter( s => this.checkBuffer(s)),
            // Join the buffer into a string and remove the start and stop characters
            map( (s) => this.publish(s))
        );
    }

    detectedStart(e: KeyboardEvent): boolean {
        try {
        console.log(e);
        } catch (er) {
            console.log(er);
        }
        return e.key === this.startChar;
    }

    detectedEnd(e: KeyboardEvent): boolean {
        return e.key === this.endChar;
    }

    publish(s: string[]): string {
        const f =  s.join('').slice(1, s.length - 1);
        return f.replace(/Clear|Alt0010/g, '\r\n').replace(/Alt0030|Alt0013/g, '');
    }

    checkBuffer(s: string[]): boolean {
        const f = s[0] === this.startChar && s[s.length - 1] === this.endChar;
        console.log(s);
        return f;
    }

    stopScanning() {
        this.scannerActive = false;
    }


}
