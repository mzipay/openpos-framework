import { Directive, Renderer2, ElementRef, Input, OnInit } from '@angular/core';
import { OpenposMediaService } from '../../core/services';

const selectors = `
[klass], [klass.xs], [klass.sm], [klass.md], [klass.lg], [klass.xl]
`;

  @Directive({selector: selectors})
  export class KlassDirective implements OnInit  {
    private prevClass: string;
    private inputMap: Map<string, string> = new Map;

    @Input('klass')
    set klass(val: string) {
        this.inputMap.set('', val);
    }
    @Input('klass.xs')
    set klassXs(val: string) {
        this.inputMap.set('xs', val);
    }
    @Input('klass.sm')
    set klassSm(val: string) {
        this.inputMap.set('sm', val);
    }
    @Input('klass.md')
    set klassMd(val: string) {
        this.inputMap.set('md', val);
    }
    @Input('klass.lg')
    set klassLg(val: string) {
        this.inputMap.set('lg', val);
    }
    @Input('klass.xl')
    set klassXl(val: string) {
        this.inputMap.set('xl', val);
    }
    constructor(private mediaService: OpenposMediaService, private renderer: Renderer2, private el: ElementRef) {
    }

    ngOnInit(): void {
        this.mediaService.mediaObservableFromMap(this.inputMap).subscribe( c => {

            // Remove old class first incase there is overlap
            if ( this.prevClass ) {
                this.prevClass.split(' ').forEach( klass => {
                    this.renderer.removeClass( this.el.nativeElement, klass);
                });
            }
            if ( c ) {
                c.split(' ').forEach( klass => {
                    this.renderer.addClass(this.el.nativeElement, klass);
                });
            }
            this.prevClass = c;
        });
      }
  }
