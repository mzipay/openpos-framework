export interface IFormatter {
    allowKey( key: string, newValue: string ) : boolean;
    formatValue( value: string ): string;
    unFormatValue( value: string ): string;
}
