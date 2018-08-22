import { TestBed } from '@angular/core/testing';
import { SessionService } from './session.service';
import { PersonalizationService } from './personalization.service';
import { NgZone } from '@angular/core';
import { MatDialog } from '@angular/material';
import { StompRService } from '@stomp/ng2-stompjs';


describe('SessionService', () => {

    let stompServiceSpy: jasmine.SpyObj<StompRService>;
    let sessionService: SessionService;

    beforeEach(() => {
        const stompSpy = jasmine.createSpyObj('StompRService', ['publish']);
        const personalizationSpy = jasmine.createSpyObj('PersonalizationService', ['getNodeId']);
        const matDialogSpy = jasmine.createSpyObj('MatDialog', ['open']);

        TestBed.configureTestingModule({
            providers: [
                { provide: PersonalizationService, useValue: personalizationSpy },
                SessionService,
                { provide: MatDialog, useValue: matDialogSpy },
                {provide: StompRService, useValue: stompSpy }
            ]
        });

        stompServiceSpy = TestBed.get(StompRService);
        sessionService = TestBed.get(SessionService);
    });

    describe('onAction', () => {
        it('should call StompService.publish when called', () => {
            sessionService.onAction('foo', 'bar');
            expect(stompServiceSpy.publish.calls.count).toBeTruthy();
        });
    });

});
