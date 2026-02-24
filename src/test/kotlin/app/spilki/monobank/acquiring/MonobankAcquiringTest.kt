package app.spilki.monobank.acquiring

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MonobankAcquiringTest {
    private lateinit var server: MockWebServer

    @BeforeEach
    fun setUp() {
        server = MockWebServer()
        server.start()
    }

    @AfterEach
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `initializes all api clients`() {
        val client =
            MonobankAcquiring(
                MonobankAcquiringConfig(
                    token = "test-token",
                    baseUrl = server.url("/").toString().removeSuffix("/"),
                ),
            )

        assertThat(client.invoices).isNotNull
        assertThat(client.merchant).isNotNull
        assertThat(client.subscriptions).isNotNull
        assertThat(client.qr).isNotNull
        assertThat(client.wallet).isNotNull
        assertThat(client.split).isNotNull
        assertThat(client.monopay).isNotNull
        assertThat(client.submerchants).isNotNull
        assertThat(client.terminals).isNotNull
        assertThat(client.webhooks).isNotNull
    }

    @Test
    fun `passes config to transport headers and base url`() {
        server.enqueue(
            MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody("""{"merchantId":"m1","merchantName":"Mono","edrpou":"12345678"}"""),
        )

        val client =
            MonobankAcquiring(
                MonobankAcquiringConfig(
                    token = "cfg-token",
                    baseUrl = server.url("/").toString().removeSuffix("/"),
                    cmsName = "MyCMS",
                    cmsVersion = "2.0.1",
                ),
            )

        client.merchant.details()

        val recorded = server.takeRequest()
        assertThat(recorded.path).isEqualTo("/api/merchant/details")
        assertThat(recorded.getHeader("X-Token")).isEqualTo("cfg-token")
        assertThat(recorded.getHeader("X-Cms")).isEqualTo("MyCMS")
        assertThat(recorded.getHeader("X-Cms-Version")).isEqualTo("2.0.1")
    }
}
