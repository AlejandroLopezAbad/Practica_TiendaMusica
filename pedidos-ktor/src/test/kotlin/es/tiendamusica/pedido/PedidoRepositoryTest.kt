package es.tiendamusica.pedido

import com.mongodb.internal.connection.tlschannel.util.Util.assertTrue
import es.tiendamusica.models.Pedido
import es.tiendamusica.models.User
import es.tiendamusica.repository.pedidos.PedidosRepository
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertAll
import java.time.LocalDate
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PedidoRepositoryTest {
    private val repo = PedidosRepository()
    private val userTest = User(
        name = "jeremy"
    )
    private val pedidoTest = Pedido(
        usuario = userTest,
        price = 4.93,
        status = Pedido.Status.FINISHED,
        createdAt = LocalDate.now(),
        deliveredAt = LocalDate.now()
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
        val res = repo.findById(pedidoTest.id)

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
            usuario = userTest,
            price = 4.95,
            status = Pedido.Status.FINISHED,
            createdAt = LocalDate.now(),
            deliveredAt = LocalDate.now()
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
































