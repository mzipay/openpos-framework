import {Parser} from 'xml2js';
import { ResponseBase } from './responsebase';

export class UploadStoredBatchResponse extends ResponseBase {
    public static readonly requestType = 'UploadStoredBatchRequest';

    protected makeResponseXml(requestJson: any): string {
        const req: any = requestJson[UploadStoredBatchResponse.requestType];
        const xml =
`
<UploadStoredBatchResponse>
    <POSID>${req.POSID}</POSID>
    <APPID>${req.APPID}</APPID>
    <CCTID>${req.CCTID}</CCTID>
    <ApprovedCount>1</ApprovedCount>
    <DeclinedCount>0</DeclinedCount>
    <PendingCount>0</PendingCount>
    <PendingReason></PendingReason>
    <CreditSettlementStatus></CreditSettlementStatus>
    <DebitSettlementStatus></DebitSettlementStatus>
    <ResponseCode>00000</ResponseCode>
    <ResponseText>Approved</ResponseText>
</UploadStoredBatchResponse>
`;
        return xml;
    }

}
