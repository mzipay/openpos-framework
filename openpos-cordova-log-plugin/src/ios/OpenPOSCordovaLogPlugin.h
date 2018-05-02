#import <Cordova/CDVPlugin.h>

@interface OpenPOSCordovaLogPlugin : CDVPlugin<UIDocumentInteractionControllerDelegate> {

}

/* Configure the plugin with values such as the log file suffix/extension
   and the subdir to write the log files into. */
- (void) configure:(CDVInvokedUrlCommand *)command;

/* Return list of files in the Logs dir */
- (NSArray *)listLogFiles:(CDVInvokedUrlCommand *)command;

/* Reads and returns the contents of a given log file */
- (NSString *)readLogFileContents:(CDVInvokedUrlCommand *)command;

/* Returns path to given log file */
- (NSString *)getLogFilePath:(CDVInvokedUrlCommand *)command;

/* Shares a log file using platform standard file sharing UI */
- (void) shareLogFile:(CDVInvokedUrlCommand *)command;

/* Returns iOS Application version */
- (NSString *)getAppVersion:(CDVInvokedUrlCommand *)command;

/* Returns the name of the current log file */
- (NSString *) getCurrentLogFilePath:(CDVInvokedUrlCommand *)command;

@end
