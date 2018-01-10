# OpenposClientCoreLib

This is the core library and demo app for openpos. The share library can is located in `src\app\openpos-core\` and contains all of the common functionality and screen for open pos. 

## Development Setup
For development on the library you'll want to link your consumer application to the openpos-core library by the following steps.

Run `npm install` from `src\app\openpos-core\` to install all of the library dependencies.
Run `npm link` from `src\app\openpos-core\` to create a link to openpos-core in your local cache

Then from your consumer application run `npm link openpos-core` to add a link to openpos-core in your consumers node_modules.

Consumers of this library should be set up using the `openpos-client-seed` project.

## Debugging the core library
In your launch.json file configuration section the following to change the mapping from the client node modules to the core source code
```json
"sourceMapPathOverrides": {
      "webpack:/<path-to-client>/node_modules/openpos-core/*": "<path-to-open-pos.git>/openpos-client-core-lib/src/app/openpos-core/*"    
  }
```
