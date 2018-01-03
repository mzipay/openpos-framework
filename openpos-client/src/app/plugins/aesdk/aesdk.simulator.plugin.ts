import { TransResponse } from './transresponse';
import { GetCardBINResponse } from './getcardbinresponse';
import { CloseTransactionResponse } from './closetransactionresponse';
import { GetStatusResponse } from './getstatusresponse';
import { IDeviceRequest } from './../../common/idevicerequest';
import { IDevicePlugin } from './../../common/idevice-plugin';
import { ShowScreenResponse } from './showscreenresponse';
export class AESDKSimulatorPlugin implements IDevicePlugin {

    pluginId = 'aesdkSimulatorPlugin';
    pluginName = this.pluginId;
    impl: any;
    init(successCallback: () => void, errorCallback: (error?: string) => void): void {
        // throw new Error("Method not implemented.");
    }

    processRequest(request: IDeviceRequest, successCallback: (response: string) => void , errorCallback: (error: string) => void) {

        let responseXmlPromise: Promise<string> = null;
        switch (request.subType) {
            case 'CloseTransactionRequest':
                responseXmlPromise = (new CloseTransactionResponse(request.payload)).getResponseXml();
                break;
            case 'GetCardBINRequest':
                responseXmlPromise = (new GetCardBINResponse(request.payload)).getResponseXml();
                break;
            case 'GetStatusRequest':
                responseXmlPromise = (new GetStatusResponse(request.payload)).getResponseXml();
                break;
            case 'ShowScreenRequest':
                responseXmlPromise = (new ShowScreenResponse(request.payload)).getResponseXml();
                break;
            case 'TransRequest':
                responseXmlPromise = (new TransResponse(request.payload)).getResponseXml();
                break;
            default:
        }

        if (responseXmlPromise) {
            responseXmlPromise.then(responseXml => successCallback(responseXml));
        } else {
            errorCallback(`No handling available yet for ${request.subType}`);
        }
    }
}
