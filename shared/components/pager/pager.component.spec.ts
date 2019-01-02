import { TestBed, ComponentFixture, tick, fakeAsync } from '@angular/core/testing';
import { PagerComponent } from './pager.component';
import { Component, Input, QueryList } from '@angular/core';

describe('PagerComponent', () => {

    let pagerComponent: PagerComponent;
    let fixture: ComponentFixture<PagerComponent>;

    beforeEach( () => {
        TestBed.configureTestingModule({
            declarations: [
                PagerComponent,
                MockIconComponent,
            ]
        }).compileComponents();

        fixture = TestBed.createComponent(PagerComponent);
        pagerComponent = fixture.componentInstance;
    });

    describe('constructor', () => {
        it('should have default page state when intially created', () => {
            expect(pagerComponent.pageSize).toBe(5);
            expect(pagerComponent.currentPage).toBe(1);
            expect(pagerComponent.currentIndex).toBe(0);
            expect(pagerComponent.totalPages).toBeUndefined();
            expect(pagerComponent.refreshOnContentChange).toBeUndefined();
        });
    });

    describe('incrementPage', () => {
        it('should not increment page when only 1 page', () => {
            pagerComponent.totalPages = 1;
            pagerComponent.incrementPage();
            expect(pagerComponent.currentIndex).toBe(0);
            expect(pagerComponent.currentPage).toBe(1);
        });

        it('should increment page count and index by 1 when multiple pages', () => {
            pagerComponent.totalPages = 5;
            pagerComponent.incrementPage();
            expect(pagerComponent.currentIndex).toBe(5);
            expect(pagerComponent.currentPage).toBe(2);
            pagerComponent.incrementPage();
            expect(pagerComponent.currentIndex).toBe(10);
            expect(pagerComponent.currentPage).toBe(3);
        });

        it('should not increment page count when page count at last page', () => {
            pagerComponent.totalPages = 5;
            pagerComponent.currentIndex = 20;
            pagerComponent.currentPage = 5;
            pagerComponent.incrementPage();
            expect(pagerComponent.currentIndex).toBe(20);
            expect(pagerComponent.currentPage).toBe(5);
        });
    });

    describe('decrementPage', () => {
        it('should not decrement page when only 1 page', () => {
            pagerComponent.totalPages = 1;
            pagerComponent.decrementPage();
            expect(pagerComponent.currentIndex).toBe(0);
            expect(pagerComponent.currentPage).toBe(1);
        });

        it('should decrement page count and index by 1 when multiple pages', () => {
            pagerComponent.totalPages = 5;
            pagerComponent.currentIndex = 15;
            pagerComponent.currentPage = 3;
            pagerComponent.decrementPage();
            expect(pagerComponent.currentIndex).toBe(10);
            expect(pagerComponent.currentPage).toBe(2);
            pagerComponent.decrementPage();
            expect(pagerComponent.currentIndex).toBe(5);
            expect(pagerComponent.currentPage).toBe(1);
        });

        it('should not decrement page count when page count at first page', () => {
            pagerComponent.totalPages = 5;
            pagerComponent.currentIndex = 0;
            pagerComponent.currentPage = 1;
            pagerComponent.decrementPage();
            expect(pagerComponent.currentIndex).toBe(0);
            expect(pagerComponent.currentPage).toBe(1);
        });
    });

    describe('ngAfterContentInit', () => {

        beforeEach( () => {
            pagerComponent.sections = new QueryList();
        });

        it('should reset the page state', () => {
            pagerComponent.currentIndex = 10;
            pagerComponent.currentPage = 2;
            pagerComponent.ngAfterContentInit();
            expect(pagerComponent.currentIndex).toBe(0);
            expect(pagerComponent.currentPage).toBe(1);
        });

        it('should reset state after sections change when refreshOnContentChange is true', fakeAsync(() => {
            fixture.detectChanges();
            pagerComponent.refreshOnContentChange = true;
            pagerComponent.ngAfterContentInit();
            pagerComponent.currentIndex = 10;
            pagerComponent.currentPage = 2;
            pagerComponent.sections.notifyOnChanges();
            tick();
            expect(pagerComponent.currentIndex).toBe(0);
            expect(pagerComponent.currentPage).toBe(1);
        }));

        it('should not reset state after sections change when refreshOnContentChange is false', fakeAsync(() => {
            fixture.detectChanges();
            pagerComponent.refreshOnContentChange = false;
            pagerComponent.ngAfterContentInit();
            pagerComponent.currentIndex = 10;
            pagerComponent.currentPage = 2;
            pagerComponent.sections.notifyOnChanges();
            tick();
            expect(pagerComponent.currentIndex).toBe(10);
            expect(pagerComponent.currentPage).toBe(2);
        }));
    });
});

@Component({
    selector: 'app-icon',
    template: '',
})
class MockIconComponent {
    @Input() iconName: string;
}