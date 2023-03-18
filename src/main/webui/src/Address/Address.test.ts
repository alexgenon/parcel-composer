import {AddressBuilder} from "./Address";

test("Parse normal address",()=> {
    let address = AddressBuilder.fromString("Stalone Sylvester \n" +
        "Rue du Musculaire 3\n" +
        "5756 Boxeursnés \n" +
        "slysta@gmail.com\n");
    expect(address.firstName).toBe('Stalone');
    expect(address.lastName).toBe('Sylvester');
    expect(address.email).toBe('slysta@gmail.com');
    expect(address.street).toBe('Rue du Musculaire');
    expect(address.streetNb).toBe("3");
    expect(address.city).toBe('Boxeursnés');
    expect(address.postcode).toBe(5756);
});

test("Parse address with / postbox ",()=> {
    let address = AddressBuilder.fromString("Stalone Sylvester \n" +
        "Rue du Musculaire 3A\n" +
        "5756 Boxeursnés \n" +
        "slysta@gmail.com\n");
    console.log(address);
    expect(address.streetNb).toBe("3");
    expect(address.postboxLetter).toBe("A")
});

test ("Test Shuffled Address", () => {
    let testAddressReshuffled: string = `john234-doe_bruss12@gmail2.com
        Rue de la haute basse-rue,   13
        3459 Charleroi,
        John Doe
        `;

    let address = AddressBuilder.fromString(testAddressReshuffled);
    expect(address.firstName).toBe('John');
    expect(address.lastName).toBe('Doe');
    expect(address.email).toBe('john234-doe_bruss12@gmail2.com');
    expect(address.street).toBe('Rue de la haute basse-rue');
    expect(address.streetNb).toBe("13");
    expect(address.city).toBe('Charleroi');
    expect(address.postcode).toBe(3459);
})
