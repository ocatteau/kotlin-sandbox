package fr.ocat.sandbox.kotlinsandbox

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Test
import java.io.File

class JsonMapperTest {

    private val objectMapper = jacksonObjectMapper()

    @Test
    fun `process data`() {
        val data: PropertySet = objectMapper.readValue(dataFile)

//        expectThat(data) {
//            get { get("places") }.isA<List<Any>>().and { hasSize(18) }
//        }

        val places: List<PropertySet> = data.valueOf("places")
        places.map(::Place).forEach { place: Place ->
            println("${place.displayName} ${place.countryCode}")
        }
    }

    companion object {
        val dataFile = File(
            "src/test/resources/${this::class.java.packageName.replace('.', '/')}",
            "places-response.json"
        )
    }
}

data class Place(val properties: PropertySet) : PropertySet by properties {
    val displayName = valueOf<String>("displayName", "text")

    val countryCode: String? = addressComponents.find { it.types.contains("country") }?.shortText

    private val addressComponents get() = valueOf<List<PropertySet>>("addressComponents")
        .map(::AddressComponent)
}

data class AddressComponent(val properties: PropertySet): PropertySet by properties {
    val types = valueOf<List<String>>("types")
    val shortText = this?.valueOf<String>("shortText")
}
