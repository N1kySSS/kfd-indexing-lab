package lab2

class Catalog(listOfProducts: List<Product>) {
    private var _products = listOfProducts.filter { it.available }.associateBy { it.id }

    var counter = 0
        private set

    fun changeListOfProducts(products: List<Product>) {
        _products = products.filter { it.available }.associateBy { it.id }
    }

    fun selectProducts(
        recommendedIds: List<Long>,
        excludedIds: List<Long>,
        limit: Int
    ): List<Product> {
        counter = 0

        if (limit == 0) return emptyList()

        val excludedSet = excludedIds.toSet()

        val result = mutableListOf<Product>()
        val usedIds = mutableSetOf<Long>()

        for (currentId in recommendedIds) {
            counter++

            if (currentId in usedIds) continue

            val currentProductOrNull = _products[currentId]

            if (currentProductOrNull != null && currentProductOrNull.id !in excludedSet) {
                result.add(currentProductOrNull)
                usedIds.add(currentId)
            }

            if (result.size == limit) break
        }

        return result
    }
}
