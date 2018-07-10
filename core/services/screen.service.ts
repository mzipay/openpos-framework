import { Injectable, Type, ComponentFactoryResolver, ComponentFactory } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SessionService } from './session.service';
import { IScreen } from '../components';

@Injectable({
    providedIn: 'root',
  })
export class ScreenService {

  private screens = new Map<string, Type<IScreen>>();

  constructor(private componentFactoryResolver: ComponentFactoryResolver, private http: HttpClient,
    private sessionService: SessionService) { }

  public addScreen(name: string, type: Type<IScreen>): void {
    if (this.screens.get(name)) {
      // tslint:disable-next-line:max-line-length
      console.log(`replacing registration for screen of type ${this.screens.get(name).name} with ${type.name} for the key of ${name} in the screen service`);
      this.screens.delete(name);
    }
    this.screens.set(name, type);
  }

  public hasScreen(name: string): boolean {
    return this.screens.has(name);
  }

  public resolveScreen(type: string): ComponentFactory<IScreen> {
    const screenType: Type<IScreen> = this.screens.get(type);
    if (screenType) {
      return this.componentFactoryResolver.resolveComponentFactory(screenType);
    } else {
      return null;
    }
  }

  public getFieldValues(fieldId: string, searchTerm?: string): Observable<any> {
    const url: string = this.sessionService.getApiServerBaseURL() + '/app/'
      + this.sessionService.getAppId() + '/node/'
      + this.sessionService.getNodeId() + '/control/'
      + fieldId;

    const httpParams = {};
    if (searchTerm) {
      httpParams['searchTerm'] = searchTerm;
    }
    console.log(`Requesting field values from the server using url: ${url}, params: '${JSON.stringify(httpParams)}'`);
    return this.http.get(url, {params: httpParams});
  }

}


