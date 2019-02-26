import { Directive, ElementRef, OnInit, Output, EventEmitter } from '@angular/core';
import { MapsAPILoader } from '@agm/core';

declare var google: any;


@Directive({
    // tslint:disable-next-line:directive-selector
    selector: '[autoCompleteAddress]'
})
export class AutoCompleteAddressDirective implements OnInit {

    @Output() onSelect: EventEmitter<any> = new EventEmitter();

    private element: HTMLInputElement;

    constructor(elRef: ElementRef, public mapsAPILoader: MapsAPILoader) {
        // elRef will get a reference to the element where the directive is placed
        this.element = elRef.nativeElement;
    }

    getFormattedAddress(place: any) {
        const location = {};
        for (const item of place.address_components) {
            // Formatted Address
            location['formatted_address'] = place.formatted_address;
            if (item.types) {
                if (item.types.indexOf('locality') > -1) {
                    // City / Locality
                    location['locality'] = item['long_name'];
                } else if (item.types.indexOf('administrative_area_level_1') > -1) {
                    // State
                    location['state'] = item['long_name'];
                } else if (item.types.indexOf('street_number') > -1) {
                    // Street Number
                    location['street_number'] = item['short_name'];
                } else if (item.types.indexOf('route') > -1) {
                    // Street Name / Route
                    location['street_name'] = item['short_name'];
                } else if (item.types.indexOf('country') > -1) {
                    // Country
                    location['country'] = item['long_name'];
                } else if (item.types.indexOf('postal_code') > -1) {
                    // Postal Code
                    location['postal_code'] = item['short_name'];
                }
            }

        }
        return location;
    }

    ngOnInit() {
        this.mapsAPILoader
            .load()
            .then(() => {
                // Restrict search predictions to geographical locations
                const autocomplete = new google.maps.places.Autocomplete(this.element, { types: ['address'] });

                // Restrict set of place fields returned
                autocomplete.setFields(['formatted_address', 'address_components']);

                // Event listener to monitor place changes in the input
                google.maps.event.addListener(autocomplete, 'place_changed', () => {
                    // Emit the new address object for the updated place
                    const place = autocomplete.getPlace();
                    const location = this.getFormattedAddress(place);
                    this.onSelect.emit(location);
                });
            })
            .catch((err) => console.log(err));
    }

}
