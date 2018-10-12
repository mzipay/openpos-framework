import { IAbstractScreen } from '../../core/interfaces/abstract-screen.interface';

export interface IDetailTextScreen extends IAbstractScreen {
    text: string;
    title: string;
}
