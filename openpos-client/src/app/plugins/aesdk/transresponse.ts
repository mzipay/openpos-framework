import {Parser} from 'xml2js';
import { ResponseBase } from './responsebase';

export class TransResponse extends ResponseBase {
    public static readonly requestType = 'TransRequest';

    protected makeResponseXml(requestJson: any): string {
        const req: any = requestJson[TransResponse.requestType];
        const xml =
`
<TransResponse>
  <POSID>${req.POSID}</POSID>
  <APPID>${req.APPID}</APPID>
  <CCTID>${req.CCTID}</CCTID>
  <TransDetailsData>
    <TransDetailData>
      <CardNumber>511234xxxxxx0065</CardNumber>
      <CardType>MCC</CardType>
      <TransactionTypeCode>1</TransactionTypeCode>
      <CardEntryMode>M</CardEntryMode>
      <KI></KI>
      <KIType></KIType>
      <TransactionAmount>${this.makeTransactionAmount(req)}</TransactionAmount>
      <TipAmount>0</TipAmount>
      <TransactionIdentifier>${this.ticketNumber}</TransactionIdentifier>
      <ResponseCode>00000</ResponseCode>
      <TransactionResponseText>APPROVAL</TransactionResponseText>
      <ApprovalCode>CMC450</ApprovalCode>
      <BalanceAmount>0</BalanceAmount>
      <AuthAVSResult></AuthAVSResult>
      <TransactionSequenceNumber>000002</TransactionSequenceNumber>
      <CardExpiryDate>${this.expiryDateStrMMYY}</CardExpiryDate>
      <CustomerName>John Doe</CustomerName>
      <CustomerTokenNumber>B8E49B4A5547A796635E66676307957A3E4B95CF</CustomerTokenNumber>
      <SignatureReceiptFlag>0</SignatureReceiptFlag>
      <PartialApprovedFlag>N</PartialApprovedFlag>
      <EMVFlag>1</EMVFlag>
      <EMVData>Lang~30[FS]MerchantID~[FS]TerminalID~AURMXD22[FS]Sequence No~001001001001[FS]ARC~00[FS]ISO~00[FS]AID~A0000002771010[FS]TVR~8000008000[FS]TSI~7800[FS]IAD~06010A03642000[FS]App Pref Name~Interac[FS]App Label~[FS]CHName~UAT CAN/Test Card 10      Account Type~Chequing[FS]RespDate~05032017[FS]RespTime~152358</EMVData>
      <VoidData></VoidData>
      <LoyaltyInfo>
        <Usepoints></Usepoints>
        <BalancePoint>0</BalancePoint>
        <Expiry>${this.expiryDateStrMMDDYYYY}</Expiry>
        <LoyaltyLevel></LoyaltyLevel>
      </LoyaltyInfo>
      <ReferralNUM></ReferralNUM>
      <ReferralDialInformation></ReferralDialInformation>
      <OpenToPayCash>0.00</OpenToPayCash>
      <OpenToBuy>0.00</OpenToBuy>
      <ProcessorResponseCode></ProcessorResponseCode>
      <ProcessorMerchantId></ProcessorMerchantId>
      <ForcedComplianceCode></ForcedComplianceCode>
      <CardIdentifier></CardIdentifier>
      <TransactionToken></TransactionToken>
      <ReceiptToken></ReceiptToken>
      <CorpCardToken></CorpCardToken>
      <EcomToken></EcomToken>
      <CRMToken>123456789</CRMToken>
      <ProcessorToken></ProcessorToken>
      <Reserved1></Reserved1>
      <Reserved2></Reserved2>
    </TransDetailData>
  </TransDetailsData>
  <BatchNumber>${req.batchNumber}</BatchNumber>
  <AurusPayTicketNum>${this.ticketNumber}</AurusPayTicketNum>
</TransResponse>
`;
        return xml;
    }

    protected makeTransactionAmount(req: any): string {
        if (req.TransAmountDetails && req.TransAmountDetails[0].TransactionTotal) {
            const transTotal: string = req.TransAmountDetails[0].TransactionTotal[0];
            return transTotal.split('~')[1];
        } else {
            return '29.99';
        }
    }
}
