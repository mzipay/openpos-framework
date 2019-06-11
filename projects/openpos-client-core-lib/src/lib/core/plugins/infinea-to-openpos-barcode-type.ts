import { OpenposBarcodeType } from './openpos-barcode-type.enum';
import { InfineaBarcodeType } from './infinea-barcode-type.enum';

export class InfineaBarcodeUtils {

    private static infineaToOpenposTypeMap = new Map([
        [InfineaBarcodeType.CODE128, OpenposBarcodeType.CODE128],
        [InfineaBarcodeType.EAN13, OpenposBarcodeType.EAN13],
        [InfineaBarcodeType.UPCA, OpenposBarcodeType.UPCA],
        [InfineaBarcodeType.UPCE, OpenposBarcodeType.UPCE]
    ]);

    static convertToOpenposType( type: InfineaBarcodeType ): OpenposBarcodeType {
        return InfineaBarcodeUtils.infineaToOpenposTypeMap.get(type);
    }
}
