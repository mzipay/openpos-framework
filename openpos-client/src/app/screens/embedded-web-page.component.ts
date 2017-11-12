import { Router } from '@angular/router';
import { IScreen } from '../common/iscreen';
import { IMenuItem } from '../common/imenuitem';
// import {MdButtonModule, MdCheckboxModule} from '@angular/material';
import { Component, ViewChild, AfterViewInit, DoCheck } from '@angular/core';
import { SessionService } from '../session.service';

@Component({
  selector: 'app-embedded-web-page',
  templateUrl: './embedded-web-page.component.html'
})
export class EmbeddedWebPageComponent implements IScreen {

  public url: string;

  constructor(private session: SessionService,
    private router: Router) {
  }

  show(session: SessionService) {
    this.url = session.screen.url;
  }

  getURL() {
    let urlNodeId = this.session.nodeId;
    const urlTree = this.router.parseUrl(this.url);
    let hasNodeIdAlready = false;
    let hasQueryParams = false;
    if (urlTree) {
      hasQueryParams = urlTree.queryParams.keys && urlTree.queryParams.keys.length > 0;
      if (urlTree.queryParams['nodeId'] && urlTree.queryParams['nodeId'].length > 0) {
        urlNodeId =  urlTree.queryParams['nodeId'];
        hasNodeIdAlready = true;
      }
    }
    let returnUrl = this.url;
    if ( ! hasNodeIdAlready ) {
      returnUrl = `${this.url}${hasQueryParams ? '&' : '?'}nodeId=${urlNodeId}`;
    }
    returnUrl = `http://${window.location.hostname}:4201/${returnUrl}`;
    console.log('catalogUrl: ' + returnUrl);
    return returnUrl;
  }

}
