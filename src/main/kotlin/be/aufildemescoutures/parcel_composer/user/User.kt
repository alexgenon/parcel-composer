package be.aufildemescoutures.parcel_composer.user

import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import javax.persistence.Entity

@kotlinx.serialization.Serializable
data class UserId(val id:String)

@kotlinx.serialization.Serializable
class User():PanacheEntity(){
    lateinit var userId: UserId

    constructor(userId: UserId) : this() {
        this.userId = userId
    }
}
