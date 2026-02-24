package ua.monobank.acquiring.api

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ua.monobank.acquiring.MonobankAcquiring
import ua.monobank.acquiring.MonobankAcquiringConfig
import ua.monobank.acquiring.model.monopay.ImportPubKeyRequest

class MonopayApiTest {
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
    fun `importPubKey sends payload`() {
        server.enqueue(MockResponse().setResponseCode(200))

        client.monopay.importPubKey(ImportPubKeyRequest(key = "public-key", name = "main"))

        val recorded = server.takeRequest()
        assertThat(recorded.method).isEqualTo("POST")
        assertThat(recorded.path).isEqualTo("/api/merchant/monopay/pubkey-import")
        assertThat(recorded.body.readUtf8()).contains("\"key\":\"public-key\"")
    }

    @Test
    fun `deletePubKey sends key id payload`() {
        server.enqueue(MockResponse().setResponseCode(200))

        client.monopay.deletePubKey("key-1")

        val recorded = server.takeRequest()
        assertThat(recorded.method).isEqualTo("POST")
        assertThat(recorded.path).isEqualTo("/api/merchant/monopay/pubkey-delete")
        assertThat(recorded.body.readUtf8()).isEqualTo("{\"keyId\":\"key-1\"}")
    }

    @Test
    fun `listPubKeys loads key list`() {
        server.enqueue(
            MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody("""[{"keyId":"k1","name":"Main"}]"""),
        )

        val response = client.monopay.listPubKeys()

        assertThat(response).hasSize(1)
        assertThat(response.first().keyId).isEqualTo("k1")
        assertThat(server.takeRequest().path).isEqualTo("/api/merchant/monopay/pubkey-list")
    }
}
