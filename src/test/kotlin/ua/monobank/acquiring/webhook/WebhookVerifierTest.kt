package ua.monobank.acquiring.webhook

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ua.monobank.acquiring.MonobankAcquiring
import ua.monobank.acquiring.MonobankAcquiringConfig
import java.nio.charset.StandardCharsets

class WebhookVerifierTest {
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
    fun `verifies known signature from monobank docs`() {
        server.enqueue(
            MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody("""{"key":"$PUB_KEY_BASE64"}"""),
        )

        val verified = client.webhooks.verify(X_SIGN_BASE64, WEBHOOK_BODY.toByteArray(StandardCharsets.UTF_8))

        assertThat(verified).isTrue()
        assertThat(server.takeRequest().path).isEqualTo("/api/merchant/pubkey")
    }

    @Test
    fun `blank signature returns false`() {
        val verified = client.webhooks.verify("   ", WEBHOOK_BODY.toByteArray(StandardCharsets.UTF_8))

        assertThat(verified).isFalse()
        assertThat(server.requestCount).isZero()
    }

    @Test
    fun `empty body returns false`() {
        val verified = client.webhooks.verify(X_SIGN_BASE64, byteArrayOf())

        assertThat(verified).isFalse()
        assertThat(server.requestCount).isZero()
    }

    @Test
    fun `invalid signature returns false`() {
        server.enqueue(
            MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody("""{"key":"$PUB_KEY_BASE64"}"""),
        )

        val verified = client.webhooks.verify("invalid-signature", WEBHOOK_BODY.toByteArray(StandardCharsets.UTF_8))

        assertThat(verified).isFalse()
    }

    @Test
    fun `public key is cached after first verification`() {
        server.enqueue(
            MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody("""{"key":"$PUB_KEY_BASE64"}"""),
        )

        val first = client.webhooks.verify(X_SIGN_BASE64, WEBHOOK_BODY.toByteArray(StandardCharsets.UTF_8))
        val second = client.webhooks.verify(X_SIGN_BASE64, WEBHOOK_BODY.toByteArray(StandardCharsets.UTF_8))

        assertThat(first).isTrue()
        assertThat(second).isTrue()
        assertThat(server.requestCount).isEqualTo(1)
    }

    @Suppress("detekt:MaxLineLength")
    private companion object {
        private const val PUB_KEY_BASE64 =
            "LS0tLS1CRUdJTiBQVUJMSUMgS0VZLS0tLS0KTUZrd0V3WUhLb1pJemowQ0FRWUlLb1pJemowREFRY0RRZ0FFQUc1LzZ3NnZubGJZb0ZmRHlYWE4vS29CbVVjTgo3NWJSUWg4MFBhaEdldnJoanFCQnI3OXNSS0JSbnpHODFUZVQ5OEFOakU1c0R3RmZ5Znhub0ZJcmZBPT0KLS0tLS1FTkQgUFVCTElDIEtFWS0tLS0tCg=="
        private const val X_SIGN_BASE64 =
            "MEUCIQC/mVKhi8FKoayul2Mim3E2oaIOCNJk5dEXxTqbkeJSOQIgOM0hsW0qcP2H8iXy1aQYpmY0SJWEaWur7nQXlKDCFxA="
        private const val WEBHOOK_BODY = """{
  "invoiceId": "p2_9ZgpZVsl3",
  "status": "created",
  "failureReason": "string",
  "amount": 4200,
  "ccy": 980,
  "finalAmount": 4200,
  "createdDate": "2019-08-24T14:15:22Z",
  "modifiedDate": "2019-08-24T14:15:22Z",
  "reference": "84d0070ee4e44667b31371d8f8813947",
  "cancelList": [
    {
      "status": "processing",
      "amount": 4200,
      "ccy": 980,
      "createdDate": "2019-08-24T14:15:22Z",
      "modifiedDate": "2019-08-24T14:15:22Z",
      "approvalCode": "662476",
      "rrn": "060189181768",
      "extRef": "635ace02599849e981b2cd7a65f417fe"
    }
  ]
}"""
    }
}
