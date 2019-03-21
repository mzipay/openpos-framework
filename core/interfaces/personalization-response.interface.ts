import { PersonalizationProperty } from './personalization-property.interface.';

export interface PersonalizationResponse {
    success: boolean;
    message?: string;
    properties?: PersonalizationProperty[];
}
