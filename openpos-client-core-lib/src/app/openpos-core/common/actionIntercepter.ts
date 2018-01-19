
export class ActionIntercepter {

  private intercepter: FunctionActionIntercepter;
  private behavior: ActionIntercepterBehavior;

  constructor( intercepter: FunctionActionIntercepter, behavior: ActionIntercepterBehavior ) {
    this.intercepter = intercepter;
  }

  public intercept( payload: any,  continueWith: Function) {
      switch ( this.behavior) {
        case ActionIntercepterBehavior.before:
          console.log( 'Intercepting Action' );
          this.intercepter( payload );
          console.log( 'Continuing with default action handler');
          continueWith();
          break;
        case ActionIntercepterBehavior.block:
          console.log( 'Intercepting Action');
          this.intercepter( payload );
          console.log( 'Skipping default action handler');
          break;
      }
  }

}

export type FunctionActionIntercepter = (data?: any) => void;

export enum ActionIntercepterBehavior {
  before,
  block
}
