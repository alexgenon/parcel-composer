package be.aufildemescoutures.parcel_composer.address_book

@kotlinx.serialization.Serializable
data class Address(
    val id: String,
    val originalString: String,
    val firstName: String,
    val lastName: String,
    val street: String,
    val streetNb: String, // Some street numbers can contain letters and there is no added value to have a Int
    val postboxLetter: String,
    val city: String,
    val postcode: Int, // Post code is officially defined as 4 digits so Int is safe here
    val email: String?,
)
