import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ScanSomethingComponent } from './scan-something.component';

describe('ScanSomethingComponent', () => {
  let component: ScanSomethingComponent;
  let fixture: ComponentFixture<ScanSomethingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ScanSomethingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ScanSomethingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });

  describe('onBarcodeKeydown', () => {
    it('should filter out e E + - and . and allow numbers', () => {
      const event = new KeyboardEvent( 'e');
      const result = component.onBarcodeKeydown(event);
      expect(result).toBeFalsy();
    });
  });

});
