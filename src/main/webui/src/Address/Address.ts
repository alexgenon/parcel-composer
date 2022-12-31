export interface Address {
    originalString: string;
    firstName: string;
    lastName: string;
    street: string;
    streetNb: number;
    postboxLetter: string;
    city: string;
    postcode: number;
    email: string;
    id: string;
}

export class AddressBuilder {
    originalString: string = '';
    firstName: string = '';
    lastName: string = '';
    street: string = '';
    streetNb?: number = undefined;
    postboxLetter: string = '';
    city: string = '';
    postcode?: number = undefined;
    email: string = '';
    id: string = '';

    public build(): Address {
        this.id = this.firstName + this.lastName + this.city;
        return {...this} as Address;
    }

    static NB_AND_STRING_REGEX = /\s*([0-9]+[a-z,A-Z]*)?,?\s*(.+?),?\s*([0-9]+[a-z,A-Z]*)?\s*$/;
    static HAS_NUMBER_REGEX = /\d/
    static NB_AND_LETTER = /([0-9]+)([a-z,A-Z]*)/;
    static EMAIL_REGEX = /([\w-\.]+@[\w-]+\.+[\w-]+)/

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
        let nl = lines.length;
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
                address.streetNb = +(this.getTrimmedRowIfPresent(nbAndPostbox, 0));
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

    constructor() {
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

export const someAddresses: Address[] = [{
    "email": "schmitz.adeline@hotmail.com",
    "name": "Adeline Schmitz",
    "street": "Rue des calvaires",
    "streetNb": 51,
    "postcode": 6780,
    "postboxLetter": "",
    "city": "Wolkrange",
    "originalString": "schmitz.adeline@hotmail.com\nAdeline Schmitz\nRue des calvaires,51 \n6780 Wolkrange",
    "lastName": "Schmitz",
    "firstName": "Adeline",
    "id": "abc"
}, {
    "email": "adelaidelampson1@gmail.com",
    "originalString": "Adélaide Lampson\n84, rue du Castel \n6700 Arlon\nadelaidelampson1@gmail.com",
    "name": "Adélaide Lampson",
    "street": "rue du Castel",
    "streetNb": 84,
    "postboxLetter": ",",
    "city": "Arlon",
    "postcode": "6700",
    "lastName": "Lampson",
    "firstName": "Adélaide",
    "id": "bcd"
}, {
    "email": "alessandracimminiello@yahoo.be",
    "name": "Alessandra cimminiello",
    "lastName": "cimminiello",
    "firstName": "Alessandra",
    "street": "rue Nicolas Defrecheux",
    "streetNb": 144,
    "postcode": 4040,
    "postboxLetter": "",
    "city": "Herstal",
    "originalString": "alessandracimminiello@yahoo.be \nAlessandra cimminiello\nrue Nicolas Defrecheux 144 \n4040 Herstal",
    "id": "cde"
}, {
    "email": "alexandra.delmotte@hotmail.com",
    "name": "Alexandre Delmotte",
    "street": "Rue de la Vallée",
    "streetNb": 23,
    "postcode": 6200,
    "postboxLetter": "A",
    "city": "Chatelineau",
    "originalString": "Alexandre Delmotte\nalexandra.delmotte@hotmail.com\nRue de la Vallée 23A\n 6200 Chatelineau ",
    "lastName": "Delmotte",
    "firstName": "Alexandre",
    "id":"def"
}, {
    "email": "",
    "name": "Alicia Bernard",
    "street": "voie des montagnard",
    "streetNb": 19,
    "postcode": 6831,
    "postboxLetter": "",
    "city": "noirefontaine",
    "originalString": "Alicia Bernard\n voie des montagnard 19 \n6831 noirefontaine",
    "lastName": "Bernard",
    "firstName": "Alicia",
    "id":"efg",
}, {
    "email": "aline.bielande@gmail.com",
    "name": "Aline Bielande",
    "street": "rue Fond de Chavée",
    "streetNb": 1,
    "postcode": 4218,
    "postboxLetter": "A",
    "city": "Couthuin.",
    "originalString": "Aline Bielande\naline.bielande@gmail.com\nrue Fond de Chavée 1A \n4218 Couthuin.",
    "lastName": "Bielande",
    "firstName": "Aline",
    "id":"fgh"
}, {
    "email": "borges.nina@hotmail.com",
    "originalString": "Aline Borges \nRue du Stade, 18\n6790 Aubange\nborges.nina@hotmail.com",
    "name": "Aline Borges",
    "street": "Rue du Stade",
    "streetNb": "18",
    "postboxLetter": "",
    "city": "Aubange",
    "postcode": "6790",
    "lastName": "Borges",
    "firstName": "Aline",
    "id":"ghi"
}, {
    "email": "alineorban@hotmail.com",
    "name": "Aline Orban",
    "street": "Rue Tahée",
    "streetNb": 45,
    "postcode": 6951,
    "postboxLetter": "",
    "city": "Bande",
    "originalString": "Aline Orban\nRue Tahée, 45\n6951 Bande\nalineorban@hotmail.com",
    "lastName": "Orban",
    "firstName": "Aline",
    "id":"hij"
}].map(a => {
    return a as Address
});

