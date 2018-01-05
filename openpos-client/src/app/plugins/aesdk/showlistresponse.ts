import {Parser} from 'xml2js';
import { ResponseBase } from './responsebase';

export class ShowListResponse extends ResponseBase {
    public static readonly requestType = 'ShowListRequest';

    protected makeResponseXml(requestJson: any): string {
        const req: any = requestJson[ShowListResponse.requestType];
        const xml =
`
<ShowListResponse>
    <POSID>${req.POSID}</POSID>
    <APPID>${req.APPID}</APPID>
    <CCTID>${req.CCTID}</CCTID>
    <ButtonSelection>01</ButtonSelection>
    <ResponseCode>00000</ResponseCode>
    <ResponseText>Approved</ResponseText>
  </ShowListResponse>
`;
        return xml;
    }

}
