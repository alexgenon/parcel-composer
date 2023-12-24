package be.aufildemescoutures.parcel_composer.address_book

import be.aufildemescoutures.parcel_composer.user.UserId
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheEntity
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import org.jboss.logging.Logger
import javax.persistence.Entity

@Entity
@kotlinx.serialization.Serializable
class AddressBook : PanacheEntity() {
    lateinit var userId: String

    fun allAddresses(): Uni<List<Address>> = Address.findAllForAddressBook(this)
    fun findAddress(businessId: String) = Address.findForAddressBook(this,businessId)
    fun newAddress(address: Address): Uni<Address> {
        address.addressBook = this
        return address.persist()
    }

    fun removeAddress(businessId: String) = Address.delete("businessId=?1 and addressBook=?2", businessId,this)

    fun truncateBook() = Address.delete("addressBook", this)

    companion object : PanacheCompanion<AddressBook> {
        private val LOG = Logger.getLogger(javaClass)

        fun findOrCreateForUser(userId: UserId): Uni<AddressBook> =
            find("userId", userId.toString())
                .firstResult()
                .onItem()
                .ifNull()
                .switchTo { newAddressBook(userId) }
                .map { it!! }

        private fun newAddressBook(userId: UserId): Uni<AddressBook> {
            LOG.info("Creating new Address book for $userId")
            val newBook = AddressBook()
            newBook.userId = userId.toString()
            return newBook.persist<AddressBook>()
        }
    }
}
