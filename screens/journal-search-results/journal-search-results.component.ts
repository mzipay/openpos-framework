import { Component } from '@angular/core';
import { PosScreen } from '../pos-screen/pos-screen.component';


@Component({
  selector: 'app-journal-search-results',
  templateUrl: './journal-search-results.component.html',
  styleUrls: ['./journal-search-results.component.scss']
})
export class JournalSearchResultsComponent extends PosScreen<any> {

  buildScreen() {}

}