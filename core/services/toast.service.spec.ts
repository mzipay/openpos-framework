import { StompService } from '@stomp/ng2-stompjs';
import { TestBed } from '@angular/core/testing';
import { SessionService } from './session.service';
import { PersonalizationService } from './personalization.service';
import { NgZone } from '@angular/core';
import { MatSnackBar } from '@angular/material';
import { ToastService } from './toast.service';
import { of } from 'rxjs';
import { IToastScreen, ToastType } from '..';


describe('ToastService', () => {

    let sessionServiceSpy: jasmine.SpyObj<SessionService>;
    let matSnackBarSpy: jasmine.SpyObj<MatSnackBar>;

    let toastService: ToastService;
    const testToast: IToastScreen = {
        locale: 'en-us',
        name: 'Toast',
        screenType: 'Toast',
        message: 'Hi',
        duration: 2500,
        toastType: ToastType.Success
    };

    beforeEach(() => {
        const sessionSpy = jasmine.createSpyObj('SessionService', ['getMessages']);
        const matSnackSpy = jasmine.createSpyObj('MatSnackBar', ['open']);

        TestBed.configureTestingModule({
            providers: [
                ToastService,
                { provide: MatSnackBar, useValue: matSnackSpy },
                { provide: SessionService, useValue: sessionSpy }
            ]
        });

        sessionServiceSpy = TestBed.get(SessionService);
        sessionServiceSpy.getMessages.and.returnValue(of(testToast));

        matSnackBarSpy = TestBed.get(MatSnackBar);

        toastService = TestBed.get(ToastService);

    });

    describe('constructor', () => {
        it('should call StompService.publish when called', () => {
            expect(matSnackBarSpy.open.calls.count).toBeTruthy();
            expect(matSnackBarSpy.open).toHaveBeenCalledWith(
                    testToast.message,
                    null,
                    {
                        duration: 2500,
                        panelClass: 'toast-success'
                    }
                );
        });
    });

});
