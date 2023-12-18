package fr.ocat.sandbox.kotlinsandbox

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo
import strikt.assertions.message
import strikt.assertions.size

internal class PropertySetKtTest {

    @Test fun `Empty map has no keys`() {
        val propertySet: PropertySet = emptyMap()

        expectThrows<NoSuchElementException> {
            propertySet.valueOf("any")
        }.message.isEqualTo("Key any is missing in the map.")
    }

    @Test fun `null key throws`() {
        val propertySet: PropertySet = mapOf("key" to null)

        expectThrows<IllegalStateException> {
            propertySet.valueOf("key")
        }.message.isEqualTo("Key <key> is null")
    }

    @Test fun `String value`() {
        val propertySet: PropertySet = mapOf("key" to "value")

        expectThat(propertySet.valueOf<String>("key")).isEqualTo("value")
    }

    @Test fun `Int value`() {
        val propertySet: PropertySet = mapOf("key" to 3)

        expectThat(propertySet.valueOf<Int>("key")).isEqualTo(3)
    }

    @Test fun `List value`() {
        val propertySet: PropertySet = mapOf("key" to listOf(3, 5))

        expectThat(propertySet.valueOf<List<Int>>("key")).isEqualTo(listOf(3, 5))
    }

    @Test fun `Wrong list type doesnt throw`() {
        val propertySet: PropertySet = mapOf("key" to listOf(3, 5))

        expectThat(propertySet.valueOf<List<String>>("key")).size.isEqualTo(2)
    }

    @Test fun `wrong type throws`() {
        val propertySet: PropertySet = mapOf("key" to "value")

        expectThrows<IllegalStateException> {
            propertySet.valueOf<Int>("key")
        }.message.isEqualTo("Value for key <key> is not a class kotlin.Int")
    }

    @Test fun `Path of 2 keys`() {
        val propertySet: PropertySet = mapOf("key1" to mapOf("key2" to "value"))

        expectThat(propertySet.valueOf<String>("key1", "key2")).isEqualTo("value")
    }

    @Test fun `Path of 2 keys throws on unknown root path`() {
        val propertySet: PropertySet = mapOf("key1" to mapOf("key2" to "value"))

        expectThrows<NoSuchElementException> {
            propertySet.valueOf<String>("unknown", "key2")
        }.message.isEqualTo("Key unknown is missing in the map.")
    }

    @Test fun `Path of 2 keys throws on unknown child path`() {
        val propertySet: PropertySet = mapOf("key1" to mapOf("key2" to "value"))

        expectThrows<NoSuchElementException> {
            propertySet.valueOf<String>("key1", "unknown")
        }.message.isEqualTo("Key unknown is missing in the map.")
    }

    @Test fun `Path of 3 keys`() {
        val propertySet: PropertySet = mapOf("key1" to mapOf("key2" to mapOf("key3" to "value")))

        expectThat(propertySet.valueOf<String>("key1", "key2", "key3")).isEqualTo("value")
    }

    @Test fun `Path of no keys`() {
        val propertySet: PropertySet = mapOf("key1" to mapOf("key2" to "value"))

        expectThrows<IllegalStateException> {
            propertySet.valueOf<String>(emptyList())
        }.message.isEqualTo("no keys supplied")
    }
}