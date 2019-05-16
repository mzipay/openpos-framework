# Platforms

Platforms represent the container that the Openpos app runs. Examples would be Cordova, Electron, ReactNative.

All platforms should implement the `IPlatformInterface` and be made `@Injectable`.

## Initialization

Platforms get picked up by the Startup process when a platforms is provided to the `PLATFORMS` injection token.

```typescript
providers: [
    { provide: PLATFORMS, useExisting: CordovaPlatform, multi: true}
    ]
```

The startup up task task will check if the platform is present