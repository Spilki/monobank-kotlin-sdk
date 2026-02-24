package app.spilki.monobank.acquiring.model.subscription

/**
 * Request body for subscription editing.
 *
 * @property subId Subscription identifier.
 * @property amount Updated recurring amount in minor units.
 * @property interval Updated recurring interval descriptor.
 */
public data class EditSubscriptionRequest(
    val subId: String,
    val amount: Long? = null,
    val interval: String? = null,
)
