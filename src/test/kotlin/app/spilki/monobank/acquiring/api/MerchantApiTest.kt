package app.spilki.monobank.acquiring.api

import app.spilki.monobank.acquiring.MonobankAcquiring
import app.spilki.monobank.acquiring.MonobankAcquiringConfig
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MerchantApiTest {
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
    fun `details loads merchant payload`() {
        server.enqueue(
            MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody("""{"merchantId":"m1","merchantName":"Mono Shop","edrpou":"12345678"}"""),
        )

        val response = client.merchant.details()

        assertThat(response.merchantId).isEqualTo("m1")
        val recorded = server.takeRequest()
        assertThat(recorded.method).isEqualTo("GET")
        assertThat(recorded.path).isEqualTo("/api/merchant/details")
    }

    @Test
    fun `publicKey loads key payload`() {
        server.enqueue(
            MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody("""{"key":"base64-key"}"""),
        )

        val response = client.merchant.publicKey()

        assertThat(response.key).isEqualTo("base64-key")
        val recorded = server.takeRequest()
        assertThat(recorded.path).isEqualTo("/api/merchant/pubkey")
    }

    @Test
    fun `statement sends period query and parses list`() {
        server.enqueue(
            MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody("""[{"id":"st-1","amount":4200,"ccy":980}]"""),
        )

        val response = client.merchant.statement(from = 1_700_000_000, to = 1_700_100_000)

        assertThat(response).hasSize(1)
        assertThat(response.first().id).isEqualTo("st-1")
        val recorded = server.takeRequest()
        assertThat(recorded.path).isEqualTo("/api/merchant/statement?from=1700000000&to=1700100000")
    }
}
