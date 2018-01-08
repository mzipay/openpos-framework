import { Parser } from 'xml2js';
import { ResponseBase } from './responsebase';

export class CancelLastTransactionResponse extends ResponseBase {
    public static readonly requestType = 'CancelLastTransactionRequest';

    protected makeResponseXml(requestJson: any): string {
        const req: any = requestJson[CancelLastTransactionResponse.requestType];
        const xml =
`
<CancelLastTransactionResponse>
    <POSID>${req.POSID}</POSID>
    <APPID>${req.APPID}</APPID>
    <CCTID>${req.CCTID}</CCTID>
    <ResponseCode>00000</ResponseCode>
    <ResponseText>Approved</ResponseText>
</CancelLastTransactionResponse>
`;
        return xml;
    }
}
