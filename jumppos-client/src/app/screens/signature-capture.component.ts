import { IScreen } from '../common/iscreen';
import {Component, AfterViewInit, DoCheck} from '@angular/core';
import {SessionService} from '../session.service';
import 'signature_pad';

@Component({
  selector: 'app-prompt',
  templateUrl: './signature-capture.component.html'
})
export class SignatureCaptureComponent implements AfterViewInit, DoCheck, IScreen {

  static readonly DEFAULT_MEDIA_TYPE = 'image/jpeg';

  protected initialized: Boolean = false;
  protected signaturePad: SignaturePad;
  protected canvas: HTMLCanvasElement = null;

  constructor(public readonly session: SessionService) {
  }

  show(session: SessionService) {
  }


  ngDoCheck(): void {
  }

  ngAfterViewInit(): void {
    this.initialized = true;
    const wrapper = document.getElementById('signature-pad');
    this.canvas = wrapper.querySelector('canvas');
    this.canvas.height = wrapper.clientHeight;
    this.canvas.width = wrapper.clientWidth;
    this.signaturePad = new SignaturePad(this.canvas);
    window.addEventListener('resize', this.resizeCanvas);
    this.resizeCanvas(null);
  }

  protected resizeCanvas = (evt: Event) => {
    // When zoomed out to less than 100%, for some very strange reason,
    // some browsers report devicePixelRatio as less than 1
    // and only part of the canvas is cleared then.
    const ratio =  Math.max(window.devicePixelRatio || 1, 1);

    // This part causes the canvas to be cleared
// TODO: determine if this is necessary, working better without for now
//    this.canvas.width = this.canvas.offsetWidth * ratio;
//    this.canvas.height = this.canvas.offsetHeight * ratio;
//    this.canvas.getContext('2d').scale(ratio, ratio);

    // This library does not listen for canvas changes, so after the canvas is automatically
    // cleared by the browser, SignaturePad#isEmpty might still return false, even though the
    // canvas looks empty, because the internal data of this library wasn't cleared. To make sure
    // that the state of this library is consistent with visual state of the canvas, you
    // have to clear it manually.
    this.signaturePad.clear();
  }

  onClearSignature(): void {
    this.signaturePad.clear();
  }

  onSaveSignature(): void {
    if ( this.signaturePad.isEmpty()) {
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
    this.session.screen.signatureData = encodedImage;
    this.session.response = JSON.stringify( {points: dataPoints, mediaType: mediaType, base64EncodedImage: encodedImage });
    this.session.onAction('SaveSignature');
  }
}
