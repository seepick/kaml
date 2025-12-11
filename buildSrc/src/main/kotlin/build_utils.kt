
fun enhanceSystemProperties(vararg more: Pair<String, String>): Map<String, Any> =
    enhanceSystemProperties(more.toList())

fun enhanceSystemProperties(more: List<Pair<String, String>>): Map<String, Any> =
    System.getProperties().asIterable().associate { it.key.toString() to it.value }.plus(more.toMap())
