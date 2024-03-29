export interface Address {
    originalString: string;
    firstName: string;
    lastName: string;
    street: string;
    streetNb: string;
    postboxLetter: string;
    city: string;
    postcode: number;
    email: string;
    businessId: string;
}

export class AddressBuilder {
    originalString: string ;
    firstName: string = '';
    lastName: string = '';
    street: string = '';
    streetNb: string='';
    postboxLetter: string = '';
    city: string = '';
    postcode: number=0;
    email: string = '';
    businessId: string = '';

    //constructor();
    constructor (initData?: Address){
        this.originalString = initData?.originalString??"";
        this.firstName= initData?.firstName??'';
        this.lastName= initData?.lastName??'';
        this.street= initData?.street??'';
        this.streetNb = initData?.streetNb??'';
        this.postboxLetter= initData?.postboxLetter??'';
        this.city= initData?.city??'';
        this.postcode = initData?.postcode??0;
        this.email= initData?.email??'';
        this.businessId= initData?.businessId??'';
    }

    public build(): Address {
        this.businessId = this.firstName + this.lastName + this.city;
        return {...this} as Address;
    }

    static NB_AND_STRING_REGEX = /\s*([0-9]+[a-z,A-Z]*)?,?\s*(.+?),?\s*([0-9]+[a-z,A-Z]*)?\s*$/;
    static HAS_NUMBER_REGEX = /\d/
    static NB_AND_LETTER = /([0-9]+)([a-z,A-Z]*)/;
    static EMAIL_REGEX = /([\w-.]+@[\w-]+\.+[\w-]+)/

    private static getTrimmedRowIfPresent(array: any[], index: number) {
        let row = (array.length >= index + 1 ? array[index] : "") as string;
        return row && row.trim();
    }

    static getStringAndNumber(composedString: string) {
        let parsedAddress = composedString.match(this.NB_AND_STRING_REGEX);
        if (parsedAddress) {
            let stringPart = this.getTrimmedRowIfPresent(parsedAddress, 2);
            let firstPartOfRegex = this.getTrimmedRowIfPresent(parsedAddress, 1);
            let numberPart;
            if (firstPartOfRegex && firstPartOfRegex.length >= 1) {
                numberPart = parsedAddress[1];
            } else {
                numberPart = parsedAddress[3];
            }
            return [stringPart, numberPart];
        } else {
            return [];
        }
    }

    static getNumberAndLetters(composedString: string) {
        let parsedNb = composedString.match(this.NB_AND_LETTER);
        if (parsedNb) {
            let numberPart = parsedNb[1];
            let letterPart = this.getTrimmedRowIfPresent(parsedNb, 2);
            return [numberPart, letterPart];
        } else {
            return [];
        }
    }

    static fromString(originalString: string): AddressBuilder {
        let lines: string[] = originalString.split(/\r\n|\r|\n/);
        let address = new AddressBuilder();
        address.originalString = originalString;
        for (let line of lines) {
            line = line.trim();
            let emailMatchRegEx = line.match(this.EMAIL_REGEX);
            if (emailMatchRegEx && !address.email) {
                /* emails are easy to detect */
                address.email = emailMatchRegEx[0];
            } else if (line.match(this.HAS_NUMBER_REGEX) && !address.street) {
                /* We assume the first line with numbers is the street */
                let streetAndNb = this.getStringAndNumber(line);
                address.street = this.getTrimmedRowIfPresent(streetAndNb, 0);
                let nbAndPostbox = this.getNumberAndLetters(this.getTrimmedRowIfPresent(streetAndNb, 1))
                address.streetNb = this.getTrimmedRowIfPresent(nbAndPostbox, 0);
                address.postboxLetter = this.getTrimmedRowIfPresent(nbAndPostbox, 1);
            } else if (line.match(this.HAS_NUMBER_REGEX) && address.street && !address.city) {
                /* Then if street is defined, the next line with numbers is the city */
                let cityAndPostcode = this.getStringAndNumber(line);
                address.city = this.getTrimmedRowIfPresent(cityAndPostcode, 0);
                address.postcode = +this.getTrimmedRowIfPresent(cityAndPostcode, 1);
            } else if (!address.firstName) {
                /* Finally, line not matching anything else must be the name */
                address.setNameFields(line);
            }
        }
        return address;
    }

    postAddressToString(): string {
        return `${this.street}, ${this.streetNb}${this.postboxLetter} - ${this.postcode} ${this.city}`
    }

    setNameFields(name: string) {
        name = name.trim();
        let lastSpace = name.lastIndexOf(' ') + 1;
        this.lastName = name.substring(lastSpace);
        this.firstName = name.substring(0, lastSpace - 1);
    }
}
