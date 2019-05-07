import { Injectable, InjectionToken, Optional, Inject } from '@angular/core';
import { IScanner } from '../plugins/scanner.interface';
import { Observable, merge } from 'rxjs';

export const SCANNERS = new InjectionToken<IScanner[]>('Scanners');

@Injectable({
    providedIn: 'root',
  })
export class ScannerService {
    constructor( @Optional() @Inject(SCANNERS) private scanners: Array<IScanner>) {
    }

    public startScanning(): Observable<string> {
        return merge(...this.scanners.map(s => s.startScanning()));
    }

    public stopScanning() {
        this.scanners.forEach(s => s.stopScanning() );
    }
}
