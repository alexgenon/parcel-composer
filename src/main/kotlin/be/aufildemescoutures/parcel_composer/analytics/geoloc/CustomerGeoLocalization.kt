package be.aufildemescoutures.parcel_composer.analytics.geoloc

import be.aufildemescoutures.parcel_composer.address_book.Address

data class CustomerGeoLocalization (val address:Address, val latitude: Double, val longitude: Double)
