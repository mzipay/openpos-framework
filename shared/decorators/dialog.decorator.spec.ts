import { PosScreen } from '../../screens-deprecated';
import { Component } from '@angular/core';
import { IAbstractScreen } from '../../core/interfaces/abstract-screen.interface';
import { TestBed } from '@angular/core/testing';
import { Registry } from '../../core/registry';
import { DialogComponent } from './dialog.decorator';

interface ITestScreenInterface extends IAbstractScreen {
    testProperty: string;
}

@DialogComponent({
    name: 'TestPart',
    moduleName: 'Test'
})
@Component({
    template: 'hi',
})
class TestScreenComponent extends PosScreen<ITestScreenInterface> {
    buildScreen() {
    }
}

describe('DialogDecorator', () => {
    let sut: TestScreenComponent;
    function setup() {
        TestBed.configureTestingModule({
            declarations: [ Registry.getComponents('Test', Registry.dialogs) ],
        });
        const fixture = TestBed.createComponent(TestScreenComponent);
        sut = fixture.componentInstance;
    }

    it('should set register the screen with the test module', () => {
        setup();
        expect(sut).toBeTruthy();
    });

});
