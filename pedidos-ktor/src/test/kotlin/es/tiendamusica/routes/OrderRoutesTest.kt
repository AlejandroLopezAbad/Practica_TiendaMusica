package es.tiendamusica.routes

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import es.tiendamusica.dtos.OrderCreateDto
import es.tiendamusica.dtos.OrderDto
import es.tiendamusica.dtos.SellLine
import es.tiendamusica.models.Order
import es.tiendamusica.models.User
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.util.*
import io.ktor.server.config.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.statement.*
import io.ktor.server.testing.*


private val json = Json {
        ignoreUnknownKeys = true}

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class OrderRoutesTest {

    private val config = ApplicationConfig("application.config")


    val user = User(UUID.fromString("b39a2fd2-f7d7-405d-b73c-b68a8dedbcdf").toString())

    val products = mutableListOf(
        SellLine(UUID.randomUUID().toString(), (1..100).random().toDouble(), (1..15).random())
    )

    private val order = Order(
        price = products.sumOf { it.price * it.quantity} ,
        status = Order.Status.PROCESSING,
        createdAt = LocalDate.now(),
        deliveredAt = LocalDate.now(),
        userId = user.name,
        productos = products
    )

    private val create = OrderCreateDto(
        price = order.price,
        products = order.productos,
        userId = order.userId
    )

    @Test
    @org.junit.jupiter.api.Order(1)
    fun testGetAll() = testApplication {
        environment { config }
        val response = client.get("/Orders")
        assertEquals(HttpStatusCode.OK, response.status)
    }
    @Test
    @org.junit.jupiter.api.Order(2)
    fun testPost() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation){
                json()
            }
        }

        val response = client.post("/pedidos"){
            contentType(ContentType.Application.Json)
            setBody(create)
        }
        println(response.bodyAsText())
        val result = response.bodyAsText()
        val dept = json.decodeFromString<OrderDto>(result)
        assertAll(
            {assertEquals(order.price, dept.price)}
        )
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    fun testPatchNotFound() = testApplication {
        environment { config }
        val client = createClient {
            install(ContentNegotiation){
                json()
            }
        }


        val response = client.patch("/pedidos/${UUID.randomUUID()}"){
            contentType(ContentType.Application.Json)
            setBody(create)
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }


    @Test
    @org.junit.jupiter.api.Order(4)
    fun testDelete() = testApplication {

        environment { config }


        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        var response = client.post("/pedidos") {
            contentType(ContentType.Application.Json)
            setBody(create)
        }

        val dto = json.decodeFromString<OrderDto>(response.bodyAsText())

        response = client.delete("/pedidos/${dto.id}")

        assertEquals(HttpStatusCode.NoContent, response.status)
    }


    @Test
    @org.junit.jupiter.api.Order(5)
    fun testDeleteNotFound() = testApplication {

        environment { config }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.delete("/pedidos/${UUID.randomUUID()}")

        assertEquals(HttpStatusCode.NotFound, response.status)
    }






}


























