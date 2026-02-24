package app.spilki.monobank.acquiring.internal

import app.spilki.monobank.acquiring.MonobankAcquiringConfig
import app.spilki.monobank.acquiring.exception.MonobankApiException
import app.spilki.monobank.acquiring.exception.MonobankAuthException
import app.spilki.monobank.acquiring.exception.MonobankNetworkException
import app.spilki.monobank.acquiring.exception.MonobankRateLimitException
import app.spilki.monobank.acquiring.model.merchant.PublicKeyResponse
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class HttpTransportTest {
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
    fun `get operation sends token header and parses response`() {
        server.enqueue(
            MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody("""{"key":"public-key-value"}"""),
        )
        val transport = newTransport()

        val response = transport.get("/api/merchant/pubkey", PublicKeyResponse::class.java)

        assertThat(response.key).isEqualTo("public-key-value")
        val recorded = server.takeRequest()
        assertThat(recorded.method).isEqualTo("GET")
        assertThat(recorded.path).isEqualTo("/api/merchant/pubkey")
        assertThat(recorded.getHeader("X-Token")).isEqualTo("test-token")
    }

    @Test
    fun `post operation sends json body and parses response`() {
        server.enqueue(
            MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody("""{"key":"ok"}"""),
        )
        val transport = newTransport()

        val response = transport.post("/api/test", mapOf("name" to "mono"), PublicKeyResponse::class.java)

        assertThat(response.key).isEqualTo("ok")
        val recorded = server.takeRequest()
        assertThat(recorded.method).isEqualTo("POST")
        assertThat(recorded.getHeader("Content-Type")).isEqualTo("application/json")
        assertThat(recorded.body.readUtf8()).isEqualTo("{\"name\":\"mono\"}")
    }

    @Test
    fun `delete operation sends delete request`() {
        server.enqueue(MockResponse().setResponseCode(204))
        val transport = newTransport()

        transport.deleteUnit("/api/test")

        val recorded = server.takeRequest()
        assertThat(recorded.method).isEqualTo("DELETE")
        assertThat(recorded.path).isEqualTo("/api/test")
    }

    @Test
    fun `sends optional cms headers when configured`() {
        server.enqueue(
            MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody("""{"key":"ok"}"""),
        )
        val transport =
            HttpTransport(
                MonobankAcquiringConfig(
                    token = "test-token",
                    baseUrl = server.url("/").toString().removeSuffix("/"),
                    cmsName = "ShopCMS",
                    cmsVersion = "1.2.3",
                ),
            )

        transport.get("/api/merchant/pubkey", PublicKeyResponse::class.java)

        val recorded = server.takeRequest()
        assertThat(recorded.getHeader("X-Cms")).isEqualTo("ShopCMS")
        assertThat(recorded.getHeader("X-Cms-Version")).isEqualTo("1.2.3")
    }

    @Test
    fun `maps 400 response to MonobankApiException`() {
        server.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setHeader("Content-Type", "application/json")
                .setBody("""{"errCode":"E400","errText":"bad request"}"""),
        )
        val transport = newTransport()

        val ex =
            assertThrows<MonobankApiException> {
                transport.get("/api/merchant/pubkey", PublicKeyResponse::class.java)
            }
        assertThat(ex.statusCode).isEqualTo(400)
        assertThat(ex.errCode).isEqualTo("E400")
        assertThat(ex.errText).isEqualTo("bad request")
    }

    @Test
    fun `maps 403 response to MonobankAuthException`() {
        server.enqueue(
            MockResponse()
                .setResponseCode(403)
                .setHeader("Content-Type", "application/json")
                .setBody("""{"errCode":"E403","errText":"forbidden"}"""),
        )
        val transport = newTransport()

        val ex =
            assertThrows<MonobankAuthException> {
                transport.get("/api/merchant/pubkey", PublicKeyResponse::class.java)
            }
        assertThat(ex.statusCode).isEqualTo(403)
        assertThat(ex.errCode).isEqualTo("E403")
    }

    @Test
    fun `maps 429 response to MonobankRateLimitException`() {
        server.enqueue(
            MockResponse()
                .setResponseCode(429)
                .setHeader("Content-Type", "application/json")
                .setBody("""{"errCode":"E429","errText":"too many"}"""),
        )
        val transport = newTransport()

        val ex =
            assertThrows<MonobankRateLimitException> {
                transport.get("/api/merchant/pubkey", PublicKeyResponse::class.java)
            }
        assertThat(ex.statusCode).isEqualTo(429)
        assertThat(ex.errCode).isEqualTo("E429")
    }

    @Test
    fun `maps 500 response to MonobankNetworkException`() {
        server.enqueue(MockResponse().setResponseCode(500).setBody("server down"))
        val transport = newTransport()

        assertThatThrownBy { transport.get("/api/merchant/pubkey", PublicKeyResponse::class.java) }
            .isInstanceOf(MonobankNetworkException::class.java)
            .hasMessageContaining("HTTP 500")
    }

    @Test
    fun `network failures throw MonobankNetworkException`() {
        val transport =
            HttpTransport(
                MonobankAcquiringConfig(
                    token = "test-token",
                    baseUrl = "http://127.0.0.1:1",
                    connectTimeoutMs = 200,
                    readTimeoutMs = 200,
                ),
            )

        assertThatThrownBy { transport.get("/api/merchant/pubkey", PublicKeyResponse::class.java) }
            .isInstanceOf(MonobankNetworkException::class.java)
            .hasMessageContaining("network request failed")
    }

    @Test
    fun `empty error body is handled gracefully`() {
        server.enqueue(MockResponse().setResponseCode(400))
        val transport = newTransport()

        val ex =
            assertThrows<MonobankApiException> {
                transport.get("/api/merchant/pubkey", PublicKeyResponse::class.java)
            }
        assertThat(ex.errCode).isNull()
        assertThat(ex.errText).isNull()
    }

    @Test
    fun `non json error body is handled gracefully`() {
        server.enqueue(MockResponse().setResponseCode(400).setBody("plain error"))
        val transport = newTransport()

        val ex =
            assertThrows<MonobankApiException> {
                transport.get("/api/merchant/pubkey", PublicKeyResponse::class.java)
            }
        assertThat(ex.errCode).isNull()
        assertThat(ex.errText).isNull()
        assertThat(ex.responseBody).isEqualTo("plain error")
    }

    private fun newTransport(): HttpTransport =
        HttpTransport(
            MonobankAcquiringConfig(
                token = "test-token",
                baseUrl = server.url("/").toString().removeSuffix("/"),
                connectTimeoutMs = 1_000,
                readTimeoutMs = 1_000,
            ),
        )
}
