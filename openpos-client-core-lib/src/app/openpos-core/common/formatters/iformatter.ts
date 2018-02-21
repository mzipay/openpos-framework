export interface IFormatter {
    locale?: string;
    allowKey( key: string, newValue: string ) : boolean;
    formatValue( value: string ): string;
    unFormatValue( value: string ): string;
}
