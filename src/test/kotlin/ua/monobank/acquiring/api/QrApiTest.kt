package ua.monobank.acquiring.api

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ua.monobank.acquiring.MonobankAcquiring
import ua.monobank.acquiring.MonobankAcquiringConfig

class QrApiTest {
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
    fun `details sends encoded qr id query`() {
        server.enqueue(
            MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody("""{"qrId":"qr id/1","amount":4200,"ccy":980}"""),
        )

        val response = client.qr.details("qr id/1")

        assertThat(response.qrId).isEqualTo("qr id/1")
        assertThat(server.takeRequest().path).isEqualTo("/api/merchant/qr/details?qrId=qr+id%2F1")
    }

    @Test
    fun `resetAmount sends post payload`() {
        server.enqueue(MockResponse().setResponseCode(200))

        client.qr.resetAmount("qr-2")

        val recorded = server.takeRequest()
        assertThat(recorded.method).isEqualTo("POST")
        assertThat(recorded.path).isEqualTo("/api/merchant/qr/reset-amount")
        assertThat(recorded.body.readUtf8()).isEqualTo("{\"qrId\":\"qr-2\"}")
    }

    @Test
    fun `list loads qr cash registers`() {
        server.enqueue(
            MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody("""[{"qrId":"qr-3","name":"Main"}]"""),
        )

        val response = client.qr.list()

        assertThat(response).hasSize(1)
        assertThat(response.first().qrId).isEqualTo("qr-3")
        assertThat(server.takeRequest().path).isEqualTo("/api/merchant/qr/list")
    }

    @Test
    fun `employees loads employee list`() {
        server.enqueue(
            MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody("""[{"employeeId":"e-1","name":"Alice"}]"""),
        )

        val response = client.qr.employees()

        assertThat(response).hasSize(1)
        assertThat(response.first().employeeId).isEqualTo("e-1")
        assertThat(server.takeRequest().path).isEqualTo("/api/merchant/employee/list")
    }
}
