export interface IPlugin {
    pluginId: string;
    pluginName?: string;
    impl: any;

	// TODO: change to a Promise?
    init(successCallback: () => void, errorCallback: (error?: string) => void): void;
}
