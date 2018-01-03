
var exec = require('cordova/exec');

const PLUGIN_NAME = 'OpenPOSCordovaLogPlugin';

var OpenPOSCordovaLogPlugin = {
  pluginId: 'openPOSCordovaLogPlugin',
  pluginName: PLUGIN_NAME,
  
  init: function(successCallback, errorCallback) {
    // Nothing to init for this plugin
    successCallback();
  },
  
  /** 
   * Lists the name of all the files ending with *.log in the Logs directory.
   * 
   * successCallback - a single parameter callback that will receive an
   *   array of log file names found in the Logs directory
   * errorCallback - a single parameter callback which will receive an error message if
   *   there was a failure. 
   */
  listLogFiles: function(successCallback, errorCallback ) {
    exec(
        successCallback,
        errorCallback,
        PLUGIN_NAME, 
        'listLogFiles', []
      );
  },
  
  
  /** 
   * If the file exists, the successCallback will return the contents
   * of the file as a string.
   * 
   * logFilename - just the name of the logfile, no path information
   * successCallback - a single parameter callback that will receive the contents of the
   *   logfile as a string
   * errorCallback - a single parameter callback which will receive an error message if
   *   there was a failure in reading the file. 
   */
  readLogFileContents: function(logFilename, successCallback, errorCallback ) {
    exec(
        successCallback, 
        errorCallback,
        PLUGIN_NAME, 
        'readLogFileContents', [logFilename]
      );
  },
  
  /**
   * Provide the user with an option to share the given log file using
   * standard OS sharing mechanism.
   */
  shareLogFile: function(logFilename, successCallback, errorCallback ) {
    exec(
        successCallback, 
        errorCallback,
        PLUGIN_NAME, 
        'shareLogFile', [logFilename]
      );
  }
  

};

module.exports = OpenPOSCordovaLogPlugin;
