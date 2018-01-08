import { Parser } from 'xml2js';
import { ResponseBase } from './responsebase';

export class GetCardBINResponse extends ResponseBase {
    public static readonly requestType = 'GetCardBINRequest';

    protected makeResponseXml(requestJson: any): string {
        const req = requestJson[GetCardBINResponse.requestType];
        const xml =
`
<GetCardBINResponse>
    <POSID>${req.POSID}</POSID>
    <APPID>${req.APPID}</APPID>
    <CCTID>${req.CCTID}</CCTID>
    <KI></KI>
    <KIType></KIType>
    <CardType>VIC</CardType>
    <CardToken>411111XXXXXX1111</CardToken>
    <CardEntryMode>I</CardEntryMode>
    <CardTokenDetailData></CardTokenDetailData>
    <FirstName>John</FirstName>
    <LastName>Mathew</LastName>
    <CardExpiryDate>${this.expiryDateStrMMYY}</CardExpiryDate>
    <CustomerInfoValidationResult>0101010101</CustomerInfoValidationResult>
    <CustomerIdentifier></CustomerIdentifier>
    <Level3Capable>N</Level3Capable>
    <ProcessorToken>123456980890</ProcessorToken>
    <ResponseCode>00000</ResponseCode>
    <ResponseText>Approved</ResponseText>
</GetCardBINResponse>
`;
        return xml;
    }
}
