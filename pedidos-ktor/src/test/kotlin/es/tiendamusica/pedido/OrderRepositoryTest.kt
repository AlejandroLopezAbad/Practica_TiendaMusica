package es.tiendamusica.pedido

import com.mongodb.internal.connection.tlschannel.util.Util.assertTrue
import es.tiendamusica.models.Order
import es.tiendamusica.repository.pedidos.OrderRepository
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.*
import org.litote.kmongo.toId
import java.time.LocalDate
import java.util.*
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

class OrderRepositoryTest {
    private val repo = OrderRepository()
    private val user = UUID.randomUUID().toString()
    private val orderTest = Order(
        userId = user,
        price = 4.93,
        status = Order.Status.FINISHED,
        createdAt = LocalDate.now(),
        deliveredAt = LocalDate.now(),
        productos = mutableListOf()
    )

    @BeforeEach
    fun setup() = runTest {
        repo.save(orderTest)
    }

    @AfterEach
    fun tearDown() = runTest {
        repo.delete(orderTest)
    }


    @Test
    fun findAll() = runTest {
        val res = repo.findAll().toList()

        assertTrue(res.isNotEmpty())
    }


    @Test
    fun findById() = runTest {
        val res = repo.findById(orderTest.id.toId())

        assertAll(
            {
                assertEquals(res!!.createdAt, orderTest.createdAt)
                assertEquals(res.price, orderTest.price)
            }
        )

    }

    @Test
    fun save() = runTest {

        val order = Order(
            userId = user,
            price = 4.95,
            status = Order.Status.FINISHED,
            createdAt = LocalDate.now(),
            deliveredAt = LocalDate.now(),
            productos = mutableListOf()
        )

        val res = repo.save(order)

        assertAll(
            {
                assertEquals(res.createdAt, order.createdAt)
                assertEquals(res.price, order.price)
            }
        )

    }

    @Test
    fun delete() = runTest {
        val res = repo.delete(orderTest)
        assertTrue(res)
    }

}
































