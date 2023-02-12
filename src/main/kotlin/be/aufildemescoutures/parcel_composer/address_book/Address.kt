package be.aufildemescoutures.parcel_composer.address_book

import be.aufildemescoutures.parcel_composer.infrastructure.serialization.InstantSerializer
import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntityBase
import org.hibernate.annotations.CreationTimestamp
import java.time.Instant
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne

@kotlinx.serialization.Serializable
@Entity
class Address(): PanacheEntityBase {
    @Id
    @GeneratedValue
    var id: Long? = null

    lateinit var businessId: String
    @ManyToOne
    var addressBook: AddressBook? = null
    lateinit var originalString: String
    lateinit var firstName: String
    lateinit var lastName: String
    lateinit var street: String
    lateinit var streetNb: String // Some street numbers can contain letters and there is no added lateinit varue to have a Int
    lateinit var postboxLetter: String
    lateinit var city: String
    var postcode: Int = -1 // Post code is officially defined as 4 digits so Int is safe here
    var email: String? = null
    @kotlinx.serialization.Serializable(with = InstantSerializer::class)
    @CreationTimestamp
    var creationTimestamp: Instant? = null

    constructor(
        businessId: String,
        originalString: String,
        firstName: String,
        lastName: String,
        street: String,
        streetNb: String,
        postboxLetter: String,
        city: String,
        postcode: Int,
        email: String?,
    ) :this(){
        this.businessId = businessId
        this.originalString = originalString
        this.firstName = firstName
        this.lastName = lastName
        this.street = street
        this.streetNb = streetNb
        this.postboxLetter = postboxLetter
        this.city = city
        this.postcode = postcode
        this.email = email
    }


    companion object: PanacheCompanion<Address>{
        fun findAllForAddressBook(addressBook: AddressBook):Set<Address> =
            find("addressBook",addressBook).list().toSet()
    }
}

