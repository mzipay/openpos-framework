export interface IPlugin {
    pluginId: string;
    pluginName?: string;

    init(): void;
}
