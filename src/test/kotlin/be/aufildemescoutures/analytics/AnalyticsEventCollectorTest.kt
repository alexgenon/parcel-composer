package be.aufildemescoutures.analytics

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
class AnalyticsEventCollectorTest {
    private val LOG = Logger.getLogger(javaClass)
    @Inject
    @field:Default
    lateinit var addressBookService: AddressBookService

    @Inject
    @field:Default
    lateinit var analyticsService: AnalyticsService

    @InjectMock
    @RestClient
    lateinit var openStreetMapResolver: OpenStreetMapResolver

    private val user123 = UserId("123")

    @BeforeEach
    fun setup(){
       OpenStreetMapMock.setupOpenStreetMapMock(openStreetMapResolver)
    }

    @Test
    fun newAddressSentToAnalytics(){
        val address = addressBookService
            .newAddress(user123, DummyAddresses.dummyBastogneAddress())
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .assertCompleted()
            .item

        Thread.sleep(500)
        val specificAddressOnMap = analyticsService
            .findForAddress(address.businessId)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .assertCompleted()
            .item!!

        assertAll("Check specific address collection",
            { Assertions.assertEquals(specificAddressOnMap.addressId, address.businessId) },
            { Assertions.assertEquals(specificAddressOnMap.lat, 50.0057556) }
        )

        val addressesOnMap = analyticsService
            .listAddressesOnMap()
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .item

        LOG.debug("Check AddressOnMap:")
        addressesOnMap.forEach { LOG.debug(it) }
        Assertions.assertTrue(addressesOnMap.any { it.addressId == address.businessId })
    }
}
