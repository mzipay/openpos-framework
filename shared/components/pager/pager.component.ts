import { Component, ViewChildren, TemplateRef, QueryList, ContentChildren, Input, AfterContentInit } from "@angular/core";

@Component({
    selector: 'app-pager',
    templateUrl: './pager.component.html',
    styleUrls: ['./pager.component.scss']
})

export class PagerComponent implements AfterContentInit {
    @ContentChildren("pagerItem") sections: QueryList<TemplateRef<any>>;

    @Input() pageSize: number = 5;
    currentPage: number = 1;
    totalPages: number;
    currentIndex: number = 0;

    ngAfterContentInit(): void {
        this.totalPages = Math.ceil(this.sections.length / this.pageSize);
    }

    incrementPage(){
        if(this.currentPage != this.totalPages){
            this.currentIndex += this.pageSize;
            this.currentPage++;
        }
    }

    decrementPage(){
        if(this.currentPage != 1){
            this.currentIndex -= this.pageSize;
            this.currentPage--;
        }
    }
}