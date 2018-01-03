import {Parser} from 'xml2js';
import { ResponseBase } from './responsebase';

export class ShowScreenResponse extends ResponseBase {
    reqType = 'ShowScreenRequest';

    protected makeResponseXml(requestJson: any): string {
        const req: any = requestJson[this.reqType];
        const xml =
`
<ShowScreenResponse>
    <POSID>${req.POSID}</POSID>
    <APPID>${req.APPID}</APPID>
    <CCTID>${req.CCTID}</CCTID>
    <ButtonReturn>01</ButtonReturn>
    <ResponseCode>00000</ResponseCode>
    <ResponseText>Approved</ResponseText>
</ShowScreenResponse>
`;
        return xml;
    }

}
