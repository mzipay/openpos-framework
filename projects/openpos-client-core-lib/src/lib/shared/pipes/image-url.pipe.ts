import { Pipe, PipeTransform } from '@angular/core';
import { ImageService } from '../../core';

@Pipe({ name: 'imageUrl' })
export class ImageUrlPipe implements PipeTransform {

    constructor(private imageService: ImageService) { }

    transform(url: string) {
        return this.imageService.replaceImageUrl(url);
    }

}
