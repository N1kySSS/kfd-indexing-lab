package lab2

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test

class ProductSelectionTest {
    @Test
    fun selectsAvailableProductsInRecommendationOrder() {
        val keyboard = Product(1, "Keyboard", true)
        val mouse = Product(2, "Mouse", false)
        val monitor = Product(3, "Monitor", true)
        val webcam = Product(4, "Webcam", true)
        val products = listOf(keyboard, webcam, mouse, monitor)

        val result = selectProducts(
            products,
            listOf(3L, 2L, 3L, 99L, 1L, 4L),
            listOf(1L)
        )

        assertEquals(listOf(monitor, webcam), result)
    }

    @Test
    fun returnsEmptyListWhenThereAreNoRecommendations() {
        val products = listOf(Product(1, "Keyboard", true))

        val result = selectProducts(products, emptyList(), emptyList())

        assertEquals(emptyList<Product>(), result)
    }

    @Test
    fun ignoresDuplicateRecommendations() {
        val a = Product(1, "A", true)
        val b = Product(2, "B", true)
        val products = listOf(a, b)

        val result = selectProducts(products, listOf(2L, 1L, 2L, 2L, 1L), emptyList())

        assertEquals(listOf(b, a), result)
    }

    @Test
    fun ignoresDuplicateExcludedIds() {
        val a = Product(1, "A", true)
        val b = Product(2, "B", true)
        val products = listOf(a, b)

        val result = selectProducts(products, listOf(1L, 2L), listOf(1L, 1L, 1L))

        assertEquals(listOf(b), result)
    }

    @Test
    fun skipsUnavailableExcludedAndUnknownProducts() {
        val available = Product(1, "Available", true)
        val unavailable = Product(2, "Unavailable", false)
        val excluded = Product(3, "Excluded", true)
        val products = listOf(available, unavailable, excluded)

        val result = selectProducts(
            products,
            listOf(2L, 3L, 99L, 1L),
            listOf(3L, 100L)
        )

        assertEquals(listOf(available), result)
    }

    @Test
    fun returnsEmptyListWhenCatalogIsEmpty() {
        val result = selectProducts(emptyList(), listOf(1L, 2L), listOf(3L))

        assertEquals(emptyList<Product>(), result)
    }

    @Test
    fun worksWithEmptyExcludedIds() {
        val a = Product(1, "A", true)
        val b = Product(2, "B", true)

        val result = selectProducts(listOf(a, b), listOf(2L, 1L), emptyList())

        assertEquals(listOf(b, a), result)
    }

    @Test
    fun returnsEmptyListWhenNoProductFits() {
        val unavailable = Product(1, "Unavailable", false)
        val excluded = Product(2, "Excluded", true)
        val products = listOf(unavailable, excluded)

        val result = selectProducts(products, listOf(1L, 2L, 99L), listOf(2L))

        assertEquals(emptyList<Product>(), result)
    }

    @Test
    fun returnsOriginalProductObjects() {
        val a = Product(1, "A", true)

        val result = selectProducts(listOf(a), listOf(1L), emptyList())

        assertEquals(1, result.size)
        assertSame(a, result[0])
        assertEquals(1L, result[0].id)
        assertEquals("A", result[0].name)
        assertEquals(true, result[0].available)
    }

    @Test
    fun doesNotChangeInputData() {
        val products = mutableListOf(
            Product(1, "A", true),
            Product(2, "B", false),
            Product(3, "C", true)
        )
        val recommended = mutableListOf(3L, 3L, 2L, 1L, 99L)
        val excluded = mutableListOf(1L, 1L, 100L)

        val productsCopy = products.toList()
        val recommendedCopy = recommended.toList()
        val excludedCopy = excluded.toList()

        selectProducts(products, recommended, excluded)

        assertEquals(productsCopy, products)
        assertEquals(recommendedCopy, recommended)
        assertEquals(excludedCopy, excluded)
    }

    @Test
    fun returnsSameResultOnRepeatedCalls() {
        val a = Product(1, "A", true)
        val b = Product(2, "B", true)
        val products = listOf(a, b)
        val recommended = listOf(2L, 1L, 2L)
        val excluded = listOf(99L)

        val first = selectProducts(products, recommended, excluded)
        val second = selectProducts(products, recommended, excluded)

        assertEquals(listOf(b, a), first)
        assertEquals(first, second)
    }
}
