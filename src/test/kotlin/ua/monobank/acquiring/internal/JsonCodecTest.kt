package ua.monobank.acquiring.internal

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import ua.monobank.acquiring.model.common.CancelStatus
import ua.monobank.acquiring.model.common.PaymentType
import ua.monobank.acquiring.model.invoice.CreateInvoiceRequest
import ua.monobank.acquiring.model.invoice.CreateInvoiceResponse
import ua.monobank.acquiring.model.invoice.FinalizeInvoiceRequest
import ua.monobank.acquiring.model.invoice.FiscalCheck
import ua.monobank.acquiring.model.subscription.CreateSubscriptionRequest

class JsonCodecTest {
    @Test
    fun `serializes request dto with expected fields`() {
        val payload =
            CreateInvoiceRequest(
                amount = 4_200,
                ccy = 980,
                paymentType = PaymentType.DEBIT,
                extRef = "ext-1",
            )

        val json = JsonCodec.write(payload)
        val tree = JsonCodec.mapper.readTree(json)

        assertThat(tree.get("amount").asLong()).isEqualTo(4_200)
        assertThat(tree.get("ccy").asInt()).isEqualTo(980)
        assertThat(tree.get("paymentType").asText()).isEqualTo("debit")
        assertThat(tree.get("extRef").asText()).isEqualTo("ext-1")
    }

    @Test
    fun `deserializes response dto`() {
        val json = """{"invoiceId":"inv-1","pageUrl":"https://pay.mono.ua/inv-1"}"""

        val dto = JsonCodec.read(json, CreateInvoiceResponse::class.java)

        assertThat(dto.invoiceId).isEqualTo("inv-1")
        assertThat(dto.pageUrl).isEqualTo("https://pay.mono.ua/inv-1")
    }

    @Test
    fun `unknown fields are ignored`() {
        val json = """{"invoiceId":"inv-2","pageUrl":"https://pay","unknown":"value"}"""

        val dto = JsonCodec.read(json, CreateInvoiceResponse::class.java)

        assertThat(dto.invoiceId).isEqualTo("inv-2")
        assertThat(dto.pageUrl).isEqualTo("https://pay")
    }

    @Test
    fun `null fields are excluded from serialization`() {
        val payload =
            CreateSubscriptionRequest(
                amount = 2_500,
                ccy = 980,
                interval = "month",
                redirectUrl = null,
                webHookUrls = null,
            )

        val json = JsonCodec.write(payload)
        val tree = JsonCodec.mapper.readTree(json)

        assertThat(tree.has("redirectUrl")).isFalse()
        assertThat(tree.has("webHookUrls")).isFalse()
        assertThat(tree.get("interval").asText()).isEqualTo("month")
    }

    @Test
    fun `deserializes list payload`() {
        val json = """[{"checkId":"c1","status":"ok","type":"sale"},{"checkId":"c2","status":"ok","type":"refund"}]"""

        val list = JsonCodec.readList(json, FiscalCheck::class.java)

        assertThat(list).hasSize(2)
        assertThat(list.map { it.checkId }).containsExactly("c1", "c2")
    }

    @Test
    fun `serializes enum values by api representation`() {
        val payload = FinalizeInvoiceRequest(invoiceId = "inv-3")

        val json = JsonCodec.write(mapOf("status" to CancelStatus.SUCCESS, "payload" to payload))
        val tree = JsonCodec.mapper.readTree(json)

        assertThat(tree.get("status").asText()).isEqualTo("success")
        assertThat(tree.get("payload").get("invoiceId").asText()).isEqualTo("inv-3")
    }
}
