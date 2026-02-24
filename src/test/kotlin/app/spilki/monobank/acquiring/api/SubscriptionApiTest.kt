package app.spilki.monobank.acquiring.api

import app.spilki.monobank.acquiring.MonobankAcquiring
import app.spilki.monobank.acquiring.MonobankAcquiringConfig
import app.spilki.monobank.acquiring.model.subscription.CreateSubscriptionRequest
import app.spilki.monobank.acquiring.model.subscription.EditSubscriptionRequest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SubscriptionApiTest {
    private lateinit var server: MockWebServer
    private lateinit var client: MonobankAcquiring

    @BeforeEach
    fun setUp() {
        server = MockWebServer()
        server.start()
        client =
            MonobankAcquiring(
                MonobankAcquiringConfig(
                    token = "test-token",
                    baseUrl = server.url("/").toString().removeSuffix("/"),
                ),
            )
    }

    @AfterEach
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `create sends request and parses response`() {
        server.enqueue(
            MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody("""{"subId":"sub-1","pageUrl":"https://pay.mono.ua/sub-1"}"""),
        )

        val response =
            client.subscriptions.create(
                CreateSubscriptionRequest(amount = 5000, ccy = 980, interval = "month"),
            )

        assertThat(response.subId).isEqualTo("sub-1")
        val recorded = server.takeRequest()
        assertThat(recorded.method).isEqualTo("POST")
        assertThat(recorded.path).isEqualTo("/api/merchant/subscription/create")
    }

    @Test
    fun `status sends encoded sub id query`() {
        server.enqueue(
            MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody("""{"subId":"sub id/1","status":"active"}"""),
        )

        val response = client.subscriptions.status("sub id/1")

        assertThat(response.subId).isEqualTo("sub id/1")
        assertThat(server.takeRequest().path)
            .isEqualTo("/api/merchant/subscription/status?subId=sub+id%2F1")
    }

    @Test
    fun `payments loads list`() {
        server.enqueue(
            MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody("""[{"invoiceId":"inv-1","status":"success","amount":5000,"ccy":980}]"""),
        )

        val response = client.subscriptions.payments("sub-2")

        assertThat(response).hasSize(1)
        assertThat(response.first().invoiceId).isEqualTo("inv-1")
        assertThat(server.takeRequest().path)
            .isEqualTo("/api/merchant/subscription/payments?subId=sub-2")
    }

    @Test
    fun `list loads subscriptions by wallet id`() {
        server.enqueue(
            MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody("""[{"subId":"sub-3","walletId":"wallet-1"}]"""),
        )

        val response = client.subscriptions.list("wallet-1")

        assertThat(response).hasSize(1)
        assertThat(response.first().subId).isEqualTo("sub-3")
        assertThat(server.takeRequest().path)
            .isEqualTo("/api/merchant/subscription/list?walletId=wallet-1")
    }

    @Test
    fun `remove sends post body`() {
        server.enqueue(MockResponse().setResponseCode(200))

        client.subscriptions.remove("sub-4")

        val recorded = server.takeRequest()
        assertThat(recorded.method).isEqualTo("POST")
        assertThat(recorded.path).isEqualTo("/api/merchant/subscription/remove")
        assertThat(recorded.body.readUtf8()).isEqualTo("{\"subId\":\"sub-4\"}")
    }

    @Test
    fun `edit sends post body`() {
        server.enqueue(MockResponse().setResponseCode(200))

        client.subscriptions.edit(EditSubscriptionRequest(subId = "sub-5", amount = 6000, interval = "week"))

        val recorded = server.takeRequest()
        assertThat(recorded.method).isEqualTo("POST")
        assertThat(recorded.path).isEqualTo("/api/merchant/subscription/edit")
        assertThat(recorded.body.readUtf8()).contains("\"subId\":\"sub-5\"")
    }
}
