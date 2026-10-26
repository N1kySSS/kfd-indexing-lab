package lab2

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test

class ProductSelectionAdditionalTest {
    private val keyboard = Product(1, "Keyboard", true)
    private val mouse = Product(2, "Mouse", false)
    private val monitor = Product(3, "Monitor", true)
    private val webcam = Product(4, "Webcam", true)
    private val products = listOf(keyboard, webcam, mouse, monitor)
    private val recommended = listOf(3L, 2L, 3L, 99L, 1L, 4L)

    @Test
    fun selectsAvailableProductsInRecommendationOrder() {
        val catalog = Catalog(products)

        val result = catalog.selectProducts(recommended, listOf(1L), 10)

        assertEquals(listOf(monitor, webcam), result)
    }

    @Test
    fun removesDuplicateRecommendations() {
        val catalog = Catalog(products)

        val result = catalog.selectProducts(listOf(4L, 3L, 4L, 4L, 3L), emptyList(), 10)

        assertEquals(listOf(webcam, monitor), result)
    }

    @Test
    fun skipsUnavailableExcludedAndUnknownProducts() {
        val catalog = Catalog(products)

        val result = catalog.selectProducts(listOf(2L, 1L, 99L, 3L), listOf(1L, 1L, 100L), 10)

        assertEquals(listOf(monitor), result)
    }

    @Test
    fun returnsEmptyListWhenCatalogIsEmpty() {
        val catalog = Catalog(emptyList())

        val result = catalog.selectProducts(recommended, emptyList(), 10)

        assertEquals(emptyList<Product>(), result)
    }

    @Test
    fun returnsEmptyListWhenNoProductFits() {
        val catalog = Catalog(products)

        val result = catalog.selectProducts(listOf(2L, 1L, 99L), listOf(1L), 10)

        assertEquals(emptyList<Product>(), result)
    }

    @Test
    fun returnsOriginalProductObjects() {
        val catalog = Catalog(products)

        val result = catalog.selectProducts(listOf(3L), emptyList(), 10)

        assertSame(monitor, result[0])
    }

    @Test
    fun doesNotChangeInputData() {
        val productsCopy = products.toList()
        val recommendedMutable = recommended.toMutableList()
        val excludedMutable = mutableListOf(1L, 1L)
        val catalog = Catalog(products)

        catalog.selectProducts(recommendedMutable, excludedMutable, 10)

        assertEquals(productsCopy, products)
        assertEquals(recommended, recommendedMutable)
        assertEquals(listOf(1L, 1L), excludedMutable)
    }

    @Test
    fun appliesLimitInRecommendationOrder() {
        val catalog = Catalog(products)

        val result = catalog.selectProducts(recommended, listOf(1L), 1)

        assertEquals(listOf(monitor), result)
    }

    @Test
    fun returnsEmptyListForZeroLimit() {
        val catalog = Catalog(products)

        val result = catalog.selectProducts(recommended, emptyList(), 0)

        assertEquals(emptyList<Product>(), result)
        assertEquals(0, catalog.counter)
    }

    @Test
    fun resetsCounterAfterZeroLimitRequest() {
        val catalog = Catalog(products)
        catalog.selectProducts(recommended, listOf(1L), 10)

        catalog.selectProducts(recommended, listOf(1L), 0)

        assertEquals(0, catalog.counter)
    }

    @Test
    fun returnsAllFoundWhenLimitIsBigger() {
        val catalog = Catalog(products)

        val result = catalog.selectProducts(recommended, listOf(1L), 10)

        assertEquals(listOf(monitor, webcam), result)
        assertEquals(recommended.size, catalog.counter)
    }

    @Test
    fun stopsViewingWhenResultIsFull() {
        val catalog = Catalog(products)
        val longRecommended = listOf(3L, 4L) + List(100_000) { 99L }

        val result = catalog.selectProducts(longRecommended, emptyList(), 2)

        assertEquals(listOf(monitor, webcam), result)
        assertEquals(2, catalog.counter)
    }

    @Test
    fun countsDuplicatesAndUnknownIdsBeforeFullResult() {
        val catalog = Catalog(products)
        val rec = listOf(3L, 3L, 99L, 2L, 4L, 1L, 1L)

        val result = catalog.selectProducts(rec, emptyList(), 2)

        assertEquals(listOf(monitor, webcam), result)
        assertEquals(5, catalog.counter)
    }

    @Test
    fun queriesToSameCatalogDoNotAffectEachOther() {
        val catalog = Catalog(products)

        val first = catalog.selectProducts(listOf(3L, 4L), listOf(3L), 5)
        val second = catalog.selectProducts(listOf(3L, 4L), emptyList(), 5)
        val third = catalog.selectProducts(listOf(3L, 4L), listOf(3L), 1)

        assertEquals(listOf(webcam), first)
        assertEquals(listOf(monitor, webcam), second)
        assertEquals(listOf(webcam), third)
    }

    @Test
    fun counterIsResetForEachRequest() {
        val catalog = Catalog(products)

        catalog.selectProducts(listOf(99L, 98L, 97L), emptyList(), 5)
        assertEquals(3, catalog.counter)

        catalog.selectProducts(listOf(3L), emptyList(), 5)
        assertEquals(1, catalog.counter)
    }

    @Test
    fun usesNewDataAfterChangingCatalog() {
        val catalog = Catalog(listOf(monitor))
        assertEquals(listOf(monitor), catalog.selectProducts(listOf(3L, 4L), emptyList(), 5))

        catalog.changeListOfProducts(listOf(webcam))

        assertEquals(listOf(webcam), catalog.selectProducts(listOf(3L, 4L), emptyList(), 5))
    }

    @Test
    fun oldCatalogProductsAreNotReturnedAfterChange() {
        val catalog = Catalog(listOf(monitor))

        catalog.changeListOfProducts(listOf(keyboard))

        assertEquals(emptyList<Product>(), catalog.selectProducts(listOf(3L), emptyList(), 5))
    }
}
