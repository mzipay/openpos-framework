import { IRegistryItem } from './registry-item.interface';
import { IScreen } from '../components/dynamic-screen/screen.interface';

export interface IDialogRegistryItem extends IRegistryItem<IScreen> {
    name: string;
}
