import { Component, Injector } from '@angular/core';
import { HomeInterface } from './home.interface';
import { ScreenComponent } from '../../shared/decorators/screen-component.decorator';
import { PosScreen } from '../pos-screen/pos-screen.component';
import { OpenposMediaService, MediaBreakpoints } from '../../core/media/openpos-media.service';
import { Configuration } from '../../configuration/configuration';
import { Observable, from, timer } from 'rxjs';
import { IActionItem } from '../../core/actions/action-item.interface';
import { INotificationItem } from '../../core/interfaces/notification-item.interface';
import { trigger, state, style, transition, useAnimation } from '@angular/animations';
import { map } from 'rxjs/operators';
import { bounceAnimation } from '../../shared/animations/bounce.animation';

@ScreenComponent({
  name: 'Home'
})
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  animations: [
    trigger('bounce', [
      // ...
      state('move', style({
        transform: 'translateY(0)'
      })),
      transition('* => move', [
        useAnimation( bounceAnimation, {
          params: {
            height: '100px',
            time: '2s'
          }
        })
      ])
    ]),
  ]
})
export class HomeComponent extends PosScreen<HomeInterface> {

  bounceInterval = timer(5000, 5000).pipe( map( i => i % 2 ? 'down' : 'move'));
  gutterSize: Observable<number>;
  gridColumns: Observable<number>;
  isMobile: Observable<boolean>;
  iconClass: Observable<string>;
  badgeSize: Observable<string>;

  

  constructor( media: OpenposMediaService, injector: Injector ) {
    super(injector);
    this.gridColumns = media.observe(new Map([
      [MediaBreakpoints.MOBILE_PORTRAIT, 1],
      [MediaBreakpoints.MOBILE_LANDSCAPE, 2],
      [MediaBreakpoints.TABLET_PORTRAIT, 3],
      [MediaBreakpoints.TABLET_LANDSCAPE, 3],
      [MediaBreakpoints.DESKTOP_PORTRAIT, 3],
      [MediaBreakpoints.DESKTOP_LANDSCAPE, 3]
    ]));

    this.gutterSize = media.observe(new Map([
      [MediaBreakpoints.MOBILE_PORTRAIT, 10],
      [MediaBreakpoints.MOBILE_LANDSCAPE, 10],
      [MediaBreakpoints.TABLET_PORTRAIT, 20],
      [MediaBreakpoints.TABLET_LANDSCAPE, 20],
      [MediaBreakpoints.DESKTOP_PORTRAIT, 20],
      [MediaBreakpoints.DESKTOP_LANDSCAPE, 20]
    ]));

    this.isMobile = media.observe(new Map([
      [MediaBreakpoints.MOBILE_PORTRAIT, true],
      [MediaBreakpoints.MOBILE_LANDSCAPE, false],
      [MediaBreakpoints.TABLET_PORTRAIT, false],
      [MediaBreakpoints.TABLET_LANDSCAPE, false],
      [MediaBreakpoints.DESKTOP_PORTRAIT, false],
      [MediaBreakpoints.DESKTOP_LANDSCAPE, false]
    ]));

    this.iconClass = media.observe(new Map([
      [MediaBreakpoints.MOBILE_PORTRAIT, 'material-icons mat-24'],
      [MediaBreakpoints.MOBILE_LANDSCAPE, 'material-icons mat-24'],
      [MediaBreakpoints.TABLET_PORTRAIT, 'material-icons mat-24'],
      [MediaBreakpoints.TABLET_LANDSCAPE, 'material-icons mat-64'],
      [MediaBreakpoints.DESKTOP_PORTRAIT, 'material-icons mat-64'],
      [MediaBreakpoints.DESKTOP_LANDSCAPE, 'material-icons mat-64']
    ]));

    this.badgeSize = media.observe(new Map([
      [MediaBreakpoints.MOBILE_PORTRAIT, 'medium'],
      [MediaBreakpoints.MOBILE_LANDSCAPE, 'medium'],
      [MediaBreakpoints.TABLET_PORTRAIT, 'medium'],
      [MediaBreakpoints.TABLET_LANDSCAPE, 'large'],
      [MediaBreakpoints.DESKTOP_PORTRAIT, 'large'],
      [MediaBreakpoints.DESKTOP_LANDSCAPE, 'large']
    ]));
  }

  buildScreen() {}

  public keybindsEnabled() {
    return Configuration.enableKeybinds;
  }

  public getNotificationForButton(item: IActionItem): INotificationItem {
    if (this.screen.notificationItems) {
      return this.screen.notificationItems.find(i => i.id === item.action);
    }
    return null;
  }

  public ngAfterViewInit() {
//     (<any>window).player.playVideo();
  }

  public ngOnDestroy() {
//     console.log('desroy - stopping video.');
//     (<any>window).player.stopVideo();
  }

  //       // 2. This code loads the IFrame Player API code asynchronously.
  //       console.log('init youtube background.');
  //   var tag = document.createElement('script');

  //   tag.src = "https://www.youtube.com/iframe_api";
  //   var firstScriptTag = document.getElementsByTagName('script')[0];
  //   firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);

  //   // 3. This function creates an <iframe> (and YouTube player)
  //   //    after the API code downloads.
  //   var player;
  //   function onYouTubeIframeAPIReady() {
  //     player = new (<any>window).YT.Player('player', {
  //       height: '100%',
  //       width: '100%',
  //       videoId: 'vU-_8DkZDWo',
  //       playerVars: {
  //           autoplay: 1,        // Auto-play the video on load
  //     autohide: 1, // Hide video controls when playing
  //     disablekb: 1, 
  //     controls: 0,        // Hide pause/play buttons in player
  //     showinfo: 0,        // Hide the video title
  //     modestbranding: 1,  // Hide the Youtube Logo
  //     loop: 1,            // Run the video in a loop
  //     fs: 0,              // Hide the full screen button       
  //     rel: 0,
  //     enablejsapi: 1,
  //     theme: 'light'

  //       },
  //       events: {
  //         'onReady': onPlayerReady,
  //         'onStateChange': onPlayerStateChange
  //       }

  //     });
  //   }

  //   // 4. The API will call this function when the video player is ready.
  //   function onPlayerReady(event) {
  //     player.mute();
  //     event.target.playVideo();
  //   }

  //   // 5. The API calls this function when the player's state changes.
  //   //    The function indicates that when playing a video (state=1),
  //   //    the player should play for six seconds and then stop.
  //   var done = false;
  //   function onPlayerStateChange(event) {
  //       console.log('player state change: ' + event.data);
  //     if (event.data == (<any>window).YT.PlayerState.PLAYING && !done) {
  //       setTimeout(stopVideo, 6000);

  //       done = true;
  //     } else if (event.data == (<any>window).YT.PlayerState.ENDED) {
  //           player.mute();
  //           player.playVideo();
  //     }

  //   }
  //   function stopVideo() {
  //   //   player.stopVideo();
  //   }
  // }
}
