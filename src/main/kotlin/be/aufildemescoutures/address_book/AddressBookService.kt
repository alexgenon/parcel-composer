package be.aufildemescoutures.address_book

import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class AddressBookService {

    private var addressRepository = emptySet<Address>()
    fun resetAddressBook(newAddresses: Set<Address>) {
        this.addressRepository = newAddresses
    }

    fun getAllAddresses(): Set<Address> = this.addressRepository
    fun addAddresses(addresses: Set<Address>) {
        addressRepository = addressRepository.plus(addresses)
    }

    private fun newAddress(address: Address) {
        addressRepository = addressRepository.plus(address)
    }
}
