import { ScreenComponent } from './screen.decorator';
import { PosScreen } from '../../screens-deprecated';
import { Component } from '@angular/core';
import { IAbstractScreen } from '../../core/interfaces/abstract-screen.interface';
import { TestBed } from '@angular/core/testing';
import { Registry } from '../../core/registry';

interface ITestScreenInterface extends IAbstractScreen {
    testProperty: string;
}

@ScreenComponent({
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

describe('ScreenDecorator', () => {
    let sut: TestScreenComponent;
    function setup() {
        TestBed.configureTestingModule({
            declarations: [ Registry.getComponents('Test', Registry.screens) ],
        });
        const fixture = TestBed.createComponent(TestScreenComponent);
        sut = fixture.componentInstance;
    }

    it('should set register the screen with the test module', () => {
        setup();
        expect(sut).toBeTruthy();
    });

});
