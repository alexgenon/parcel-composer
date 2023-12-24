package be.aufildemescoutures.infrastructure.analytics.geoloc

import be.aufildemescoutures.address_book.DummyAddresses
import be.aufildemescoutures.infrastructure.OpenStreetMapMock
import be.aufildemescoutures.parcel_composer.analytics.geoloc.AddressOnMap
import be.aufildemescoutures.parcel_composer.infrastructure.analytics.geoloc.AddressResolutionService
import be.aufildemescoutures.parcel_composer.infrastructure.analytics.geoloc.OpenStreetMapResolver
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber
import kotlinx.serialization.json.*
import org.eclipse.microprofile.rest.client.inject.RestClient
import org.junit.jupiter.api.*
import javax.inject.Inject


@QuarkusTest
class AddressResolutionIT {
    @InjectMock
    @RestClient
    lateinit var openStreetMapResolver: OpenStreetMapResolver

    @Inject
    lateinit var addressResolutionService: AddressResolutionService

    @BeforeEach
    fun setup(){
        OpenStreetMapMock.setupOpenStreetMapMock(openStreetMapResolver)
    }

    @Test
    @DisplayName("Gets a correct address")
    fun checkCorrectAddressResolution(){
        val result = addressResolutionService.getAddress(DummyAddresses.dummyBastogneAddress())
        val subscriber:UniAssertSubscriber<AddressOnMap> = result
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())

        val resultAddressOnMap = subscriber.awaitItem().item
        assertAll("Correctly get lat and lon",
            { Assertions.assertEquals( resultAddressOnMap?.lat, 50.0057556) },
            { Assertions.assertEquals( resultAddressOnMap?.lon,5.7204901 ) }
        )
    }
}
