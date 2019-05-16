import { Component, Input, OnInit, ElementRef, Renderer2 } from '@angular/core';
import { IconService } from '../../../core/services/icon.service';
import { Observable } from 'rxjs';
import { SafeHtml } from '@angular/platform-browser';

@Component({
    selector: 'app-icon',
    templateUrl: './icon.component.html',
    styleUrls: ['./icon.component.scss']
})

export class IconComponent implements OnInit {

    @Input()
    iconName: string;

    @Input() iconClass: string;

    parser = new DOMParser();
    icon: Observable<SafeHtml>;

    ngOnInit(): void {
        this.icon = this.iconService.getIconHtml(this.iconName);
        this.renderer.addClass( this.elementRef.nativeElement, 'mat-24');
        if ( this.iconClass ) {
            this.iconClass.split(' ').forEach( e => this.renderer.addClass( this.elementRef.nativeElement, e));
        }
    }


    constructor( private iconService: IconService, private elementRef: ElementRef, private renderer: Renderer2 ) {}
}
