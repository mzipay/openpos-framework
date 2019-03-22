import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root',
})
export class Logger {

    logLevel = LogLevel.INFO;

    public debug(message: any, ...args: any[]): void {
        this.log(LogLevel.DEBUG, message, args);
    }

    public info(message: any, ...args: any[]): void {
        this.log(LogLevel.INFO, message, args);
    }

    public warn(message: any, ...args: any[]): void {
        this.log(LogLevel.WARN, message, args);
    }

    public error(message: any, ...args: any[]): void {
        this.log(LogLevel.ERROR, message, args);
    }

    protected log(logLevel: number, message: any, args: any[]): void {
        if (this.logLevel >= logLevel) {
            let messageType: string;
            switch (logLevel) {
                case LogLevel.DEBUG:
                    messageType = 'debug';
                    break;
                case LogLevel.WARN:
                    messageType = 'warn';
                    break;
                case LogLevel.ERROR:
                    messageType = 'error';
                    break;
                case LogLevel.INFO:
                default:
                    messageType = 'info';
                    break;
            }
            if (args.length > 0) {
                console[messageType](message, args);
            } else {
                console[messageType](message);
            }
        }
    }
}

export class LogLevel {
    public static DEBUG = 4;
    public static INFO = 3;
    public static WARN = 2;
    public static ERROR = 1;
}
