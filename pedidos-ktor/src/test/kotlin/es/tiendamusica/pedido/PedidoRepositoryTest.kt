package es.tiendamusica.pedido

import com.mongodb.internal.connection.tlschannel.util.Util.assertTrue
import es.tiendamusica.models.Pedido
import es.tiendamusica.repository.pedidos.PedidosRepository
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.*
import org.litote.kmongo.toId
import java.time.LocalDate
import java.util.*
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

class PedidoRepositoryTest {
    private val repo = PedidosRepository()
    private val user = UUID.randomUUID().toString()
    private val pedidoTest = Pedido(
        userId = user,
        price = 4.93,
        status = Pedido.Status.FINISHED,
        createdAt = LocalDate.now(),
        deliveredAt = LocalDate.now(),
        productos = mutableListOf()
    )

    @BeforeEach
    fun setup() = runTest {
        repo.save(pedidoTest)
    }

    @AfterEach
    fun tearDown() = runTest {
        repo.delete(pedidoTest)
    }


    @Test
    fun findAll() = runTest {
        val res = repo.findAll().toList()

        assertTrue(res.isNotEmpty())
    }


    @Test
    fun findById() = runTest {
        val res = repo.findById(pedidoTest.id.toId())

        assertAll(
            {
                assertEquals(res!!.createdAt, pedidoTest.createdAt)
                assertEquals(res.price, pedidoTest.price)
            }
        )

    }

    @Test
    fun save() = runTest {

        val pedido = Pedido(
            userId = user,
            price = 4.95,
            status = Pedido.Status.FINISHED,
            createdAt = LocalDate.now(),
            deliveredAt = LocalDate.now(),
            productos = mutableListOf()
        )

        val res = repo.save(pedido)

        assertAll(
            {
                assertEquals(res.createdAt, pedido.createdAt)
                assertEquals(res.price, pedido.price)
            }
        )

    }

    @Test
    fun delete() = runTest {
        val res = repo.delete(pedidoTest)
        assertTrue(res)
    }

}
































