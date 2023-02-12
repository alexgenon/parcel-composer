package be.aufildemescoutures.parcel_composer.infrastructure.serialization
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import org.jboss.logging.Logger
import java.time.Instant

@Serializer(forClass = Instant::class)
@OptIn(ExperimentalSerializationApi::class)
object InstantSerializer : KSerializer<Instant> {
    private val LOG = Logger.getLogger(javaClass)
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Instant", PrimitiveKind.LONG)
    override fun serialize(encoder: Encoder, value: Instant) {
        val epoch = value.toEpochMilli()
        LOG.trace("About to encode $epoch")
        encoder.encodeLong(epoch)
    }

    override fun deserialize(decoder: Decoder): Instant {
        val longDecoded = decoder.decodeLong()
        LOG.trace("About the decode $longDecoded")
        return Instant.ofEpochMilli(longDecoded)
    }
}
