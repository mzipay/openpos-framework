
export interface ITransactionSummary {
    transactionId: string;
    description: string;
    grandTotal: string;
    workstationId: string;
    storeNumber: string;
    location: string;
    transactionDate: string;
    numberUnitsSold: number;
}