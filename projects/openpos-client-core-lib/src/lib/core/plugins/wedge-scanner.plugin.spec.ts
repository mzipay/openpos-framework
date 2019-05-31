import { TestBed } from '@angular/core/testing';
import { WedgeScannerPlugin } from './wedge-scanner.plugin';
import { SessionService } from '../services/session.service';
import { cold, getTestScheduler } from 'jasmine-marbles';
import { IScanData } from './scan.interface';
import { DomEventManager } from '../services/dom-event-mangaer.service';
import { Subscription } from 'rxjs';

describe('WedgeScanner', () => {

    const config = {
        configType: 'WedgeScanner',
        startSequence: '*',
        endSequence: 'Enter',
        codeTypeLength: 1
    };

    let scanResult: IScanData;
    let keyDownEvents: KeyboardEvent[];
    let subscription: Subscription;

    function queueEvent( key: string, ctrlKey: boolean, altKey: boolean ) {
        const event = new KeyboardEvent('keydown', {
            key,
            ctrlKey,
            altKey,
        });

        keyDownEvents.push(event);
    }

    function makeEventCold() {
        let marbles = '----';
        const values = {};

        keyDownEvents.forEach( ( e, index ) => {
            marbles = marbles.concat(String.fromCharCode(index + 97) + '-');
            values[String.fromCharCode(index + 97)] = e;
        });

        return cold(marbles, values);
    }

    function getConfig() {
        return cold('-x', {x: config});
    }

    function setup() {
        let sessionService: jasmine.SpyObj<SessionService>;
        let domEventManager: jasmine.SpyObj<DomEventManager>;
        let wedgeScannerPlugin: WedgeScannerPlugin;
        const sessionSpy = jasmine.createSpyObj('SessionService', ['getMessages']);
        const domEventManagerSpy = jasmine.createSpyObj('DomEventManager', ['createEventObserver']);

        TestBed.configureTestingModule({
            providers: [
                WedgeScannerPlugin,
                { provide: SessionService, useValue: sessionSpy },
                { provide: DomEventManager, useValue: domEventManagerSpy}
            ]
        });

        sessionService = TestBed.get(SessionService);
        sessionService.getMessages.and.callFake(getConfig);

        domEventManager = TestBed.get(DomEventManager);
        domEventManager.createEventObserver.and.callFake(makeEventCold);

        wedgeScannerPlugin = TestBed.get(WedgeScannerPlugin);
        subscription = wedgeScannerPlugin.startScanning().subscribe( s => scanResult = s);
    }

    beforeEach( () => {
        scanResult = null;
        keyDownEvents = [];
        TestBed.resetTestingModule();
        if ( subscription ) {
            subscription.unsubscribe();
        }
    });

    it('should replace special character codes with the correct special character', () => {

        config.startSequence = '*';
        config.endSequence = 'Enter';

        queueEvent( '*', false, false );
        queueEvent( 'X', false, false );
        queueEvent( '1', false, false );
        queueEvent( '2', false, false );
        queueEvent( '3', false, false );
        queueEvent( '4', false, false );
        queueEvent( 'A', false, false );
        queueEvent( 'B', false, false );
        queueEvent( '1', false, true);
        queueEvent( '0', false, true);
        queueEvent( '3', false, true);
        queueEvent( '0', false, true);
        queueEvent( 'Enter', false, false );

        setup();

        getTestScheduler().flush();

        expect(scanResult.type).toEqual('X');
        expect(scanResult.data).toEqual('1234AB' + String.fromCharCode(10) + String.fromCharCode(30));
    });

    it('should buffer barcode between * and Enter', () => {

        config.startSequence = '*';
        config.endSequence = 'Enter';

        queueEvent( '*', false, false );
        queueEvent( 'X', false, false );
        queueEvent( '1', false, false );
        queueEvent( '2', false, false );
        queueEvent( '3', false, false );
        queueEvent( '4', false, false );
        queueEvent( 'A', false, false );
        queueEvent( 'B', false, false );
        queueEvent( 'Enter', false, false );

        setup();

        getTestScheduler().flush();

        expect(scanResult.type).toEqual('X');
        expect(scanResult.data).toEqual('1234AB');
    });

    it('should buffer barcode between ctrl+b and ctrl+j', () => {


        config.startSequence = 'ctrl+b';
        config.endSequence = 'ctrl+j';

        queueEvent( 'b', true, false );
        queueEvent( 'X', false, false );
        queueEvent( '1', false, false );
        queueEvent( '2', false, false );
        queueEvent( '3', false, false );
        queueEvent( '4', false, false );
        queueEvent( 'Z', false, false );
        queueEvent( 'B', false, false );
        queueEvent( 'j', true, false );

        setup();

        getTestScheduler().flush();

        expect(scanResult.type).toEqual('X');
        expect(scanResult.data).toEqual('1234ZB');
    });

});
