import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BlankComponent } from './blank.component';
import { AppInjector } from '../../../core/app-injector';
import { Injector } from '@angular/core';
import { SessionService } from '../../../core/services/session.service';

describe('BlankComponent', () => {
  let component: BlankComponent;
  let fixture: ComponentFixture<BlankComponent>;

  beforeEach((() => {

    const sessionServiceSpy = jasmine.createSpyObj('SessionService', ['getMessages']);
    TestBed.configureTestingModule({
      declarations: [ BlankComponent ],
      providers: [
        { provide: SessionService, useValue: sessionServiceSpy },
    ]
    });

    AppInjector.Instance = TestBed.get(Injector);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BlankComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
