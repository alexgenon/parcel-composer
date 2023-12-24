package be.aufildemescoutures.parcel_composer.analytics.geoloc

import io.quarkus.hibernate.reactive.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheEntity
import javax.persistence.Entity

@kotlinx.serialization.Serializable
@Entity
class AddressOnMap() : PanacheEntity() {
    var addressId: String = ""
    var lat: Double = 0.0
    var lon: Double = 0.0

    constructor(addressId: String, lat: Double, lon:Double) : this() {
        this.addressId = addressId
        this.lat = lat
        this.lon = lon
    }

    companion object : PanacheCompanion<AddressOnMap> {
    }
}
