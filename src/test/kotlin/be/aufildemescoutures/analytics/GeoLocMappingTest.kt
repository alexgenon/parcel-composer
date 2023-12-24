package be.aufildemescoutures.analytics

import be.aufildemescoutures.address_book.AddressServiceHelper
import be.aufildemescoutures.address_book.DummyAddresses
import be.aufildemescoutures.infrastructure.OpenStreetMapMock
import be.aufildemescoutures.parcel_composer.address_book.AddressBookService
import be.aufildemescoutures.parcel_composer.analytics.AnalyticsService
import be.aufildemescoutures.parcel_composer.infrastructure.analytics.geoloc.OpenStreetMapResolver
import be.aufildemescoutures.parcel_composer.user.UserId
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber
import org.eclipse.microprofile.rest.client.inject.RestClient
import org.jboss.logging.Logger
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import javax.enterprise.inject.Default
import javax.inject.Inject

@QuarkusTest

class GeoLocMappingTest {
    private val LOG = Logger.getLogger(javaClass)
    @Inject
    @field:Default
    lateinit var analyticsService: AnalyticsService

    @Inject
    @field:Default
    lateinit var addressBookService: AddressBookService

    @InjectMock
    @RestClient
    lateinit var openStreetMapResolver: OpenStreetMapResolver


    @BeforeEach
    fun setup(){
        OpenStreetMapMock.setupOpenStreetMapMock(openStreetMapResolver)
    }

    @Test
    fun testGetForUsers(){
        val userId=UserId("geloc123")
        val otherUserId=UserId("geloc789")

        val addresses = listOf(
            AddressServiceHelper.newAddress(addressBookService, userId,DummyAddresses.dummyBastogneAddress()),
            AddressServiceHelper.newAddress(addressBookService, userId,DummyAddresses.dummyBertogneAddress()),
            AddressServiceHelper.newAddress(addressBookService, userId,DummyAddresses.dummyAddress())
        )
        val otherAddresses = listOf(AddressServiceHelper.newAddress(addressBookService, otherUserId,DummyAddresses.dummyFauvillersAddress()))

        val geolocedAddressesUser = analyticsService
            .findForUser(userId)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .assertCompleted()
            .item

        LOG.debug("result of findForUser $userId: ${geolocedAddressesUser.joinToString("\n")}")

        assertAll("Check that merging addresses with addresses on map for $userId is correct", {
            Assertions.assertEquals(3, geolocedAddressesUser.size)
            Assertions.assertEquals(2, geolocedAddressesUser.filter { it.addressOnMap != null }.size)
            Assertions.assertEquals(geolocedAddressesUser.find { it.address.businessId == DummyAddresses.dummyBastogneAddress().businessId}?.addressOnMap?.lat, 50.0057556 )
            Assertions.assertEquals(geolocedAddressesUser.find { it.address.businessId == DummyAddresses.dummyBertogneAddress().businessId}?.addressOnMap?.lat, 50.0818725 )
            Assertions.assertNull(geolocedAddressesUser.find { it.address.businessId == DummyAddresses.dummyAddress().businessId}?.addressOnMap)
        })

        val geolocedAddressesOtherUser = analyticsService
            .findForUser(otherUserId)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .assertCompleted()
            .item

        LOG.debug("result of findForUser $otherUserId: ${geolocedAddressesOtherUser.joinToString("\n")}")

        assertAll("Check that merging addresses with addresses on map for $otherUserId is correct", {
            Assertions.assertEquals(1, geolocedAddressesOtherUser.size)
            Assertions.assertNotNull(geolocedAddressesOtherUser.first().addressOnMap)
            Assertions.assertEquals(5.6668351,geolocedAddressesOtherUser.first().addressOnMap?.lon )
        })
    }

}
