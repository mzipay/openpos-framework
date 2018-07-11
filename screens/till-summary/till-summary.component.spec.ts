import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TillSummaryComponent } from './till-summary.component';

describe('TillSummaryComponent', () => {
  let component: TillSummaryComponent;
  let fixture: ComponentFixture<TillSummaryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TillSummaryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TillSummaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
