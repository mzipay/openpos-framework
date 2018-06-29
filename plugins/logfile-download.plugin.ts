import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { FileUploadService } from './../services/file-upload.service';
import { CordovaDevicePlugin } from './../common/cordova-device-plugin';
import { IPlugin } from '../common/iplugin';
import { IDeviceRequest } from '../common/idevicerequest';
import { ReflectiveInjector, Injector } from '@angular/core';
import { SessionService, PluginService} from '../services';
/*
 * A wrapper around the OpenPOSCordovaLogPlugin to provide operations to allow
 * the server to request download of one or more log files from the client.
 */
export class LogfileDownloadPlugin extends CordovaDevicePlugin {

    pluginId = 'logfileDownloadPlugin';
    pluginName = this.pluginId;
    // private pluginImpl: IPlugin;
    //private fileUploadService: FileUploadService;

    constructor(private fileUploadService: FileUploadService) {
        super('openPOSCordovaLogPlugin');
    }
    
    init(successCallback: () => void, errorCallback: (error?: string) => void): void {
        super.init(successCallback, errorCallback);
        //const injector = Injector.create([{provide: FileUploadService, deps: []}]);
        //this.fileUploadService = injector.get(FileUploadService);
    }

    processRequest(deviceRequest: IDeviceRequest, successCallback: (response: any) => void, errorCallback: (error: string) => void) {
        switch (deviceRequest.subType) {
            case 'LIST': // list the log files
                console.log('list files request received.');
                if (this.impl) {
                    this.impl.listLogFiles('DESC', successCallback, errorCallback);
                } else {
                    console.log('No log files available');
                    successCallback([]);
                }
                break;
            case 'DOWNLOAD_FILES': // download the given list of files
                console.log('download-files request received.');
                if (this.impl) {
                    const logFiles = deviceRequest.payload || [];
                    let uploadPromises: Promise<string>[] = [];
                    logFiles.forEach(logfile => {
                        uploadPromises.push(this.uploadFile(logfile));
                    });
                    Promise.all(uploadPromises).then(messages => {
//                        results.forEach(result => console.log(`Promise result: ${JSON.stringify(result)}`));
//                        console.log(`Messages received: ${messages}`);
                        successCallback(messages);
                    }).catch( error => 
                        errorCallback(error)
                    );
                } else {
                    successCallback('No files to download');
                }
                break;
        }
    }

    private uploadFile(logFilename: string) : Promise<string> {
        return new Promise<string>((resolve, reject) => {
            this.impl.getLogFilePath(
                logFilename,
                (logfilePath) => {
                    this.fileUploadService.uploadLocalDeviceFileToServer('log', logFilename, 'text/plain', logfilePath).then(
                        result => {
                            resolve(result.message);    
                        }
                    ).catch(error => {
                        reject(error);
                    });
                },
                (error) => {
                    console.log(error);
                    reject(error);
                }
            )
        });
    } 

}
