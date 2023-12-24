package be.aufildemescoutures.parcel_composer.analytics

import be.aufildemescoutures.parcel_composer.address_book.Address
import be.aufildemescoutures.parcel_composer.address_book.AddressBookService
import be.aufildemescoutures.parcel_composer.analytics.geoloc.AddressOnMap
import be.aufildemescoutures.parcel_composer.infrastructure.analytics.geoloc.AddressResolutionService
import be.aufildemescoutures.parcel_composer.user.UserId
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional
import io.quarkus.vertx.ConsumeEvent
import io.smallrye.mutiny.Uni
import org.jboss.logging.Logger
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.inject.Default
import javax.inject.Inject

@kotlinx.serialization.Serializable
data class GeolocedAddress(val address: Address, val addressOnMap: AddressOnMap?)

@ApplicationScoped
class AnalyticsService {
    private val LOG = Logger.getLogger(javaClass)

    @Inject
    @field:Default
    lateinit var addressResolutionService: AddressResolutionService

    @Inject
    @field:Default
    lateinit var addressBookService: AddressBookService

    /**
     * Proactively capture new Address creation and query OpenStreetMaps
     */
    @ConsumeEvent(Address.NEW_ADDRESS_EVENT)
    fun newAddress(ad: Address): Uni<Void> {
        LOG.debug("New address created: ${ad.businessId}")
        return createAddressOnMap(ad).replaceWithVoid()
    }

    @ReactiveTransactional
    fun createAddressOnMap(address: Address): Uni<AddressOnMap?> {
        return addressResolutionService
            .getAddress(address)
            .flatMap { aom -> aom.persist() }
    }


    fun listAddressesOnMap(): Uni<List<AddressOnMap>> {
        return AddressOnMap.listAll()
    }

    fun findForUser(userId: UserId): Uni<List<GeolocedAddress>> {
        val addressesResult =addressBookService.getAllAddresses(userId)
        val allAdressesOnMapsResult = listAddressesOnMap().onItem()
            .transform { list -> list.groupBy { it.addressId}}
        return Uni.combine().all().unis(addressesResult,allAdressesOnMapsResult).combinedWith { addresses, aom ->
            addresses.map { address ->
                GeolocedAddress(address,aom[address.businessId]?.firstOrNull())
            }
        }
    }

    fun findForAddress(addressId: String): Uni<AddressOnMap?> {
        return AddressOnMap
            .find("addressId", addressId)
            .firstResult()
    }
}
