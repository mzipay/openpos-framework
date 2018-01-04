# openpos-client-core-lib

## Setup Development Environment
Run `npm install`
Run `npm link`

## Build library
Run `npm run build` to build once
Run `npm run build:watch` to build and watch for file changes

## Link Library to client app for development
Run `npm link openpos-client-core-lib` after installing librarys with either yarn install or npm install

## Debugging the library
To debug typscript in the library you'll need to set your breakpoints in the `temp/src-inlined` files since this is what is linked to the client app.
