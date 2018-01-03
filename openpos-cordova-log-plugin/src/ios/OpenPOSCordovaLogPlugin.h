#import <Cordova/CDVPlugin.h>

@interface OpenPOSCordovaLogPlugin : CDVPlugin<UIDocumentInteractionControllerDelegate> {

}

/* Return list of files in the Logs dir */
- (NSArray *)listLogFiles:(CDVInvokedUrlCommand *)command;

/* Reads and returns the contents of a given log file */
- (NSString *)readLogFileContents:(CDVInvokedUrlCommand *)command;

/* Shares a log file using platform standard file sharing UI */
- (void) shareLogFile:(CDVInvokedUrlCommand *)command;

@end
