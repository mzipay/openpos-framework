import { IField } from './ifield';
export interface IItem {
    index: number;
    id: string;
    description: string;
    subtitle: string;
    amount: string;
    fields: IField[];
}
