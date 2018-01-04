import { Parser } from 'xml2js';
import { ResponseBase } from './responsebase';

export class CloseTransactionResponse extends ResponseBase {
    reqType = 'CloseTransactionRequest';

    protected makeResponseXml(requestJson: any): string {
        const req: any = requestJson[this.reqType];
        const xml =
`
<CloseTransactionResponse>
    <POSID>${req.POSID}</POSID>
    <APPID>${req.APPID}</APPID>
    <CCTID>${req.CCTID}</CCTID>

    <AurusPayTicketNum>12345678</AurusPayTicketNum>
    <TransactionIdentifier>10001</TransactionIdentifier>

    <ResponseCode>00000</ResponseCode>
    <ResponseText>Approved</ResponseText>
</CloseTransactionResponse>
`;
        return xml;
    }
}
