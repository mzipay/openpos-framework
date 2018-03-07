export interface IPlugin {
    pluginId: string;
    pluginName?: string;
    impl: any;
    config?: any;

    configure?(params: any): boolean;
	// TODO: change to a Promise?
    init(successCallback: () => void, errorCallback: (error?: string) => void): void;
}
