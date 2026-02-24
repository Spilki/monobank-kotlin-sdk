package ua.monobank.acquiring.api

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ua.monobank.acquiring.MonobankAcquiring
import ua.monobank.acquiring.MonobankAcquiringConfig
import ua.monobank.acquiring.model.wallet.WalletPaymentRequest

class WalletApiTest {
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
    fun `cards loads tokenized wallet cards`() {
        server.enqueue(
            MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody("""[{"cardToken":"ct-1","walletId":"w1"}]"""),
        )

        val response = client.wallet.cards("w1")

        assertThat(response).hasSize(1)
        assertThat(response.first().cardToken).isEqualTo("ct-1")
        assertThat(server.takeRequest().path).isEqualTo("/api/merchant/wallet?walletId=w1")
    }

    @Test
    fun `payment sends wallet payment payload`() {
        server.enqueue(
            MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody("""{"invoiceId":"inv-1","status":"processing","trnId":"trn-1","amount":1000,"ccy":980}"""),
        )

        val response =
            client.wallet.payment(
                WalletPaymentRequest(
                    cardToken = "ct-2",
                    amount = 1000,
                    ccy = 980,
                ),
            )

        assertThat(response.invoiceId).isEqualTo("inv-1")
        val recorded = server.takeRequest()
        assertThat(recorded.method).isEqualTo("POST")
        assertThat(recorded.path).isEqualTo("/api/merchant/wallet/payment")
        assertThat(recorded.body.readUtf8()).contains("\"cardToken\":\"ct-2\"")
    }

    @Test
    fun `deleteCard sends delete with query params`() {
        server.enqueue(MockResponse().setResponseCode(200))

        client.wallet.deleteCard("card token", "wallet/1")

        val recorded = server.takeRequest()
        assertThat(recorded.method).isEqualTo("DELETE")
        assertThat(recorded.path)
            .isEqualTo("/api/merchant/wallet/card?cardToken=card+token&walletId=wallet%2F1")
    }
}
