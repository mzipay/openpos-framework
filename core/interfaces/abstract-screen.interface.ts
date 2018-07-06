import { IAbstractScreenTemplate } from './abstract-screen-template.interface';

export interface IAbstractScreen {
    name: string;
    type: string;
    template: IAbstractScreenTemplate;
    locale: string; 
}