# openpos-client-core-lib


## Setup Development Environment
`npm install yarn`

Run `yarn install`
Run `yarn link`

## Build library
Run `yarn build` to build once
Run `yarn build:watch` to build and watch for file changes

## Link Library to client app for development
Run `yarn link openpos-client-core-lib` after installing librarys with either yarn install or npm install

## Debugging the library
To debug typscript in the library you'll need to set your breakpoints in the `temp/src-inlined` files since this is what is linked to the client app.
