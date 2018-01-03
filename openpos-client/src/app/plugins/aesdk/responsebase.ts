import {Parser} from 'xml2js';

export abstract class ResponseBase {
    requestJsonPromise: Promise<any>;
    protected expiryDateStrMMYY: string;
    protected expiryDateStrMMDDYYYY: string;
    protected ticketNumber: string;

    constructor(public requestXml: string) {
        const parser = new Parser();

        this.requestJsonPromise = new Promise<any>((resolve, reject) => {
            parser.parseString(this.requestXml, (err, result) => {
                if (result) {
                    console.log(`Request JSON: ${JSON.stringify(result)}`);
                }
                resolve(result);
            });
        });

        this.initSimData();
    }

    protected initSimData() {
        // Make expiryDate 2 years into the future
        const curDate = new Date();
        const monthStr = (curDate.getMonth() <= 8 ? '0' : '') + `${(curDate.getMonth() + 1)}`;
        const dayStr = (curDate.getDate() <= 9 ? '0' : '') + curDate.getDate();
        this.expiryDateStrMMYY = `${monthStr}${(curDate.getFullYear() % 100) + 2}`;
        this.expiryDateStrMMDDYYYY = `${monthStr}${dayStr}${curDate.getFullYear() + 2}`;

        // Create a semi-random Aurus ticket number
        let ticketNum = Math.floor((Math.random() * 100000000) + 1);
        ticketNum += 1011000000000;
        this.ticketNumber = ticketNum + '';
    }

    protected abstract makeResponseXml(requestJson: any): string;

    getResponseXml(): Promise<string> {

        const responseXmlPromise = new Promise<string>((resolve, reject) => {
            this.requestJsonPromise.then( requestJson => {
                resolve(this.makeResponseXml(requestJson));
            });
        });

        return responseXmlPromise;
    }
}
