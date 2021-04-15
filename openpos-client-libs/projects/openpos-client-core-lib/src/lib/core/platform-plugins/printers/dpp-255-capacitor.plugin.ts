import {IPrinter} from "./printer.interface";
import {InfineaPlugin, IPlatformPlugin} from "../platform-plugin.interface";
import {Observable, of} from "rxjs";
import {Capacitor} from "@capacitor/core";
import {Plugins as CapacitorPlugins} from "@capacitor/core/dist/esm/global";
import {map} from "rxjs/operators";
import {Injectable} from "@angular/core";

declare module '@capacitor/core' {
    interface PluginRegistry {
        Dpp255Capacitor: InfineaPlugin;
    }
}

@Injectable({
    providedIn: 'root'
})
export class Dpp255CapacitorPlugin implements IPrinter, IPlatformPlugin {
    id: string = 'DPP255CAP';

    print(html: String) {
        CapacitorPlugins.Dpp255Capacitor.print({data: html});
    }

    initialize(): Observable<string> {
        return of(CapacitorPlugins.Dpp255Capacitor.initialize({
            apiKey: 'UtJtVhO9yImiQhADyh+0PqEQG0eotVpTsBN9IALb0maa2pUMXH1GvIlhzNLEsIrmFoGs4rumE1Ex4nFR9sHWy1Liox7o/7nvGYfz/kOrisY='
        })).pipe(
            map(() => "initialized DPP-255 printer for Capacitor")
        );
    }

    name(): string {
        return "Dpp255Capacitor";
    }

    pluginPresent(): boolean {
        return Capacitor.isNative && Capacitor.isPluginAvailable('Dpp255Capacitor');
    }
}