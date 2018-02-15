export interface IFormatter {
    formatValue( value: string ): string;
    unFormatValue( value: string ): string;
    keyFilter: RegExp;
}