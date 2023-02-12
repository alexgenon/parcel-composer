package be.aufildemescoutures.address_book

import be.aufildemescoutures.parcel_composer.address_book.AddressBookService
import be.aufildemescoutures.parcel_composer.user.UserId
import io.quarkus.test.junit.QuarkusTest
import org.jboss.logging.Logger
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import javax.inject.Inject

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class AddressBookServiceTest {
    private val LOG = Logger.getLogger(javaClass)
    @Inject
    lateinit var addressBookService: AddressBookService

    private val user456 = UserId("456")
    private val user123 = UserId("123")

    @Test
    @Order(1)
    fun createAddressForMultipleUsers() {
        addressBookService.newAddress(user123, DummyAddresses.dummyAddress())
        addressBookService.newAddress(user456, DummyAddresses.dummyAddress())
        addressBookService.newAddress(user456, DummyAddresses.dummyAddress2())

        val user123Addresses = addressBookService.getAllAddresses(user123)
        val user456Addresses = addressBookService.getAllAddresses(user456)

        assertAll("Manages multi-users", {
            assertEquals(1,user123Addresses.size )
            assertEquals(2,user456Addresses.size )
            assertEquals(DummyAddresses.dummyAddress().businessId,user123Addresses.first().businessId)
        })
    }

    @Test
    @Order(2)
    fun correctlyRemovesAddress(){
        addressBookService.removeAddress(user456,DummyAddresses.dummyAddress().businessId)
        addressBookService.removeAddress(user456,"not_a_known_id")

        val user123Addresses = addressBookService.getAllAddresses(user123)
        val user456Addresses = addressBookService.getAllAddresses(user456)
        assertAll("Correctly removes addresses from $user456", {
            assertEquals(1, user456Addresses.size)
            assertEquals(DummyAddresses.dummyAddress2().businessId, user456Addresses.first().businessId)
        })
        assertAll("Does not touch $user123", {
            assertEquals(1, user123Addresses.size)
        })
    }
}
