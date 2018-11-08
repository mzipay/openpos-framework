import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-time-chooser',
  templateUrl: './time-chooser.component.html',
  styleUrls: ['./time-chooser.component.scss']
})
export class TimeChooserComponent implements OnInit {

  @Input() value: string;
  @Input() formGroup: FormGroup;
  @Input() controlName: string;
  @Input() label: string;
  @Input() required: boolean;
  @Input() keyboardLayout: string;
  @Input() iconName: string;
  @Output() change = new EventEmitter<string>();

  hours: string;
  minutes: string;
  seconds: string;
  amPm = 'am';

  constructor() { }

  ngOnInit() {
  }

  onTimeChange() {
    this.value = this.formatTime();
    console.log(this.value);
    this.change.emit(this.value);
    this.formGroup.controls[this.controlName].setValue(this.value);
  }

  private formatTime(): string {
    const h24 = this.convertTo24();

    const h = this.padTime(h24);
    const m = this.padTime(this.minutes);
    const s = this.padTime(this.seconds);

    return h.concat(':', m, ':', s);
  }

  private convertTo24(): string {
    let h24 = Number.parseInt(this.hours, 10);
    if (this.hours) {
      if (h24 < 12 && this.amPm === 'pm') {
        h24 += 12;
      } else if (h24 === 12 && this.amPm === 'am') {
        h24 = 0;
      }
    }
    return h24.toString(10);
  }

  private padTime(input: string) {
    let output = '00';
    if (input) {
      if (input.length === 1) {
        output = '0'.concat(input);
      } else {
        output = input;
      }
    }
    return output;
  }
}
