package com.example

import com.example.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*

class OrderRouteTests {
    @Test
    fun testsGetOrder() = testApplication {
        val response = client.get("/order/28-02-2024")

        assertEquals(
            """{"number":"28-02-2024","contents":[{"item":"Sandwich","amount":6,"price":20.0},{"item":"MeatBall Sandwich","amount":4,"price":28.5},{"item":"Triple CheeseBurger","amount":7,"price":30.0},{"item":"Krusty Burger","amount":10,"price":25.0}]}""",
            response.bodyAsText()
            )

        assertEquals(response.status, HttpStatusCode.OK)
    }

    @Test
    fun getAllReturnsListOfOrders() {
        // Arrange

    }
}