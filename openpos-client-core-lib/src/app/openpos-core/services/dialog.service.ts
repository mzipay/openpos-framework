import { IScreen } from '../common/iscreen';
import { Injectable, Type, ComponentFactoryResolver, ComponentFactory } from '@angular/core';
import { SessionService } from '../services/session.service';
import { ChooseOptionsDialogComponent } from '../dialogs/choose-options/choose-options-dialog.component';
import { ScreenService } from '.';
import { MultipleDynamicFormDialogComponent } from '../dialogs/multiple-dynamic-form/multiple-dynamic-form-dialog.component';

@Injectable()
export class DialogService {

  private dialogs = new Map<string, Type<IScreen>>();

  constructor(private componentFactoryResolver: ComponentFactoryResolver,
    private sessionService: SessionService, private screenService: ScreenService) {

    // To make a dialog screen available add it here and in entryComponents in the app.module.ts
    this.dialogs.set('ChooseOptions', ChooseOptionsDialogComponent);
    this.dialogs.set('MultipleDynamicForm', MultipleDynamicFormDialogComponent);
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
    return this.dialogs.has(name) || this.screenService.hasScreen(name);
  }

  public resolveDialog(type: string): ComponentFactory<IScreen> {
    const dialogType: Type<IScreen> = this.dialogs.get(type);
    if (dialogType) {
        return this.componentFactoryResolver.resolveComponentFactory(dialogType);
    } else {
        return this.screenService.resolveScreen(type);
    }
  }
}


