package fr.ocat.sandbox.kotlinsandbox


typealias PropertySet = Map<String, Any?>

inline fun <reified T: Any> PropertySet.valueOf(key: String): T {
    val value = getValue(key)
        ?: error("Key <$key> is null")

    return value as? T
        ?: error("Value for key <$key> is not a ${T::class}")
}

//inline fun <reified T : Any> PropertySet.valueOf(vararg keys: String): T {
//    return valueOf(keys.toList())
//}

inline fun <reified T : Any> PropertySet.valueOf(key0: String, vararg keys: String): T {
    return valueOf(listOf(key0) + keys.toList())
}

inline fun <reified T : Any> PropertySet.valueOf(keys: List<String>): T {
    return when {
        keys.isEmpty() -> error("no keys supplied")
        else -> keys.dropLast(1).fold(this) { acc, key ->
            acc.valueOf<PropertySet>(key)
        }.valueOf(keys.last())
    }
}
