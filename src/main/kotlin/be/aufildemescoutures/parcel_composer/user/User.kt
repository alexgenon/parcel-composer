package be.aufildemescoutures.parcel_composer.user

import io.quarkus.hibernate.reactive.panache.kotlin.PanacheEntity
import javax.persistence.Entity


@kotlinx.serialization.Serializable
data class UserId(val id:String)

@kotlinx.serialization.Serializable
// @Entity - not yet ready to persist this
class User(var userId: UserId): PanacheEntity(){
    var senderHeader:String="Fake Name;Some Company;BE123412314;Some street;32;;9328;Some City;someemail@gmail.com;3249876543;"
}
