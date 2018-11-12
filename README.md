# OpenposClientCoreLib

This is the core library for openpos and contains all of the common functionality and screen for open pos. 

## Add Openpos-client-core-lib to package.json
To use this library add to following to your dependencies in `package.json`
```json
"@jumpmind/openpos-client-core-lib": "github:JumpMind/openpos-client-core-lib"
```

## Development Setup
For development on the library you'll want to link your client application to openpos-client-core-lib by the following steps.

Run the grunt command from the openpos-client-core-lib directory.  If grunt is not installed, install it by running:

`npm install grunt --save-dev`

The grunt task will copy core lib changes out to a common location and then it will link that location to npm.

Lastly, from your client application run `npm link @jumpmind/openpos-client-core-lib` to add a link to openpos-client-core-lib in your client node_modules.

## Debugging the core library
In your launch.json file configuration section the following to change the mapping from the client node modules to the core source code
```json
"sourceMapPathOverrides": {
      "webpack:/<path-to-client>/node_modules/openpos-core/*": "<path-to-open-pos.git>/openpos-client-core-lib/src/app/openpos-core/*"    
  }
```
