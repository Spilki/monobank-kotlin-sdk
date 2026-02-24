package ua.monobank.acquiring.api

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ua.monobank.acquiring.MonobankAcquiring
import ua.monobank.acquiring.MonobankAcquiringConfig
import ua.monobank.acquiring.model.common.CancelStatus
import ua.monobank.acquiring.model.invoice.CancelInvoiceRequest
import ua.monobank.acquiring.model.invoice.CreateInvoiceRequest
import ua.monobank.acquiring.model.invoice.FinalizeInvoiceRequest
import ua.monobank.acquiring.model.invoice.PaymentDirectRequest

class InvoiceApiTest {
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
    fun `create sends correct request`() {
        server.enqueue(
            MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody("""{"invoiceId":"inv123","pageUrl":"https://pay.mono.ua/inv123"}"""),
        )

        val response = client.invoices.create(CreateInvoiceRequest(amount = 4200, ccy = 980))

        assertThat(response.invoiceId).isEqualTo("inv123")
        val recorded = server.takeRequest()
        assertThat(recorded.method).isEqualTo("POST")
        assertThat(recorded.path).isEqualTo("/api/merchant/invoice/create")
        assertThat(recorded.getHeader("X-Token")).isEqualTo("test-token")
        assertThat(recorded.body.readUtf8()).contains("\"amount\":4200")
    }

    @Test
    fun `status sends encoded query and parses response`() {
        server.enqueue(
            MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody("""{"invoiceId":"inv 1/2","status":"created","amount":4200,"ccy":980}"""),
        )

        val response = client.invoices.status("inv 1/2")

        assertThat(response.invoiceId).isEqualTo("inv 1/2")
        val recorded = server.takeRequest()
        assertThat(recorded.method).isEqualTo("GET")
        assertThat(recorded.path).isEqualTo("/api/merchant/invoice/status?invoiceId=inv+1%2F2")
    }

    @Test
    fun `cancel sends body and parses response`() {
        server.enqueue(
            MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody("""{"status":"processing"}"""),
        )

        val response = client.invoices.cancel(CancelInvoiceRequest(invoiceId = "inv-1", amount = 1000))

        assertThat(response.status).isEqualTo(CancelStatus.PROCESSING)
        val recorded = server.takeRequest()
        assertThat(recorded.method).isEqualTo("POST")
        assertThat(recorded.path).isEqualTo("/api/merchant/invoice/cancel")
        assertThat(recorded.body.readUtf8()).contains("\"invoiceId\":\"inv-1\"")
    }

    @Test
    fun `remove posts remove payload`() {
        server.enqueue(MockResponse().setResponseCode(200))

        client.invoices.remove("inv-2")

        val recorded = server.takeRequest()
        assertThat(recorded.method).isEqualTo("POST")
        assertThat(recorded.path).isEqualTo("/api/merchant/invoice/remove")
        assertThat(recorded.body.readUtf8()).isEqualTo("{\"invoiceId\":\"inv-2\"}")
    }

    @Test
    fun `finalize sends body and parses response`() {
        server.enqueue(
            MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody("""{"status":"success"}"""),
        )

        val response = client.invoices.finalize(FinalizeInvoiceRequest(invoiceId = "inv-3", amount = 4200))

        assertThat(response.status).isEqualTo(CancelStatus.SUCCESS)
        val recorded = server.takeRequest()
        assertThat(recorded.path).isEqualTo("/api/merchant/invoice/finalize")
        assertThat(recorded.body.readUtf8()).contains("\"invoiceId\":\"inv-3\"")
    }

    @Test
    fun `paymentDirect sends nested card data`() {
        server.enqueue(
            MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody("""{"invoiceId":"inv-4","status":"processing","trnId":"trn-1","amount":4200,"ccy":980}"""),
        )

        val response =
            client.invoices.paymentDirect(
                PaymentDirectRequest(
                    invoiceId = "inv-4",
                    cardData =
                        PaymentDirectRequest.CardData(
                            pan = "4444333322221111",
                            exp = "0128",
                            cvv = "123",
                        ),
                ),
            )

        assertThat(response.invoiceId).isEqualTo("inv-4")
        val recorded = server.takeRequest()
        assertThat(recorded.path).isEqualTo("/api/merchant/invoice/payment-direct")
        assertThat(recorded.body.readUtf8()).contains("\"cardData\"")
    }

    @Test
    fun `receipt downloads binary payload`() {
        server.enqueue(MockResponse().setResponseCode(200).setBody("receipt-bytes"))

        val response = client.invoices.receipt("inv-5")

        assertThat(String(response)).isEqualTo("receipt-bytes")
        val recorded = server.takeRequest()
        assertThat(recorded.method).isEqualTo("GET")
        assertThat(recorded.path).isEqualTo("/api/merchant/invoice/receipt?invoiceId=inv-5")
    }

    @Test
    fun `fiscalChecks loads list response`() {
        server.enqueue(
            MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody("""[{"checkId":"c1","status":"ok","type":"sale"}]"""),
        )

        val response = client.invoices.fiscalChecks("inv-6")

        assertThat(response).hasSize(1)
        assertThat(response.first().checkId).isEqualTo("c1")
        val recorded = server.takeRequest()
        assertThat(recorded.method).isEqualTo("GET")
        assertThat(recorded.path).isEqualTo("/api/merchant/invoice/fiscal-checks?invoiceId=inv-6")
    }
}
