# OpenposClientCoreLib

This is the parent project for OpenPos Client Core Lib that orchestrats the building and packaging of the core library as well as running the test suite.
Code for the library can be found in `projects\openpos-client-core-lib\src\lib`.

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 7.3.5.

## Develop

## Build

There are 2 components to building this library, building and minifying the angular files and bundling the SASS theme files into a single flat scss file. This normally will be done by the build server but can be done manually one time by running `npm run build-openpos-lib`. This will execute `ng build openpos-client-core-lib` followed by `scss-bundle -c projects/openpos-client-core-lib/scss-bundle.config.json`. This does a production build that places the build artifacts in `/dist/openpos-client-core-lib`

## Publishing

