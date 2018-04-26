
var exec = require('cordova/exec');

const PLUGIN_NAME = 'OpenPOSCordovaLogPlugin';

var OpenPOSCordovaLogPlugin = {
  pluginId: 'openPOSCordovaLogPlugin',
  pluginName: PLUGIN_NAME,
  /*  These are now initted through a preference in the cordova config.xml
  config: {
    logDir: 'Logs',
    logSuffix: '.log'
  },
  */

  init: function(successCallback, errorCallback) {
    // Nothing to init for this plugin
    // Make sure plugin at least has default config
    _configure(null);
    successCallback();
  },
  
  configure: function(pluginConfig) {
    _configure(pluginConfig);
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
  readLogFileContents: function(logFilename, successCallback, errorCallback) {
    exec(
        successCallback, 
        errorCallback,
        PLUGIN_NAME, 
        'readLogFileContents', [logFilename]
      );
  },
  

  /** 
   * If the file exists, the successCallback will return the full path
   * of the file as a string.
   * 
   * logFilename - just the name of the logfile, no path information
   * successCallback - a single parameter callback that will receive the full path of the logfile
   *    as a string
   * errorCallback - a single parameter callback which will receive an error message if
   *   there was a failure in locating the file. 
   */
  getLogFilePath: function(logFilename, successCallback, errorCallback) {
    exec(
        successCallback, 
        errorCallback,
        PLUGIN_NAME, 
        'getLogFilePath', [logFilename]
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

function _configure(pluginConfig) {
  /* These settings are now configured through the cordova preferences in config.xml
  if (pluginConfig) {
    if (pluginConfig.logDir !== 'undefined' && pluginConfig.logDir) {
      OpenPOSCordovaLogPlugin.config.logDir = pluginConfig.logDir;
    }

    if (pluginConfig.logSuffix !== 'undefined' && pluginConfig.logSuffix) {
      OpenPOSCordovaLogPlugin.config.logSuffix = pluginConfig.logSuffix;
    }
  }
  
  exec(
    function() {},
    function(error) {},
    PLUGIN_NAME,
    'configure', [OpenPOSCordovaLogPlugin.config.logDir, OpenPOSCordovaLogPlugin.config.logSuffix]
  );
  */
}

module.exports = OpenPOSCordovaLogPlugin;
