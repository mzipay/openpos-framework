import {
    ContentChildren,
    Directive,
    ElementRef, Host,
    Input,
    OnDestroy,
    Optional,
    QueryList,
    Renderer2,
    Self
} from '@angular/core';
import {ArrowTabItemDirective} from './arrow-tab-item.directive';
import {fromEvent, Subject, Subscription} from 'rxjs';
import {KeyPressProvider} from '../providers/keypress.provider';
import {takeUntil} from 'rxjs/operators';
import {InfiniteScrollComponent} from '../components/infinite-scroll/infinite-scroll.component';

@Directive({
    selector: '[appArrowTab]'
})
export class ArrowTabDirective implements OnDestroy {
    @Input('appArrowTabPriority') priority = 100
    @ContentChildren(ArrowTabItemDirective)
    arrowTabItems: QueryList<ArrowTabItemDirective>;
    selectedIndex = -1;
    contentObserver: MutationObserver;
    destroyed$ = new Subject();
    private _subscription: Subscription;

    constructor(
        private hostElement: ElementRef,
        private renderer: Renderer2,
        @Optional() @Self() @Host() private infiniteScroll: InfiniteScrollComponent<any>,
        keyPresses: KeyPressProvider
    ) {
        this.contentObserver = new MutationObserver(mutations => this.onContentChanged(mutations));
        this.contentObserver.observe(this.hostElement.nativeElement, {childList: true, subtree: true});

        // fromEvent<Event>(hostElement.nativeElement, 'focusin').pipe(
        //     takeUntil(this.destroyed$)
        // ).subscribe(event => this.onFocus(event));
        //
        // fromEvent<Event>(hostElement.nativeElement, 'focusout').pipe(
        //     takeUntil(this.destroyed$)
        // ).subscribe(event => this.onBlur(event));

        this._subscription = keyPresses.subscribe('ArrowUp', this.priority, (event: KeyboardEvent) => {
            if (event.repeat || event.type !== 'keydown') {
                return;
            }
            this.selectedIndex = this.getActiveButtonIndex() - 1;
            while (this.selectedIndex >= 0 && this.arrowTabItems.toArray()[this.selectedIndex].isDisabled()) {
                this.selectedIndex--;
            }
            if (this.selectedIndex >= 0) {
                this.arrowTabItems.toArray()[this.selectedIndex].nativeElement.focus();
            }
        });

        this._subscription.add(keyPresses.subscribe('ArrowDown', this.priority, (event: KeyboardEvent) => {
            if (event.repeat || event.type !== 'keydown') {
                return;
            }
            this.selectedIndex = this.getActiveButtonIndex() + 1;
            while (this.selectedIndex < this.arrowTabItems.length && this.arrowTabItems.toArray()[this.selectedIndex].isDisabled()) {
                this.selectedIndex++;
            }
            if (this.selectedIndex < this.arrowTabItems.length) {
                this.arrowTabItems.toArray()[this.selectedIndex].nativeElement.focus();
            }
        }));
    }

    ngOnDestroy(): void {
        if (this._subscription) {
            this._subscription.unsubscribe();
        }
        this.contentObserver.disconnect();
        this.destroyed$.next();
    }

    focus(): void {
        if (this.selectedIndex >= 0 && this.selectedIndex < this.arrowTabItems.length) {
            this.arrowTabItems.toArray()[this.selectedIndex].nativeElement.focus();
        }
    }

    onContentChanged(mutations: MutationRecord[]): void {
        const removedNodes = mutations.reduce((allRemovedNodes, mutation) => {
            Array.prototype.push.apply(allRemovedNodes, mutation.removedNodes);
            return allRemovedNodes;
        }, []);

        const removedTabItemIndex = this.arrowTabItems.toArray().findIndex(arrowTabItem => removedNodes.indexOf(arrowTabItem.nativeElement) >= 0);
        // this.selectedIndex = removedTabItemIndex;
        // this.focus();
        console.log(mutations);
        console.log(removedNodes);
    }

    getActiveButtonIndex(): number {
        const arrowTabItems = this.arrowTabItems.toArray()
            .filter(arrowTabItem =>
                arrowTabItem.nativeElement === document.activeElement || arrowTabItem.nativeElement.contains(document.activeElement)
            );

        return arrowTabItems.length > 0 ? this.arrowTabItems.toArray().indexOf(arrowTabItems[0]) : -1;
    }

    // listenForContentChanges(): void {
    //     this.contentObserver.observe(this.hostElement.nativeElement, {childList: true, subtree: true});
    // }

    onFocus(event: Event): void {
        // this.selectedIndex = this.buttons.toArray().findIndex(arrowTab => arrowTab.nativeElement === event.target);
        // this.listenForContentChanges();
    }

    onBlur(event: Event): void {
        // this.contentObserver.disconnect();
    }
}
