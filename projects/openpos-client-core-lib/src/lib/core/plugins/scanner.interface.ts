import { Observable } from 'rxjs';

export interface IScanner {
    /**
     * Tell the scanner to start scanning
     * returns true if successfull started; false if start fails
     */
    startScanning(): Observable<string>;

    /**
     * Tell the scanner to stop scanning
     * returns true if successful stop; false if stop fails
     */
    stopScanning();
}
