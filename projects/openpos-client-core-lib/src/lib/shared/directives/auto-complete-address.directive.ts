import { Directive, ElementRef, Output, EventEmitter, AfterViewInit, OnDestroy } from '@angular/core';
import { Configuration } from '../../configuration/configuration';


declare var google: any;


@Directive({
    // tslint:disable-next-line:directive-selector
    selector: '[autoCompleteAddress]'
})
export class AutoCompleteAddressDirective implements AfterViewInit, OnDestroy {

    @Output() onSelect: EventEmitter<any> = new EventEmitter();

    private element: HTMLInputElement;

    protected readonly SCRIPT_ID: string = 'googleMapsApiScript';
    protected readonly CALLBACK_NAME: string = 'initAutoComplete';

    constructor(protected elRef?: ElementRef) {
        // elRef will get a reference to the element where the directive is placed
        if (elRef.nativeElement instanceof HTMLInputElement) {
            this.element = elRef.nativeElement;
        }
    }

    ngAfterViewInit() {
        if (!this.element) {
            const input = this.elRef.nativeElement.querySelector('input');
            if (input && input instanceof HTMLInputElement) {
                this.element = input;
            }
        }

        this.loadApi();
    }

    getFormattedAddress(place: any) {
        const address = {};
        if (place) {
            // Formatted Address
            address['formatted_address'] = place.formatted_address;

            if (place.address_components) {
                for (const item of place.address_components) {
                    if (item.types) {
                        if (item.types.indexOf('locality') > -1) {
                            // City / US Locality
                            address['locality'] = item['long_name'];
                        } else if (item.types.indexOf('postal_town') > -1) {
                            // City / Foreign Locality
                            address['locality'] = item['long_name'];
                        } else if (item.types.indexOf('administrative_area_level_1') > -1) {
                            // State
                            address['state'] = item['long_name'];
                        } else if (item.types.indexOf('street_number') > -1) {
                            // Street Number
                            address['streetNumber'] = item['short_name'];
                        } else if (item.types.indexOf('route') > -1) {
                            // Street Name / Route
                            address['streetName'] = item['long_name'];
                        } else if (item.types.indexOf('country') > -1) {
                            // Country
                            address['country'] = item['long_name'];
                        } else if (item.types.indexOf('postal_code') > -1) {
                            // Postal Code
                            address['postalCode'] = item['short_name'];
                        }
                    }
                }
            }
        }
        return address;
    }

    loadApi() {
        const loadAPI = this.attachScript();
        loadAPI.then(() => {
            this.initAutoComplete();
        });
    }

    attachScript() {
        const key = Configuration.googleApiKey;
        if (!key) {
            return Promise.reject('No Google API Key provided');
        }

        if ((<any>window).google && (<any>window).google.maps) {
            // Google maps already loaded on the page
            return Promise.resolve();
        }

        const script = this.element.ownerDocument.createElement('script');
        script.type = 'text/javascript';
        script.async = true;
        script.defer = true;
        script.id = this.SCRIPT_ID;
        script.src = `https://maps.googleapis.com/maps/api/js?key=${key}&libraries=places&callback=${this.CALLBACK_NAME}`;

        const loadAPI = new Promise<void>((resolve: Function, reject: Function) => {
            window[this.CALLBACK_NAME] = () => {
                resolve();
            };

            script.onerror = (error: Event) => {
                reject(error);
            };
        });

        this.element.ownerDocument.body.appendChild(script);

        return loadAPI;
    }

    initAutoComplete() {
        // Restrict search predictions to adresses
        const autocomplete = new google.maps.places.Autocomplete(this.element, { types: ['address'] });

        // Restrict set of place fields returned
        autocomplete.setFields(['formatted_address', 'address_components']);

        // Event listener to monitor place changes in the input
        google.maps.event.addListener(autocomplete, 'place_changed', () => {
            // Emit the new address object for the updated place
            const place = autocomplete.getPlace();
            const address = this.getFormattedAddress(place);
            this.onSelect.emit(address);
        });
    }

    ngOnDestroy(): void {
        const element = document.getElementById(this.SCRIPT_ID);
        if (element) {
            document.body.removeChild(element);
        }
    }

}
