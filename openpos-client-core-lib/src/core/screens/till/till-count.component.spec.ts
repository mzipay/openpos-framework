import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TillCountComponent } from './till-count.component';

describe('TillCountComponent', () => {
  let component: TillCountComponent;
  let fixture: ComponentFixture<TillCountComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TillCountComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TillCountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
