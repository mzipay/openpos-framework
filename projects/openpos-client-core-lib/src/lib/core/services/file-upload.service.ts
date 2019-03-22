
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Logger } from './logger.service';
import { FileUploadResult } from './../interfaces/file-upload-result.interface';
import { PersonalizationService } from './personalization.service';
import { CordovaService } from './cordova.service';
import { Subscription } from 'rxjs';

@Injectable({
    providedIn: 'root',
  })
export class FileUploadService {
    constructor(private log: Logger, private cordovaService: CordovaService, private personalization: PersonalizationService, private httpClient: HttpClient) {
    }

    public async uploadLocalDeviceFileToServer(context: string, filename: string, contentType: string, filepath: string):
      Promise<FileUploadResult> {

        if (this.cordovaService.isRunningInCordova()) {
            let localfilepath = filepath;
            if (! filename.startsWith('file:')) {
                localfilepath = `file://${localfilepath}`;
            }
            this.log.info(`File to upload: ${localfilepath}`);

            const url = this.getUploadServiceUrl();
            this.log.info(`File upload endpoint url: ${url}`);

            const formData = new FormData();
            formData.append('nodeId', this.personalization.getNodeId().toString());
            formData.append('targetContext', context);
            formData.append('filename', filename);
            try {
                const fileBlob = await this.readFile(localfilepath, contentType);
                // For testing large file uploads
                // const fileBlob = await this.readMockFile(localfilepath, contentType);
                this.log.info(`File ${filename} read successfully.  Size is: ${fileBlob.size} bytes. Will now attempt to upload...`);
                formData.append('file', fileBlob);
                return this.uploadFile(url, filename, formData);
            } catch (error) {
                if ('success' in error && 'message' in error) {
                    return error;
                } else {
                    return { success: false, message: error instanceof Error ? error.message : JSON.stringify(error)};
                }
            }
        } else {
            const msg = `Not running in Cordova, cannot upload file ${filename}`;
            this.log.warn(msg);
            return Promise.reject({success: false, message: msg});
        }

    }

    public uploadFile(url: string, filename: string, formData: FormData, timeoutMillis = 20000): Promise<FileUploadResult> {
        let fileUploadTimer: any = null;
        let uploadSub: Subscription = null;

        const timeoutPromise = new Promise<FileUploadResult>((resolve, reject) => {
            fileUploadTimer = setTimeout( () => {
                this.log.info(`upload timed out`);
                if (uploadSub) {
                    uploadSub.unsubscribe();
                }
                resolve({success: false, message: `Upload request for '${filename}' timed out.`});
            },
            timeoutMillis);
        });

        const uploadPromise = new Promise<FileUploadResult>((resolve, reject) => {
            uploadSub = this.httpClient.post(url, formData, {observe: 'response'}).subscribe( response => {
                if (fileUploadTimer) {
                    clearTimeout(fileUploadTimer);
                }
                const msg = `File ${filename} uploaded to server successfully.`;
                this.log.info(msg);
                this.log.info(`File upload response: ${JSON.stringify(response)}`);
                resolve({success: true, message: msg});
            },
            err => {
                if (fileUploadTimer) {
                    clearTimeout(fileUploadTimer);
                }
                const msg = `Upload Error occurred: ${JSON.stringify(err)}`;
                this.log.error(msg);
                const statusCode = err.status || (err.error ? err.error.status : null);
                let errMsg = '';
                if (err.error) {
                    if (err.error.error) {
                        errMsg += err.error.error;
                    }
                    if (err.error.message) {
                        errMsg += (errMsg ? '; ' : '') + err.error.message;
                    }
                }
                const returnMsg = `${statusCode ? statusCode + ': ' : ''}` +
                   (errMsg ? errMsg : 'Upload failed. Check client and server logs.');
                reject({success: false, message: returnMsg});
            });
        });

        return Promise.race([timeoutPromise, uploadPromise]);
    }

    private readMockFile(localfilepath: string, contentType: string): Promise<Blob> {
        const giantArray = [];
        const repeat = '0123457890012345789001234578900123457890012345789001234578900123457890012345789001234578900123457890';
        for (let i = 0; i < 500000; i++) {
            giantArray.push(repeat);
        }
        const blob = new Blob(giantArray, { type: contentType });
        return Promise.resolve(blob);
    }

    protected readFile(localfilepath: string, contentType: string): Promise<Blob> {
        const prom = new Promise<Blob>((resolve, reject) => {
            (<any>window).requestFileSystem((<any>window).PERSISTENT, 0, (fs) => {
                (<any>window).resolveLocalFileSystemURL(localfilepath, (fileEntry) => {
                    fileEntry.file( (file) => {
                        const reader = new FileReader();
                        reader.onloadend = () => {
                            // Create a blob based on the FileReader "result", which we asked to be retrieved as an ArrayBuffer
                            const blob = new Blob([new Uint8Array(<ArrayBuffer>reader.result)], { type: contentType });
                            resolve(blob);
                        };
                        reader.readAsArrayBuffer(file);
                    }, (fileErr) => { this.handleError(`Error getting fileEntry for file: ${localfilepath}`, fileErr); });
                }, (resolveErr) => { this.handleError(`Error resolving file: ${localfilepath}`, resolveErr); });
            }, (fsErr) => { this.handleError('Error getting persistent filesystem!', fsErr); });
        });

        return prom;
    }

    protected getUploadServiceUrl(): string {
        const url = `${this.personalization.getServerBaseURL()}/fileupload/uploadToNode`;
        return url;
    }

    protected handleError(msg: string, e: any) {
        const errorMsg = `${msg}; Error code: ${e.code}, ${JSON.stringify(e)}`;
        this.log.error(errorMsg);
        throw new Error(errorMsg);
    }

}
