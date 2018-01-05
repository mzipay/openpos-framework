import {Parser} from 'xml2js';
import { ResponseBase } from './responsebase';

export class GetAccountLookupResponse extends ResponseBase {
    public static readonly requestType = 'GetAccountLookupRequest';

    protected makeResponseXml(requestJson: any): string {
        const req: any = requestJson[GetAccountLookupResponse.requestType];
        const xml =
`
<GetAccountLookupResponse>
  <POSID>${req.POSID}</POSID>
  <APPID>${req.APPID}</APPID>
  <CCTID>${req.CCTID}</CCTID>
  <Header>61</Header>
  <LookupType>AC</LookupType>
  <ProcessorResponseCode>0</ProcessorResponseCode>
  <AccountAttributes>
    <Account>
      <CardToken>XXXXXXXXXXXX1111</CardToken>
      <CardNumber></CardNumber>
      <CardType>VIC</CardType>
      <FirstName>Joe</FirstName>
      <AddressLine1>123 Main Street</AddressLine1>
      <City>Metropolis</City>
      <StateCode>NY</StateCode>
      <Country>US</Country>
      <ZipCode>55555</ZipCode>
      <HomePhone>614-555-1212</HomePhone>
    </Account>
  </AccountAttributes>
  <ResponseCode>00000</ResponseCode>
  <ResponseText>Approved</ResponseText>
</GetAccountLookupResponse>
`;
        return xml;
    }

}
