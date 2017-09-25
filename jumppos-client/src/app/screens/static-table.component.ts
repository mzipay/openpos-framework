import { Component, OnInit } from '@angular/core';
import { SessionService } from '../session.service';
import { IScreen } from './../common/iscreen';
import { DataSource } from '@angular/cdk/collections';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Observable } from 'rxjs/Observable';

@Component({
    selector: 'app-static-table',
    templateUrl: './static-table.component.html'
})
export class StaticTableComponent implements IScreen, OnInit {
    rowData: RowDatabase;
    dataSource: RowDataSource | null;

    /** Table columns */
    columns: Array<ColumnDef> = [];
    columnIds: Array<string> = [];
    columnsById: {[key: string]: ColumnDef} = {};

    constructor(public session: SessionService) {
        this.initColumnDefs();
        this.rowData = new RowDatabase(this.session.screen.tableData);
    }

    private initColumnDefs(): void {
        // Create the list of column definitions
        let columnIdx = 0;
        if (this.session.screen.headerLabels) {
            // Initialize the list of column metadata and other convenience
            // column data structures
            this.session.screen.headerLabels.forEach(
                (headerLabel) => {
                    this.columns.push(
                        { index: columnIdx++, columnId: headerLabel, headerLabel: headerLabel }
                    );
                }
            );
            this.columnIds = this.columns.map(c => c.columnId);
            this.columns.forEach((col) => { this.columnsById[col.columnId] = col; });
        }
    }

    ngOnInit() {
        this.dataSource = new RowDataSource(this.rowData);
    }

    show(session: SessionService) {
    }
}

/**
 * Attributes that describe a column.
 */
export interface ColumnDef {
    /** Index of the column, 0-based */
    index: number;
    /** An identifier for the column, can just be the column header label */
    columnId: string;
    /** A label for the column that can be displayed to the user */
    headerLabel: string;
}

/**
 * Attributes that describe a row in the table to be displayed
 */
export interface TableRow {
    /** Index of the row, 0-based */
    index: number;
    /** Pre-formatted cell values to display to the user. The count of values
     * for each row must be >= the number of columns. */
    values: Array<string>;
}

/**
 * Holds the static data to be displayed. Data is provided in the form of
 * an Array of String Arrays.
 */
export class RowDatabase {
    /** Stream that emits an event whenever the data has been modified. */
    dataChange: BehaviorSubject<TableRow[]> = new BehaviorSubject<TableRow[]>([]);
    get data(): TableRow[] { return this.dataChange.value; }

    constructor(rows: Array<Array<string>>) {
        rows.forEach((row) => {
            this.addRow(row);
        });
    }

    /** Adds a new row to the database. */
    addRow(row: Array<string>) {
        const copiedData = this.data.slice();
        copiedData.push(this.createNewRow(row));
        this.dataChange.next(copiedData);
      }

    private createNewRow(row: Array<string>): TableRow {
        return {
          index: this.data.length,
          values: row
        };
    }
}

export class RowDataSource extends DataSource<any> {
    constructor(private _rowDatabase: RowDatabase) {
      super();
    }

    /** Connect function called by the table to retrieve one stream containing the data to render. */
    connect(): Observable<TableRow[]> {
      return this._rowDatabase.dataChange;
    }

    disconnect() {}
}
