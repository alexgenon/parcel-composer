package be.aufildemescoutures.parcel_composer.address_book

import be.aufildemescoutures.parcel_composer.user.UserId
import be.aufildemescoutures.parcel_composer.user.UserService
import org.jboss.logging.Logger
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.inject.Default
import javax.inject.Inject
import javax.transaction.Transactional

@ApplicationScoped
@Transactional
class AddressBookService {

    @Inject
    @field:Default
    lateinit var userService: UserService

    private val LOG = Logger.getLogger(javaClass)

    fun resetAddressBook(userId:UserId,newAddresses: Set<Address>) {
        LOG.info("Resetting $userId address book")
        val book = AddressBook.findByUser(userId)
        book.truncateBook()
        this.addAddresses(book,newAddresses)
    }

    fun getAllAddresses(userId:UserId) = AddressBook.findByUser(userId).allAddresses()

    private fun addAddresses(addressBook: AddressBook, addresses: Set<Address>) =
        addresses.forEach { addressBook.newAddress(it) }

    fun addAddresses(userId:UserId, addresses: Set<Address>) {
        LOG.info("Adding addresses $addresses to $userId address book")
        val addressBook = AddressBook.findByUser(userId)
        this.addAddresses(addressBook,addresses)
    }

    fun newAddress(userId:UserId, address: Address) {
        LOG.info("Adding address $address to $userId address book")
        AddressBook.findByUser(userId).newAddress(address)
    }

    fun removeAddress(userId:UserId, businessId:String){
        val deleteCount = AddressBook.findByUser(userId).removeAddress(businessId)
        LOG.info("Removing address $businessId from $userId address book: $deleteCount entries removed")
    }
}
