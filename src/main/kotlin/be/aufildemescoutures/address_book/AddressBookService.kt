package be.aufildemescoutures.address_book

import org.jboss.logging.Logger
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class AddressBookService {
    private val LOG = Logger.getLogger(javaClass)
    private var addressRepository = emptySet<Address>()
    fun resetAddressBook(newAddresses: Set<Address>) {
        this.addressRepository = newAddresses
    }

    fun getAllAddresses(): Set<Address> = this.addressRepository
    fun addAddresses(addresses: Set<Address>) {
        addressRepository = addressRepository.plus(addresses)
    }

    fun newAddress(address: Address) {
        LOG.info("Adding address $address")
        addressRepository = addressRepository.plus(address)
    }

    fun removeAddress(id:String){
        LOG.info("Removing address $id")
        addressRepository = addressRepository.filter { it.id != id }.toSet()
    }
}
