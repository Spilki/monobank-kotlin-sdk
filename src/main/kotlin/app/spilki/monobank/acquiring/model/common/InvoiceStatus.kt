package app.spilki.monobank.acquiring.model.common

import com.fasterxml.jackson.annotation.JsonValue

/**
 * Invoice lifecycle status from Monobank.
 *
 * @property value Serialized API value.
 */
public enum class InvoiceStatus(
    @get:JsonValue public val value: String,
) {
    /** Invoice is created and pending. */
    CREATED("created"),

    /** Invoice is being processed. */
    PROCESSING("processing"),

    /** Invoice is in hold state. */
    HOLD("hold"),

    /** Invoice is paid successfully. */
    SUCCESS("success"),

    /** Invoice payment failed. */
    FAILURE("failure"),

    /** Invoice was reversed. */
    REVERSED("reversed"),

    /** Invoice expired. */
    EXPIRED("expired"),
}
