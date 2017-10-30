import { IScreen } from '../common/iscreen';
import { Component, AfterViewInit, DoCheck, HostListener } from '@angular/core';
import { SessionService } from '../session.service';
import 'signature_pad';

@Component({
  selector: 'app-signature-capture',
  templateUrl: './signature-capture.component.html'
})
export class SignatureCaptureComponent implements AfterViewInit, DoCheck, IScreen {

  static readonly DEFAULT_MEDIA_TYPE = 'image/jpeg';

  protected initialized: Boolean = false;
  protected signaturePad: SignaturePad;
  protected canvas: HTMLCanvasElement = null;
  protected wrapper: HTMLElement;

  constructor(public readonly session: SessionService) {
  }

  show(session: SessionService) {
  }


  ngDoCheck(): void {
  }

  ngAfterViewInit(): void {
    this.initialized = true;
    this.wrapper = document.getElementById('signature-pad');
    this.canvas = this.wrapper.querySelector('canvas');
    this.canvas.height = this.wrapper.clientHeight;
    this.canvas.width = this.wrapper.clientWidth;
    this.signaturePad = new SignaturePad(this.canvas);
    this.onResizeCanvas(null);
  }

  @HostListener('window:resize', ['$event'])
  onResizeCanvas(evt: Event) {

      const newWidth = this.wrapper.clientWidth;
      const newHeight = this.wrapper.clientHeight;

      const tempCanvas = document.createElement('canvas');
      const tempContext = tempCanvas.getContext('2d');
      const canvasContext = this.canvas.getContext('2d');

      tempCanvas.width = newWidth;
      tempCanvas.height = newHeight;
      tempContext.fillStyle = 'white'; // TODO: this.canvas.getAttribute('backgroundColor');
      tempContext.fillRect(0, 0, newWidth, newHeight);
      tempContext.drawImage(this.canvas, 0, 0);

      // Don't resize original canvas until after the image has been copied
      this.canvas.width = newWidth;
      this.canvas.height = newHeight;

      canvasContext.drawImage(tempCanvas, 0, 0);
  }

  onClearSignature(): void {
    this.signaturePad.clear();
  }

  onSaveSignature(): void {
    if ( this.signaturePad.isEmpty()) {
      console.log('Signature is empty');
      return;
    }
    const mediaType: string = this.session.screen.signatureMediaType ?
        this.session.screen.signatureMediaType : SignatureCaptureComponent.DEFAULT_MEDIA_TYPE;

    const dataUrl: string|null = this.signaturePad.toDataURL(mediaType);
    const dataPoints = this.signaturePad.toData();
    let encodedImage: string|null = null;
    if (dataUrl) {
      const matches: RegExpMatchArray|null = dataUrl.match(/^data:.+\/(.+);base64,(.*)$/);
      encodedImage = matches && matches.length > 2 ? matches[2] : null;
    }
    const signatureData: ISignature = {
      pointGroups: this.signaturePad.toData(),
      mediaType: mediaType,
      base64EncodedImage: encodedImage
    };

    this.session.response = signatureData;
    this.session.onAction('SaveSignature');
  }
}

export interface ISignature {
  pointGroups: IPoint[][];
  mediaType: string;
  base64EncodedImage: string|null;
}

export interface IPoint {
  x: number;
  y: number;
  time: number;
  color?: string;
}
