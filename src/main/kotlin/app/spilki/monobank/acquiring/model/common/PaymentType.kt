package app.spilki.monobank.acquiring.model.common

import com.fasterxml.jackson.annotation.JsonValue

/**
 * Invoice payment type supported by Monobank.
 *
 * @property value Serialized API value.
 */
public enum class PaymentType(
    @get:JsonValue public val value: String,
) {
    /** Immediate debit payment. */
    DEBIT("debit"),

    /** Hold authorization payment. */
    HOLD("hold"),
}
