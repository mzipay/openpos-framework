import {Parser} from 'xml2js';
import { ResponseBase } from './responsebase';

export class GetStoredBatchResponse extends ResponseBase {
    public static readonly requestType = 'GetStoredBatchRequest';

    protected makeResponseXml(requestJson: any): string {
        const req: any = requestJson[GetStoredBatchResponse.requestType];
        const xml =
`
<GetStoredBatchResponse>
    <POSID>${req.POSID}</POSID>
    <APPID>${req.APPID}</APPID>
    <CCTID>${req.CCTID}</CCTID>
    <ResponseCode>00000</ResponseCode>
    <ResponseText>Approved</ResponseText>
</GetStoredBatchResponse>
`;
        return xml;
    }

}
