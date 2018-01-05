import {Parser} from 'xml2js';
import { ResponseBase } from './responsebase';

export class GetStatusResponse extends ResponseBase {
    public static readonly requestType = 'GetStatusRequest';

    protected makeResponseXml(requestJson: any): string {
        const req: any = requestJson[GetStatusResponse.requestType];
        const xml =
`
<GetStatusResponse>
    <POSID>${req.POSID}</POSID>
    <APPID>${req.APPID}</APPID>
    <CCTID>${req.CCTID}</CCTID>
    <StatusType>${req.StatusType}</StatusType>
    <CCTScreenName>Idle Screen</CCTScreenName>
    <CCTVersion>10.22</CCTVersion>
    <AESDKVersion>5.016</AESDKVersion>
    <ResponseCode>00000</ResponseCode>
    <ResponseText>ALIVE</ResponseText>
</GetStatusResponse>
`;
        return xml;
    }

}
