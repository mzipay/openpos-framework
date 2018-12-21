import { TestBed } from '@angular/core/testing';
import { SessionService } from './session.service';
import { MatSnackBar } from '@angular/material';
import { ToastService } from './toast.service';
import { of } from 'rxjs';
import { IToastScreen, ToastType, AppInjector } from '../../core';
import { Injector } from '@angular/core';


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
        const sessionSpy = jasmine.createSpyObj('SessionService', ['getMessages', 'cancelLoading']);
        const matSnackSpy = jasmine.createSpyObj('MatSnackBar', ['open']);

        TestBed.configureTestingModule({
            providers: [

                { provide: SessionService, useValue: sessionSpy },
                { provide: MatSnackBar, useValue: matSnackSpy },
                ToastService,
            ]
        });

        AppInjector.Instance = TestBed.get(Injector);
        sessionServiceSpy = TestBed.get(SessionService);
        sessionServiceSpy.getMessages.and.returnValue(of(testToast));

        matSnackBarSpy = TestBed.get(MatSnackBar);

        toastService = TestBed.get(ToastService);

    });

    describe('constructor', () => {
        it('should call MatSnackBar.open when called', () => {
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
