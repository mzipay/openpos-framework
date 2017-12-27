export interface IPlugin {
    pluginId: string;
    pluginName?: string;

	// TODO: change to a Promise?
    init(successCallback: () => void, errorCallback: () => void): void;
}
