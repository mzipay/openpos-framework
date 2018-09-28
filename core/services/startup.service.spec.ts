import { TestBed } from '@angular/core/testing';
import { StartupService, STARTUP_TASKS } from './startup.service';
import { MatDialog } from '@angular/material';
import { IStartupTask } from '../interfaces';
import { Observable, of } from 'rxjs';
import { scan } from 'rxjs/operators';

describe('StartupService', () => {

    // Test tasks
    class TestTask1 implements IStartupTask {
        name = 'Test1';        order = 100;
        execute(): Observable<string> { return of('Test1'); }
    }

    class TestTask2 implements IStartupTask {
        name = 'Test2';        order = 1;
        execute(): Observable<string> { return of('Test2'); }
    }

    class TestTask3 implements IStartupTask {
        name = 'Test1';        order = 11;
        execute(): Observable<string> { return of('Test3'); }
    }

    let startupService: StartupService;

    beforeEach(() => {
        const matDialogSpy = jasmine.createSpyObj('MatDialog', ['open']);

        TestBed.configureTestingModule({
            providers: [
                StartupService,
                { provide: MatDialog, useValue: matDialogSpy },
                { provide: STARTUP_TASKS, useClass: TestTask1, multi: true },
                { provide: STARTUP_TASKS, useClass: TestTask2, multi: true },
                { provide: STARTUP_TASKS, useClass: TestTask3, multi: true }
            ]
        });

        startupService = TestBed.get(StartupService);
    });

    describe('canActivate', () => {
        it('should run all deduped tasks in order', async () => {
            let resultMessages = '';
            startupService.startupTaskMessages$.pipe(
                scan((acc, curr) => acc + curr, ''))
                .subscribe((messages) => resultMessages = messages);

            const result = await startupService.canActivate( null, null).toPromise();

            expect(result).toBeTruthy();
            expect(resultMessages).toBe('Test2Test3');
        });
    });

});
