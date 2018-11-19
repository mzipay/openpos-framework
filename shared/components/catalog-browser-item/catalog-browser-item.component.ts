import { Component, Input, ChangeDetectorRef, ViewContainerRef, DoCheck, OnInit } from '@angular/core';
import { ISellItem, Logger, IUrlMenuItem } from '../../../core';
import { Observable, Subject } from 'rxjs';

@Component({
    selector: 'app-catalog-browser-item',
    templateUrl: './catalog-browser-item.component.html',
    styleUrls: ['./catalog-browser-item.component.scss']
})
export class CatalogBrowserItemComponent implements OnInit {
    @Input() item: ISellItem;
    loadingError = false;
    private imageLoaded$ = new Subject<boolean>();
    private imageError$ = new Subject<boolean>();

    private showImage$: Promise<boolean>;

    constructor(private log: Logger, private cdRef: ChangeDetectorRef) {
    }

    ngOnInit(): void {
        let imageTimer: any = null;

        const timeoutPromise = new Promise<boolean>((resolve, reject) => {
            imageTimer = setTimeout( () => {
                resolve(this.item.description === null);
            },
            2000);
        });


        const imagePromise = new Promise<boolean>((resolve, reject) => {
            // For both cases below, there will be a default image shown if the image does not
            // successfully load

            // If no url, then only attempt to show an image if there is no item description
            if ((this.item.menuItems[0] && this.item.menuItems[0].hasOwnProperty('url') &&
                ! (<IUrlMenuItem>this.item.menuItems[0]).url )) {
                if (imageTimer) {
                    clearTimeout(imageTimer);
                }
                this.log.debug(`No image URL for item '${this.item.subtitle}', item desc: '${this.item.description}'`);
                resolve(this.item.description === null);
            } else {
                // show the image either if it successfully loaded OR
                // it didn't successfully load && we have don't have an item description
                this.imageLoaded$.subscribe(loaded => {
                    if (imageTimer) {
                        clearTimeout(imageTimer);
                    }
                    const result =
                        (this.item.menuItems[0] && this.item.menuItems[0].hasOwnProperty('url') &&
                        (<IUrlMenuItem>this.item.menuItems[0]).url && loaded)
                        ||
                        (! loaded && this.item.description === null);
                    resolve(result);
                });
            }
        });

        this.showImage$ = Promise.race([imagePromise, timeoutPromise]);

    }

    showImage(): Promise<boolean> {
        return this.showImage$;
    }

    onImageLoaded(): void {
        this.imageLoaded$.next(true);
    }

    onImageLoadError(): void {
        this.imageError$.next(true);
        this.imageLoaded$.next(false);
    }
}
