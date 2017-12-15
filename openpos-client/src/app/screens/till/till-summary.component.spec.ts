import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TillReconcileComponent } from './till-reconcile.component';

describe('TillReconcileComponent', () => {
  let component: TillReconcileComponent;
  let fixture: ComponentFixture<TillReconcileComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TillReconcileComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TillReconcileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
