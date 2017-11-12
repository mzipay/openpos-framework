import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable()
export class SessionService {

  private _nodeId = '05243-013';

  get nodeId(): string {
      return this._nodeId;
  }

  set nodeId(nodeId: string) {
      this._nodeId = nodeId;
  }

}


