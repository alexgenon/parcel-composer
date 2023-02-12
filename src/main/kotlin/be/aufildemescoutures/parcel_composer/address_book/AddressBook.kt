package be.aufildemescoutures.parcel_composer.address_book

import be.aufildemescoutures.parcel_composer.user.UserId
import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import org.jboss.logging.Logger
import javax.persistence.Entity
import javax.persistence.OneToOne

@Entity
@kotlinx.serialization.Serializable
class AddressBook : PanacheEntity() {
    lateinit var userId: String

    fun allAddresses(): Set<Address> = Address.findAllForAddressBook(this)
    fun newAddress(address: Address): Address {
        address.addressBook = this
        address.persist()
        return address
    }

    fun removeAddress(businessId: String) = Address.delete("businessId=?1 and addressBook=?2", businessId,this)

    fun truncateBook() = Address.delete("addressBook", this)

    companion object : PanacheCompanion<AddressBook> {
        private val LOG = Logger.getLogger(javaClass)

        fun findByUser(userId: UserId): AddressBook =
            find("userId", userId.toString()).firstResult() ?: newAddressBook(userId)

        private fun newAddressBook(userId: UserId): AddressBook {
            LOG.info("Creating new Address book for $userId")
            val newBook = AddressBook()
            newBook.userId = userId.toString()
            newBook.persist()
            return newBook
        }
    }
}
