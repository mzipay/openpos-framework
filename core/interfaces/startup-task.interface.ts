export interface StartupTask {
    name: string;
    order: number;
    execute(): boolean;
}
