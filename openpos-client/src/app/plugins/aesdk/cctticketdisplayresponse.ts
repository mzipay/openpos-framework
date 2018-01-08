import { Parser } from 'xml2js';
import { ResponseBase } from './responsebase';

export class CCTTicketDisplayResponse extends ResponseBase {
    public static readonly requestType = 'CCTTicketDisplayRequest';

    protected makeResponseXml(requestJson: any): string {
        const req: any = requestJson[CCTTicketDisplayResponse.requestType];
        const xml =
`
<CCTTicketDisplayResponse>
    <POSID>${req.POSID}</POSID>
    <APPID>${req.APPID}</APPID>
    <CCTID>${req.CCTID}</CCTID>
    <ResponseCode>00000</ResponseCode>
    <ResponseText>Approved</ResponseText>
</CCTTicketDisplayResponse>
`;
        return xml;
    }
}
