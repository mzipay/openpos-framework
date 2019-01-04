import { Component, TemplateRef, QueryList, ContentChildren, Input, AfterContentInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';

@Component({
    selector: 'app-pager',
    templateUrl: './pager.component.html',
    styleUrls: ['./pager.component.scss']
})

export class PagerComponent implements AfterContentInit, OnDestroy {
    @ContentChildren('pagerItem') sections: QueryList<TemplateRef<any>>;

    @Input() pageSize = 5;
    currentPage = 1;
    totalPages: number;
    currentIndex = 0;
    private subscription: Subscription;

    ngAfterContentInit(): void {
        this._resetPageState();
        if (this.sections) {
            this.subscription = this.sections.changes.subscribe( () => {
                    this._resetPageState();
            });
        }
    }

    ngOnDestroy(): void {
        if (this.subscription) {
            this.subscription.unsubscribe();
        }
    }

    private _resetPageState(): void {
        this.currentPage = 1;
        this.currentIndex = 0;
        this.totalPages = Math.ceil(this.sections.length / this.pageSize);
    }

    incrementPage() {
        if (this.currentPage !== this.totalPages) {
            this.currentIndex += this.pageSize;
            this.currentPage++;
        }
    }

    decrementPage() {
        if (this.currentPage !== 1) {
            this.currentIndex -= this.pageSize;
            this.currentPage--;
        }
    }
}
