package be.aufildemescoutures.parcel_composer.address_book

import be.aufildemescoutures.parcel_composer.user.UserId
import be.aufildemescoutures.parcel_composer.user.UserService
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import io.vertx.mutiny.core.eventbus.EventBus
import org.jboss.logging.Logger
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.inject.Default
import javax.inject.Inject

@ApplicationScoped
class AddressBookService {

    @Inject
    @field:Default
    lateinit var userService: UserService

    @Inject
    lateinit var eventBus: EventBus

    private val LOG = Logger.getLogger(javaClass)

    fun getAllAddresses(userId: UserId): Uni<List<Address>> = AddressBook
        .findOrCreateForUser(userId)
        .flatMap { it.allAddresses() }

    fun newAddress(userId: UserId, address: Address): Uni<Address> {
        LOG.info("Adding address ${address.businessId} to $userId address book")
        return AddressBook.findOrCreateForUser(userId)
            .flatMap { newAddress(it,address)}
    }

    fun newAddress(book: AddressBook, address: Address):Uni<Address> =
        book.newAddress(address)
            .call { updatedAddress-> newAddressEvent(updatedAddress) }

    fun removeAddress(userId: UserId, businessId: String): Uni<Long> {
        LOG.info("Removing address $businessId from $userId address book")
        return AddressBook.findOrCreateForUser(userId)
            .flatMap { it.removeAddress(businessId) }
            .call{ deleteCount -> Uni.createFrom().item {  LOG.info("$deleteCount element removed")} }
    }

    fun addAddresses(userId: UserId, addresses: Set<Address>, resetBook: Boolean): Uni<AddressBook> {
        LOG.info("Adding addresses to $userId address book, reset book $resetBook")

        val bookUni = AddressBook.findOrCreateForUser(userId)
        return bookUni.onItem()
            .call { book ->
                val preProcessedBook = if (resetBook) {
                    book.truncateBook()
                } else {
                    Uni.createFrom().item(0)
                }
                preProcessedBook.map { addAddressesInBook(book, addresses) }
            }
    }

    private fun addAddressesInBook(book: AddressBook, addresses: Set<Address>): Multi<Address> {
        return Multi
            .createFrom()
            .iterable(addresses)
            .flatMap { address -> newAddress(book,address).toMulti() }
    }

    private fun newAddressEvent(address: Address) =
        Uni.createFrom().item { eventBus.publish(Address.NEW_ADDRESS_EVENT, address) }

    fun getAddress(userId: UserId, businessId: String) =
        AddressBook
            .findOrCreateForUser(userId)
            .flatMap { it.findAddress(businessId) }
}
