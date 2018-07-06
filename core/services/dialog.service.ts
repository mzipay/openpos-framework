import { Injectable, Type, ComponentFactoryResolver, ComponentFactory } from '@angular/core';
import { IScreen } from '../../common/iscreen';
@Injectable({
    providedIn: 'root',
  })
export class DialogService {

  private dialogs = new Map<string, Type<IScreen>>();

  constructor(private componentFactoryResolver: ComponentFactoryResolver) {
  }

  public addDialog(name: string, type: Type<IScreen>): void {
    if (this.dialogs.get(name)) {
      // tslint:disable-next-line:max-line-length
      console.log(`replacing registration for screen of type ${this.dialogs.get(name).name} with ${type.name} for the key of ${name} in the screen service`);
      this.dialogs.delete(name);
    }
    this.dialogs.set(name, type);
  }

  public hasDialog(name: string): boolean {
    return this.dialogs.has(name);
  }

  public resolveDialog(type: string): ComponentFactory<IScreen> {
    const dialogType: Type<IScreen> = this.dialogs.get(type);
    if (dialogType) {
        return this.componentFactoryResolver.resolveComponentFactory(dialogType);
    } else {
        return null;
    }
  }
}


