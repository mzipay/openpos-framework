# OpenposClientCoreLib

This is the parent project for OpenPos Client Core Lib that orchestrats the building and packaging of the core library as well as running the test suite.
Code for the library can be found in `projects\openpos-client-core-lib\src\lib`.

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 7.3.5.

## Develop

High level development process is to run Grunt from the project root directory. This will concurrently run the angular build task and scss bundle task in watch mode. Then if you haven't done it yet `cd dist\openpos-client-core-lib` and run `npm link` to create a npm link from your `dist/openpos-client-core-lib` directory. Then if you clients gradle develop does the link you can just run that and it'll link up to your node_modules. If your gradle develop doesn't do that for you run `npm link @jumpind\openpos-client-core-lib` from your client project root. From there just run `ng serve` from your client and you should see that any file change you make from the core will trigger the core build and a build of you client. You may see it trigger a few builds due to the multistep build process on the core to support various polyfills. If you want to be able to step through core typescript files in the browser use `ng serve --vendorSourceMap` to serve up the type definitions.

## Build

There are 2 components to building this library, building and minifying the angular files and bundling the SASS theme files into a single flat scss file. This normally will be done by the build server but can be done manually one time by running `npm run build-openpos-lib`. This will execute `ng build openpos-client-core-lib` followed by `scss-bundle -c projects/openpos-client-core-lib/scss-bundle.config.json`. This does a production build that places the build artifacts in `/dist/openpos-client-core-lib`

## Publishing
