import { ISearchResult } from './search-result.interface';

export interface ICustomerLookupResultsScreen {
    customers: ISearchResult[];
    selectedCustomerIndex: number;
    hasLoyaltyCustomerLinked: boolean;
    continueText: string;
}
