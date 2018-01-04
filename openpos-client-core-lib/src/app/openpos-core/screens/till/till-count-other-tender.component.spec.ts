import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TillCountOtherTenderComponent } from './till-count-other-tender.component';

describe('TillCountOtherTenderComponent', () => {
  let component: TillCountOtherTenderComponent;
  let fixture: ComponentFixture<TillCountOtherTenderComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TillCountOtherTenderComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TillCountOtherTenderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
