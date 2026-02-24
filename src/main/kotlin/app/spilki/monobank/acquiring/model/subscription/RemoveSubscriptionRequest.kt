package app.spilki.monobank.acquiring.model.subscription

/**
 * Request body for subscription removal.
 *
 * @property subId Subscription identifier.
 */
public data class RemoveSubscriptionRequest(
    val subId: String,
)
