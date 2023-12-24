package be.aufildemescoutures.infrastructure

import be.aufildemescoutures.address_book.DummyAddresses
import be.aufildemescoutures.parcel_composer.address_book.Address
import be.aufildemescoutures.parcel_composer.infrastructure.analytics.geoloc.OpenStreetMapResolver
import io.smallrye.mutiny.Uni
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromStream
import org.mockito.Mockito.*
import java.io.InputStream

@OptIn(ExperimentalSerializationApi::class)
object OpenStreetMapMock {
    fun Address.getOpenStreetMapJson(): JsonElement {
        val correctResponse: InputStream? = Thread.currentThread().getContextClassLoader().getResourceAsStream("OpenStreetMapResponse${this.city}.json")
        return Json.decodeFromStream(correctResponse!!)
    }

    fun setupOpenStreetMapMock (openStreetMapResolver:OpenStreetMapResolver){
        `when`(openStreetMapResolver.search(anyString(), anyInt(), anyString()))
            .thenReturn(Uni.createFrom().item(Json.decodeFromString<JsonElement>("[]") ))
        `when`(openStreetMapResolver.search("jsonv2", 1, "rue des remparts - 6600 Bastogne"))
            .thenReturn(Uni.createFrom().item(DummyAddresses.dummyBastogneAddress().getOpenStreetMapJson()))
        `when`(openStreetMapResolver.search("jsonv2", 1, "rue grande - 6687 Bertogne"))
            .thenReturn(Uni.createFrom().item(DummyAddresses.dummyBertogneAddress().getOpenStreetMapJson()))
        `when`(openStreetMapResolver.search("jsonv2", 1, "place communale - 6637 Fauvillers"))
            .thenReturn(Uni.createFrom().item(DummyAddresses.dummyFauvillersAddress().getOpenStreetMapJson()))
    }
}
