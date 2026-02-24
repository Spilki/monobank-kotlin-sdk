package ua.monobank.acquiring.model.common

import com.fasterxml.jackson.annotation.JsonValue

/**
 * Cancellation/finalization operation status.
 *
 * @property value Serialized API value.
 */
public enum class CancelStatus(
    @get:JsonValue public val value: String,
) {
    /** Operation is processing. */
    PROCESSING("processing"),

    /** Operation completed successfully. */
    SUCCESS("success"),

    /** Operation failed. */
    FAILURE("failure"),
}
