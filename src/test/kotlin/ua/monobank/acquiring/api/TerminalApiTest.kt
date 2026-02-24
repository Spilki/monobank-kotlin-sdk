package ua.monobank.acquiring.api

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ua.monobank.acquiring.MonobankAcquiring
import ua.monobank.acquiring.MonobankAcquiringConfig

class TerminalApiTest {
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
    fun `list loads terminal entries`() {
        server.enqueue(
            MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody("""[{"terminalId":"t1","name":"Terminal 1"}]"""),
        )

        val response = client.terminals.list()

        assertThat(response).hasSize(1)
        assertThat(response.first().terminalId).isEqualTo("t1")
        assertThat(server.takeRequest().path).isEqualTo("/api/merchant/t2p/terminal/list")
    }
}
