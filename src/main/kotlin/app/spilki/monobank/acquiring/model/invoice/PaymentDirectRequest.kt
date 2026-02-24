package app.spilki.monobank.acquiring.model.invoice

/**
 * Request for direct card payment against existing invoice.
 *
 * @property invoiceId Invoice identifier.
 * @property cardData Raw card details payload.
 */
public data class PaymentDirectRequest(
    val invoiceId: String,
    val cardData: CardData,
) {
    /**
     * Card details used by payment-direct endpoint.
     *
     * @property pan Card PAN.
     * @property exp Expiration date in `MMYY` format.
     * @property cvv Card CVV/CVC.
     */
    public data class CardData(
        val pan: String,
        val exp: String,
        val cvv: String,
    )
}
