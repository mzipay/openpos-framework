import { TestBed } from '@angular/core/testing';
import { cold, getTestScheduler } from 'jasmine-marbles';
import { SessionService, IAbstractScreen, AppInjector } from '../../core';
import { ScreenPartComponent, ScreenPart } from './screen-part';
import { Injector } from '@angular/core';

interface TestPartInterface extends IAbstractScreen {
    testProperty: string;
}

@ScreenPart({
    template: 'hi',
    name: 'TestPart'
})
class TestPartComponent extends ScreenPartComponent<TestPartInterface> {

    ctorWasCalled: boolean;
    constructor() {
        super();
        this.ctorWasCalled = true;
    }
    screenDataUpdated() {
    }
}

describe('ScreenPart', () => {
    let sut: TestPartComponent;
    let sessionService: jasmine.SpyObj<SessionService>;

    function setup() {
        const sessionServiceSpy = jasmine.createSpyObj('SessionService', ['getMessages']);
        TestBed.configureTestingModule({
            declarations: [ TestPartComponent ],
            providers: [
                { provide: SessionService, useValue: sessionServiceSpy },
            ]
        });
        AppInjector.Instance = TestBed.get(Injector);
        sessionService = TestBed.get(SessionService);
    }

    describe('constructor', () => {

        it('should set screenPartName', () => {
            setup();
            const testScreen = { };
            sessionService.getMessages.and.returnValue(cold('---x|', {x: testScreen}));
            sut = TestBed.createComponent(TestPartComponent).componentInstance;
            expect(sut.screenPartName).toBe('TestPart');
        });
        it('should preserve and call the constructor', () => {
            setup();
            const testScreen = { };
            sessionService.getMessages.and.returnValue(cold('---x|', {x: testScreen}));
            sut = TestBed.createComponent(TestPartComponent).componentInstance;
            expect(sut.ctorWasCalled).toBeTruthy();
        });
    });

    describe('getMessageSubscription', () => {

        it('should get the part data from the TestPart object on the screen', () => {
            setup();
            const testScreen = {
                screenType: 'Test',
                TestPart: {
                    testProperty: 'Yay'
                }
            };
            sessionService.getMessages.and.returnValue(cold('---x|', {x: testScreen}));
            const fixture = TestBed.createComponent(TestPartComponent);
            sut = fixture.componentInstance;
            fixture.detectChanges();
            getTestScheduler().flush();
            expect(sut.screenData.testProperty).toBe('Yay');
        });

        it('should get the part data off screen', () => {
            setup();
            const testScreen = {
                screenType: 'Test',
                testProperty: 'boo'
            };

            sessionService.getMessages.and.returnValue(cold('---x|', {x: testScreen}));
            const fixture = TestBed.createComponent(TestPartComponent);
            sut = fixture.componentInstance;
            fixture.detectChanges();
            getTestScheduler().flush();
            expect(sut.screenData.testProperty).toBe('boo');
        });

        it('should ignore Loading Screens', () => {
            setup();
            const testScreen = {
                screenType: 'Loading',
                testProperty: 'boo'
            };

            sessionService.getMessages.and.returnValue(cold('---x|', {x: testScreen}));
            sut = TestBed.createComponent(TestPartComponent).componentInstance;
            getTestScheduler().flush();
            expect(sut.screenData).toBeFalsy();
        });
    });

});
