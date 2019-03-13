import { IScreen } from '../components/dynamic-screen/screen.interface';
import { IRegistryItem } from './registry-item.interface';

export interface IScreenRegistryItem extends IRegistryItem<IScreen> {
    name: string;
}
