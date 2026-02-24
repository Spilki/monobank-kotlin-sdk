package app.spilki.monobank.acquiring.webhook

import app.spilki.monobank.acquiring.internal.JsonCodec
import app.spilki.monobank.acquiring.model.invoice.InvoiceStatusResponse

/**
 * Parses Monobank webhook request bodies.
 *
 * Webhook body structure is identical to the Invoice Status API response.
 */
public object WebhookParser {

    /**
     * Deserializes a webhook JSON body into [InvoiceStatusResponse].
     *
     * @param json Raw JSON string from the webhook request body.
     * @return Parsed invoice status response.
     * @throws com.fasterxml.jackson.core.JsonProcessingException if JSON is malformed.
     */
    @JvmStatic
    public fun parse(json: String): InvoiceStatusResponse =
        JsonCodec.read(json, InvoiceStatusResponse::class.java)
}
