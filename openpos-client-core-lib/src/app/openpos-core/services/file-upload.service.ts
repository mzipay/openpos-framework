import { HttpClient } from '@angular/common/http';
import { IPlugin } from './../common/iplugin';
import { SessionService } from './session.service';
import { Injectable } from '@angular/core';
import { PluginService } from './plugin.service';
import { HttpParams } from '@angular/common/http';

@Injectable()
export class FileUploadService {
    constructor(private sessionService: SessionService, private pluginService: PluginService, private httpClient: HttpClient) {

    }

    protected getUploadServiceUrl(): string {
        const port = this.sessionService.getServerPort();
        const url = `http://${this.sessionService.getServerName()}${port ? `:${port}` : ''}/fileupload/uploadToNode`;
        return url;
    }

    public uploadLocalDeviceFileToServer(context: string, filename: string, contentType: string, filepath: string):
      Promise<{success: boolean, message: string}> {
        const prom = new Promise<{success: boolean, message: string}>( (resolve, reject) => {
            let localfilepath = filepath;
            if (! filename.startsWith('file:')) {
                localfilepath = `file://${localfilepath}`;
            }
            console.log(`File to upload: ${localfilepath}`);

            const url = this.getUploadServiceUrl();
            console.log(`File upload endpoint url: ${url}`);

            const httpClient = this.httpClient;
            const formData = new FormData();
            formData.append('nodeId', this.sessionService.nodeId.toString());
            formData.append('targetContext', context);
            formData.append('filename', filename);

            // The cordova 'file' plugin is needed in order to access the local device filesystem
            this.pluginService.getPlugin('file').then(
            (filePlugin: IPlugin) => {
                if (filePlugin.impl) {
                    (<any>window).requestFileSystem((<any>window).PERSISTENT, 0, function (fs) {
                        (<any>window).resolveLocalFileSystemURL(localfilepath, function(fileEntry){
                            fileEntry.file(function (file) {
                                const reader = new FileReader();
                                reader.onloadend = function() {
                                    // Create a blob based on the FileReader "result", which we asked to be retrieved as an ArrayBuffer
                                    const blob = new Blob([new Uint8Array(this.result)], { type: contentType });
                                    formData.append('file', blob);
                                    httpClient.post(url, formData, {observe: 'response'}).subscribe( response => {
                                        const msg = `File ${filename} uploaded to server successfully.`;
                                        console.log(msg);
                                        console.log(`File upload response: ${JSON.stringify(response)}`);
                                        resolve({success: true, message: msg});
                                    },
                                    err => {
                                        const msg = `Upload Error occurred: ${JSON.stringify(err)}`;
                                        console.log(msg);
                                        reject({success: false, message: msg});
                                    });
                                };
                                reader.readAsArrayBuffer(file);
                            }, function (fileErr) { handleError(prom, `Error getting fileEntry for file: ${localfilepath}`, fileErr); });
                        }, function(resolveErr) { handleError(prom, `Error resolving file: ${localfilepath}`, resolveErr); });
                    }, function (fsErr) { handleError(prom, 'Error getting persistent filesystem!', fsErr); });
                } else {
                    const msg = `file plugin not found, cannot upload file ${filename}`;
                    console.log(msg);
                    reject({success: false, message: msg});
                }
            }
            );
        });
        return prom;
      }
}

function handleError(returnPromise, msg, e) {
    console.error(`${msg}; Error code: ${e.code}, ${JSON.stringify(e)}`);
    returnPromise.reject({success: false, message: msg});
}
