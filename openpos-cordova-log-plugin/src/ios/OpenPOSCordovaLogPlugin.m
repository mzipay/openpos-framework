#import "OpenPOSCordovaLogPlugin.h"

#import <Cordova/CDVAvailability.h>

@interface OpenPOSCordovaLogPlugin()
@property (strong, nonatomic) UIDocumentInteractionController *documentInteractionController;
@property (strong, nonatomic) UIActivityViewController *activityViewController;
/* Date components for last time date was checked in order to know when to roll the current log file. */
@property (strong, nonatomic) NSDateComponents *_lastCheckedDateParts;

@property (strong, nonatomic) NSString *_curLogFileName;
@property (nonatomic) FILE *_curLogFile;

- (NSString *) getOrCreateLogsDir;
- (NSString *) logFileNameUsingDate: (NSDate *)someDate;
- (NSString *) logFileName: (NSString *)fileName;
- (void) captureConsoleOutputToOpenPOSLogFile;
- (void)finishLaunching:(NSNotification *)notification;
@end

@implementation OpenPOSCordovaLogPlugin

- (NSArray *)listLogFiles:(CDVInvokedUrlCommand *)command {
    NSString *logFilesDir = [self getOrCreateLogsDir];
    NSFileManager *fm = [NSFileManager defaultManager];
    
    NSArray *logFilesDirContents = [fm contentsOfDirectoryAtPath:logFilesDir error:nil];
    NSPredicate *fltr = [NSPredicate predicateWithFormat:@"self ENDSWITH '.log'"];
    NSArray *logFileNames = [logFilesDirContents filteredArrayUsingPredicate:fltr];

    CDVPluginResult *result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsArray:logFileNames];
    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
    
    return logFileNames;
}

/* Returns path to given log file */
- (NSString *)getLogFilePath:(CDVInvokedUrlCommand *)command {
    NSString* logFilename = [command.arguments objectAtIndex:0];
    NSString* logFilePath = [self logFileName:logFilename];
    CDVPluginResult *result;

    BOOL fileExists = [[NSFileManager defaultManager] fileExistsAtPath:logFilePath];
    if (fileExists) {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:logFilePath];
    } else {
        NSString* errorMsg = [NSString stringWithFormat:@"Log file '%@' not found.",logFilename];
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:errorMsg];
        logFilePath = nil;
    }

    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
    return logFilePath;
}

- (NSString *)readLogFileContents:(CDVInvokedUrlCommand *)command {
    NSString* logFilename = [command.arguments objectAtIndex:0];
    NSString* logFilePath = [self logFileName:logFilename];
    
    CDVPluginResult *result;
    NSString *logfileContent;
    
    BOOL fileExists = [[NSFileManager defaultManager] fileExistsAtPath:logFilePath];
    if (fileExists) {
        logfileContent = [NSString stringWithContentsOfFile:logFilePath encoding:NSUTF8StringEncoding error:nil];
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:logfileContent];
    } else {
        NSString* errorMsg = [NSString stringWithFormat:@"Log file '%@' not found.",logFilename];
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:errorMsg];
        logfileContent = nil;
    }

    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
    return logfileContent;
}

- (void) shareLogFile:(CDVInvokedUrlCommand *)command {
    NSString* logFilename = [command.arguments objectAtIndex:0];
    NSString* logFilePath = [self logFileName:logFilename];
    
    CDVPluginResult *result;
    
    BOOL fileExists = [[NSFileManager defaultManager] fileExistsAtPath:logFilePath];
    if (fileExists) {
        NSURL *logFileURL = [NSURL fileURLWithPath:logFilePath];
        BOOL shareSuccess = [self shareViaActivityView: logFileURL];
        if (! shareSuccess) {
            NSString *msg = @"Failed to resolve an app that can share a text file";
            NSLog(@"%@",msg);
            result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:msg];
        } else {
            result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
        }
    } else {
        NSString* errorMsg = [NSString stringWithFormat:@"Log file '%@' not found.",logFilename];
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:errorMsg];
    }
    
    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

/* ***************************************************************
   Cordova overridden methods
   ***************************************************************
*/
- (void)pluginInitialize {

    /* Start thread to check when it's time to roll the current log file. */
    if ([self isCapturingLogOutputEnabled]) {
        [self startRollLogCheckTask];
    }
    NSLog(@"OpenPOSCordovaLogPlugin intializing...");
    
    // Hook into the notification for when the application launch finishes, so
    // that we can divert logging to our own file
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(finishLaunching:) name:UIApplicationDidFinishLaunchingNotification object:nil];
    
    NSLog(@"OpenPOSCordovaLogPlugin initialization complete.");
}

- (void) finishLaunching:(NSNotification *)notification {
    // Only redirect if stderr isn't already associated with a 'terminal' (e.g., a console)
    if ([self isCapturingLogOutputEnabled]) {
        [self captureConsoleOutputToOpenPOSLogFile];
    }
}

/* ***************************************************************
    Helper methods
   ***************************************************************
*/
- (BOOL) shareViaActivityView: (NSURL *) logFileURL {
    NSString *shareString = @"";
    CGRect screenRect = [[[super viewController] view] bounds];
    CGFloat screenWidth = screenRect.size.width;
    CGFloat screenHeight = screenRect.size.height;
    
    NSArray *activityItems = [NSArray arrayWithObjects:shareString, logFileURL, nil];
    
    self.activityViewController = [[UIActivityViewController alloc] initWithActivityItems:activityItems applicationActivities:nil];
    
    //if iPhone
    if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone) {
        [[super viewController] presentViewController:self.activityViewController animated:YES completion:nil];
    } else { // iPad
        // Change Rect to position Popover
        UIPopoverController *popup = [[UIPopoverController alloc] initWithContentViewController:self.activityViewController];
        /* TODO: switch over to PopOverPresentationController when time permits to eliminate deprecation warnings
         See http://pinkstone.co.uk/how-to-create-popovers-in-ios-9/
        UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
        UIViewController *controller = [storyboard instantiateViewControllerWithIdentifier:@"Pop"];
        controller.modalPresentationStyle = UIModalPresentationPopover;
        */
        [popup presentPopoverFromRect:CGRectMake(screenWidth/2,screenHeight,0,0) inView:[[super viewController] view] permittedArrowDirections:UIPopoverArrowDirectionAny animated:YES];
    }
    /*
    if(SYSTEM_VERSION_GREATER_THAN_OR_EQUAL_TO(@"8.0")){
        self.activityViewController.popoverPresentationController.sourceView = [[super viewController] view];
        self.activityViewController.popoverPresentationController.sourceRect = CGRectMake(screenWidth/2,screenHeight,0,0);
    }
    self.activityViewController.modalTransitionStyle = UIModalTransitionStyleCoverVertical;
    [[super viewController] presentViewController:self.activityViewController animated:YES completion:nil];
     */
    return YES;
}

- (BOOL) shareViaOptionsMenu: (NSURL *) logFileURL {
    self.documentInteractionController = [UIDocumentInteractionController interactionControllerWithURL:logFileURL];
    self.documentInteractionController.delegate = self;
    self.documentInteractionController.UTI = @"txt";
    
    CGRect rect = CGRectMake(0, 0, self.viewController.view.bounds.size.width, self.viewController.view.bounds.size.height);
    BOOL openInSuccess = [self.documentInteractionController presentOptionsMenuFromRect:rect inView:self.viewController.view animated:YES];
    return openInSuccess;
}

/* Checks if there is a tty console that we're logging to.  If so, returns false. */
- (BOOL) isCapturingLogOutputEnabled {
    return isatty(STDERR_FILENO) != 1;
}


- (NSString *) getOrCreateLogsDir {
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,NSUserDomainMask, YES);
    
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSString *logsDirectory = [documentsDirectory stringByAppendingPathComponent:[NSString stringWithFormat:@"%@",@"Logs"]];
    
    NSError *error = nil;
    [[NSFileManager defaultManager] createDirectoryAtPath:logsDirectory
                                    withIntermediateDirectories:YES
                                    attributes:nil
                                    error:&error];
    if (error != nil) {
        NSLog(@"Error creating Logs directory at %@: %@", logsDirectory, error);
    }
    
    return logsDirectory;
}

/* Returns a fully qualified log file name based on the given date. */
- (NSString *) logFileNameUsingDate: (NSDate *)someDate {
    NSString *logsDirectory = [self getOrCreateLogsDir];
    
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"yyyy-MM-dd"];
    NSString *fileName = [NSString stringWithFormat:@"openpos_%@.log",[dateFormatter stringFromDate: someDate]];
    NSString *logFilePath = [logsDirectory stringByAppendingPathComponent: fileName];
    return logFilePath;
}

- (NSString *) logFileName: (NSString *)fileName {
    NSString *logsDirectory = [self getOrCreateLogsDir];
    NSString *logFilePath = [logsDirectory stringByAppendingPathComponent: fileName];
    return logFilePath;
}

/* Makes a temp copy of the current log file, redirects output to a new file whose name is based on the current date,
   and renames the temp copy of the current log to its prior name. */
- (void) rollOpenPOSLogFile {
    if (self._curLogFile != nil) {
        NSString *logFilePathCurrentDate = [self logFileNameUsingDate: [NSDate date]];
        
        NSFileManager *fileManager = [NSFileManager defaultManager];
        NSString *tempFileName = [NSString stringWithFormat:@"%@%@", self._curLogFileName, @".2"];
        [fileManager copyItemAtPath:self._curLogFileName toPath:tempFileName  error:NULL];
        self._curLogFile = freopen([logFilePathCurrentDate cStringUsingEncoding:NSASCIIStringEncoding],"a+",self._curLogFile);
        [fileManager moveItemAtPath:tempFileName toPath:self._curLogFileName error:NULL];
        self._curLogFileName = logFilePathCurrentDate;
    }
    // TODO? Could also prune log files older than X days here
}

- (void) captureConsoleOutputToOpenPOSLogFile {
    // Get log file for current date
    NSString *logFilePathCurrentDate = [self logFileNameUsingDate: [NSDate date]];
    
    // redirect stderr to log file
    self._curLogFile = freopen([logFilePathCurrentDate cStringUsingEncoding:NSASCIIStringEncoding],"a+",stderr);
    self._curLogFileName = logFilePathCurrentDate;
}

- (UIViewController *) documentInteractionControllerViewControllerForPreview:(UIDocumentInteractionController *)controller {
    return self.viewController;
}

- (void)startRollLogCheckTask {
    [NSTimer scheduledTimerWithTimeInterval:15.0 target:self selector:@selector(backgroundCheckToRollLog) userInfo:nil repeats:YES];
}

- (void)backgroundCheckToRollLog {
    if ([self isCapturingLogOutputEnabled]) {
        NSDateComponents *currentDateParts = [[NSCalendar currentCalendar] components:NSCalendarUnitDay | NSCalendarUnitMonth | NSCalendarUnitYear fromDate:[NSDate date]];
        if (self._lastCheckedDateParts == nil) {
            self._lastCheckedDateParts = currentDateParts;
        }
        // If the day changed, need to roll the log file to a new file
        if ([currentDateParts day] != [self._lastCheckedDateParts day]) {
            self._lastCheckedDateParts = currentDateParts;
            NSLog(@"Date has changed, rolling the log file...");
            [self rollOpenPOSLogFile]; // roll the log file
        }

    }
}
    
@end
