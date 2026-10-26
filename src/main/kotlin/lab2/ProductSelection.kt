package lab2

fun selectProducts(
    products: List<Product>,
    recommendedIds: List<Long>,
    excludedIds: List<Long>
): List<Product> {
    val excludedSet = excludedIds.toSet()

    val availableById = products
        .filter { it.available && it.id !in excludedSet }
        .associateBy { it.id }

    val result = mutableListOf<Product>()

    recommendedIds.toSet().forEach { currentId ->
        val currentProductOrNull = availableById[currentId]

        if (currentProductOrNull != null) {
            result.add(currentProductOrNull)
        }
    }

    return result
}
