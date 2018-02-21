export interface IFormatter {
    locale?: string;
    keyFilter: RegExp | IKeyFilter;
    formatValue( value: string ): string;
    unFormatValue( value: string ): string;
}

export interface IKeyFilter {
    /**
     * Returns true if the given inputChar should be filtered, false if not.
     * @param valueBefore The value of the targetElement prior to the given inputChar
     * @param inputChar The current inputCharacter to evaluate
     */
    filter(valueBefore: string, inputChar: string): boolean;
}
