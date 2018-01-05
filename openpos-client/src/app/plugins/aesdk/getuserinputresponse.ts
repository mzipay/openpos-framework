import {Parser} from 'xml2js';
import { ResponseBase } from './responsebase';

export class GetUserInputResponse extends ResponseBase {
    public static readonly requestType = 'GetUserInputRequest';

    protected makeResponseXml(requestJson: any): string {
        const req: any = requestJson[GetUserInputResponse.requestType];
        const xml =
`
<GetUserInputResponse>
    <POSID>${req.POSID}</POSID>
    <APPID>${req.APPID}</APPID>
    <CCTID>${req.CCTID}</CCTID>
    <InputData>4274534680356947846970498</InputData>
    <ButtonSelection>1</ButtonSelection>
    <ResponseCode>00000</ResponseCode>
    <ResponseText>Approved</ResponseText>
</GetUserInputResponse>
`;
        return xml;
    }

}
