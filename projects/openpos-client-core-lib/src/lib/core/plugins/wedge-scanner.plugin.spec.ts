import { TestBed } from '@angular/core/testing';
import { WedgeScannerPlugin } from './wedge-scanner.plugin';
import { SessionService } from '../services/session.service';
import { cold, getTestScheduler } from 'jasmine-marbles';
import { IScanData } from './scan.interface';

describe('WedgeScanner', () => {

    let sessionService: jasmine.SpyObj<SessionService>;
    let wedgeScannerPlugin: WedgeScannerPlugin;
    let config;
    let scanResult: IScanData;

    function dispatchEvent( key: string, ctrlKey: boolean, altKey: boolean ) {
        const event = new KeyboardEvent('keypress', {
            key,
            ctrlKey,
            altKey
        });
        document.dispatchEvent(event);
    }

    function setup() {
        config = {
            configType: 'WedgeScanner',
            startSequence: '*',
            endSequence: 'Enter',
            codeTypeLength: 1
        };
        const sessionSpy = jasmine.createSpyObj('SessionService', ['getMessages']);

        TestBed.configureTestingModule({
            providers: [
                WedgeScannerPlugin,
                { provide: SessionService, useValue: sessionSpy }
            ]
        });

        sessionService = TestBed.get(SessionService);

        sessionService.getMessages.and.returnValue(cold('--x', {x: config}));

        wedgeScannerPlugin = TestBed.get(WedgeScannerPlugin);


        wedgeScannerPlugin.startScanning().subscribe( s => scanResult = s);
    }

    it('should buffer barcode between * and Enter', async () => {
        setup();
        getTestScheduler().flush();
        dispatchEvent( '*', false, false );
        dispatchEvent( 'X', false, false );
        dispatchEvent( '1', false, false );
        dispatchEvent( '2', false, false );
        dispatchEvent( '3', false, false );
        dispatchEvent( '4', false, false );
        dispatchEvent( 'A', false, false );
        dispatchEvent( 'B', false, false );
        dispatchEvent( 'Enter', false, false );

        expect(scanResult.type).toEqual('X');
        expect(scanResult.data).toEqual('1234AB');
    });

    it('should buffer barcode between ctrl+b and ctrl+j', async () => {
        setup();

        config.startSequence = 'ctrl+b';
        config.endSequence = 'ctrl+j';

        getTestScheduler().flush();
        dispatchEvent( 'b', true, false );
        dispatchEvent( 'X', false, false );
        dispatchEvent( '1', false, false );
        dispatchEvent( '2', false, false );
        dispatchEvent( '3', false, false );
        dispatchEvent( '4', false, false );
        dispatchEvent( 'A', false, false );
        dispatchEvent( 'B', false, false );
        dispatchEvent( 'j', true, false );

        expect(scanResult.type).toEqual('X');
        expect(scanResult.data).toEqual('1234AB');
    });

    it('should replace special character codes with the correct special character', async () => {
        setup();

        getTestScheduler().flush();
        dispatchEvent( '*', true, false );
        dispatchEvent( 'X', false, false );
        dispatchEvent( '1', false, false );
        dispatchEvent( '2', false, false );
        dispatchEvent( '3', false, false );
        dispatchEvent( '4', false, false );
        dispatchEvent( 'A', false, false );
        dispatchEvent( 'B', false, false );
        dispatchEvent( '1', false, true);
        dispatchEvent( '0', false, true);
        dispatchEvent( '3', false, true);
        dispatchEvent( '0', false, true);
        dispatchEvent( 'Enter', true, false );

        expect(scanResult.type).toEqual('X');
        expect(scanResult.data).toEqual('1234AB' + String.fromCharCode(10) + String.fromCharCode(30));
    });
});
