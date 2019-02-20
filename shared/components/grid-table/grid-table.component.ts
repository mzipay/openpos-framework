import { Component, OnInit, Input } from '@angular/core';

@Component({
    selector: 'app-grid-table',
    templateUrl: './grid-table.component.html',
    styleUrls: ['./grid-table.component.scss']
})
export class GridTableComponent implements OnInit {

    @Input()
    columnHeaders = [];

    @Input()
    rows = [];

    minHeight = '40px';
    minWidth = '40px';

    outerStyle: object;

    constructor() {
    }

    ngOnInit(): void {
        const numberRows = this.rows.length;
        const numberCols = this.columnHeaders.length;

        this.outerStyle = {
            'grid-template-rows':
                `repeat(${numberRows}, minmax(${this.minHeight}, auto))`,
            'grid-template-columns':
                `repeat(${numberCols}, minmax(${this.minWidth}, auto))`,
        };
    }

}
