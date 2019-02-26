import { Component, OnInit } from '@angular/core';
import { PosScreen } from '../pos-screen/pos-screen.component';


@Component({
  selector: 'app-auto-complete-address',
  templateUrl: './auto-complete-address.component.html',
  styleUrls: ['./auto-complete-address.component.scss']
})
export class AutoCompleteAddressComponent extends PosScreen<any> implements OnInit {

  constructor() {
    super();
  }

  buildScreen() {
  }

  ngOnInit(): void {

  }

  setAddress(address: any) {
    console.log(address);
  }

}
