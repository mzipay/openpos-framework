import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DynamicFormControlComponent } from './dynamic-form-control.component';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

describe('DynamicFormControlComponent', () => {
  let component: DynamicFormControlComponent;
  let fixture: ComponentFixture<DynamicFormControlComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DynamicFormControlComponent ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DynamicFormControlComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});

