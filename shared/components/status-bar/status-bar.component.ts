import { IStatusBarControl } from './../../../core/interfaces';
import { MatSnackBar } from '@angular/material';
import { Component, Input, ViewChild, ViewContainerRef, Inject, ComponentFactoryResolver, AfterViewInit, InjectionToken, HostListener } from '@angular/core';
import { StatusBarData } from './status-bar-data';
import { SessionService, PluginService, FileUploadService, TrainingOverlayService } from '../../../core';
import { ComponentType } from '@angular/cdk/overlay/index';
import { Configuration } from '../../../configuration/configuration';


export const STATUS_BAR_STATUS_CONTROL_COMPONENT = new InjectionToken<ComponentType<IStatusBarControl>>('StatusBarStatusControlComponent');
@Component({
  selector: 'app-statusbar',
  templateUrl: './status-bar.component.html'
})
export class StatusBarComponent implements IStatusBarControl, AfterViewInit {
  @Input()
  data: StatusBarData;
  /** Swappable component that is placed on right hand side of status bar.  Clients
   *  may override this with their own component using a provider with token STATUS_BAR_STATUS_CONTROL_COMPONENT.
   */
  @ViewChild('statusBarStatusControl', {read: ViewContainerRef}) statusBarControlContainer: ViewContainerRef;

  constructor(private session: SessionService,
    private pluginService: PluginService, private fileUploadService: FileUploadService,
    public snackBar: MatSnackBar, private trainingService: TrainingOverlayService,
    @Inject(STATUS_BAR_STATUS_CONTROL_COMPONENT) private statusBarStatusCtrlType: ComponentType<IStatusBarControl>,
    private componentFactoryResolver: ComponentFactoryResolver ) {
  }

  ngAfterViewInit(): void {
    const compFactory = this.componentFactoryResolver.resolveComponentFactory(this.statusBarStatusCtrlType);
    const controlRef = this.statusBarControlContainer.createComponent(compFactory);
    controlRef.instance.data = this.data;
    // Run change detection or else get dreaded 'ExpressionChangedAfterItHasBeenCheckedError'
    controlRef.changeDetectorRef.detectChanges();
  }

  public onTraining() {
    this.trainingService.open();
  }

  public onHome() {
    if (this.data.enableHomeAction === true) {
      this.session.onAction('Home');
    }
  }

}
