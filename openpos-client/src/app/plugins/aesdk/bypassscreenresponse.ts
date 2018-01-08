import {Parser} from 'xml2js';
import { ResponseBase } from './responsebase';

export class ByPassScreenResponse extends ResponseBase {
    public static readonly requestType = 'ByPassScreenRequest';

    protected makeResponseXml(requestJson: any): string {
        const req: any = requestJson[ByPassScreenResponse.requestType];
        const xml =
`
<ByPassScreenResponse>
    <POSID>${req.POSID}</POSID>
    <APPID>${req.APPID}</APPID>
    <CCTID>${req.CCTID}</CCTID>
    <ResponseCode>00000</ResponseCode>
    <ResponseText>Approved</ResponseText>
  </ByPassScreenResponse>
`;
        return xml;
    }

}
