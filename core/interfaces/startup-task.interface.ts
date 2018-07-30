export interface IStartupTask {
    name: string;
    order: number;
    execute(): boolean;
}
