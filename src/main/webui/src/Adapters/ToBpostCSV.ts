import {Address} from "../Address/Address";
import * as iconv from "iconv-lite";

export class ToBpostCSV {
    static exportToBPostCSV(parcels:Address[],senderHeader:string|undefined): Blob {
        let CSVHeader = [
            "Receiver;;;;;;;;;;Sender;;;;;;;;;;;Parcel;;Options;;;;;;;\n" +
            "Receiver First name (20);Receiver Last name (20);Receiver Company (optional-40);Receiver Street (50);Receiver Number (12);Receiver Box (optional-12);Receiver Postal code (20);Receiver City (40);Receiver Email address (optional-50);Receiver Mobile number (optional-12);Sender First name (20);Sender Last name (20);Sender Company (optional-40);Sender VAT number (optional-12);Sender Street (50);Sender Number (12);Sender Box (optional-12);Sender Postal code (20);Sender City (40);Sender Email address (50);Sender Mobile number (optional-12);Weight kg (6);Your reference (optional-50);Payment by receiver on delivery (optional-1);Payment by receiver on delivery amount (optional-7);Payment by receiver on delivery IBAN bank account (optional-16);With warranty (optional-1);Confirmation of delivery (optional-1);Notification language of confirmation of delivery (optional-2);Notification type of confirmation of delivery (optional-50);Contact data of confirmation of delivery (optional-50)\n"
        ]
        let header = senderHeader || ""
        let asCSV = CSVHeader.concat(parcels.map(a => this.toBPostCSV(a,header) + "\n"));
        let csvInIso = asCSV.map(l => this.toIso(l)) // BPost only accepts ISO-8859-1 encoding
        return new Blob(csvInIso,{type:"text/csv;charset=ISO-8859-1"});;
    }

    private static toIso(input: string): Buffer{
        return iconv.encode(input,"ISO-8859-1");
    }

    private static toBPostCSV(address: Address, senderHeader:string): string {
        let asCSV =
            address.firstName + ";" +
            address.lastName +";"+
            ";" +   // Empty for company
            address.street + ";" +
            address.streetNb + ";" +
            address.postboxLetter + ";" +
            address.postcode + ";" +
            address.city +";" +
            address.email + ";" +
            ";" + // empty for customer phone number
            senderHeader +
            "2;;" + // weight and reference
            ";;;;;;;";
        return asCSV;
    }
}
