import { UploadStoredBatchResponse } from './uploadstoredbatchresponse';
import { GetUserInputResponse } from './getuserinputresponse';
import { ShowListResponse } from './showlistresponse';
import { GetStoredBatchResponse } from './getstoredbatchresponse';
import { CCTTicketDisplayResponse } from './cctticketdisplayresponse';
import { CancelLastTransactionResponse } from './cancellasttransresponse';
import { ByPassScreenResponse } from './bypassscreenresponse';
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
            case ByPassScreenResponse.requestType:
                responseXmlPromise = (new ByPassScreenResponse(request.payload)).getResponseXml();
                break;
            case CancelLastTransactionResponse.requestType:
                responseXmlPromise = (new CancelLastTransactionResponse(request.payload)).getResponseXml();
                break;
            case CCTTicketDisplayResponse.requestType:
                responseXmlPromise = (new CCTTicketDisplayResponse(request.payload)).getResponseXml();
                break;
            case CloseTransactionResponse.requestType:
                responseXmlPromise = (new CloseTransactionResponse(request.payload)).getResponseXml();
                break;
            case GetCardBINResponse.requestType:
                responseXmlPromise = (new GetCardBINResponse(request.payload)).getResponseXml();
                break;
            case GetStatusResponse.requestType:
                responseXmlPromise = (new GetStatusResponse(request.payload)).getResponseXml();
                break;
            case GetStoredBatchResponse.requestType:
                responseXmlPromise = (new GetStoredBatchResponse(request.payload)).getResponseXml();
                break;
            case GetUserInputResponse.requestType:
                responseXmlPromise = (new GetUserInputResponse(request.payload)).getResponseXml();
                break;
            case ShowListResponse.requestType:
                responseXmlPromise = (new ShowListResponse(request.payload)).getResponseXml();
                break;
            case ShowScreenResponse.requestType:
                responseXmlPromise = (new ShowScreenResponse(request.payload)).getResponseXml();
                break;
            case TransResponse.requestType:
                responseXmlPromise = (new TransResponse(request.payload)).getResponseXml();
                break;
            case UploadStoredBatchResponse.requestType:
                responseXmlPromise = (new UploadStoredBatchResponse(request.payload)).getResponseXml();
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
