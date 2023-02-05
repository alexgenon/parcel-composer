package be.aufildemescoutures.parcel_composer.address_book

import be.aufildemescoutures.parcel_composer.user.UserId
import be.aufildemescoutures.parcel_composer.user.UserService
import org.jboss.logging.Logger
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.inject.Default
import javax.inject.Inject

@ApplicationScoped
class AddressBookService {

    @Inject
    @field:Default
    lateinit var userService: UserService

    private val LOG = Logger.getLogger(javaClass)
    private val addressRepository:MutableMap<UserId,AddressBook> = mutableMapOf()

    fun resetAddressBook(userId:UserId,newAddresses: AddressBook) {
        this.addressRepository[userId]= newAddresses
    }

    fun getAllAddresses(userId:UserId): AddressBook = this.addressRepository.getOrElse(userId) {  emptySet() }

    private fun applyToAddressBook (userId: UserId, update: (AddressBook) -> AddressBook ) {
        val newAddressBook = update(this.getAllAddresses(userId))
        addressRepository[userId] = newAddressBook
    }

    fun addAddresses(userId:UserId, addresses: Set<Address>) {
        LOG.info("Adding addresses $addresses to $userId address book")
        applyToAddressBook(userId) {it.plus(addresses)}
    }

    fun newAddress(userId:UserId, address: Address) {
        LOG.info("Adding address $address to $userId address book")
        applyToAddressBook(userId) {it.plus(address)}
    }

    fun removeAddress(userId:UserId, id:String){
        LOG.info("Removing address $id for $userId")
        applyToAddressBook(userId) {it.filter {ad -> ad.id != id }.toSet()}
    }
}
