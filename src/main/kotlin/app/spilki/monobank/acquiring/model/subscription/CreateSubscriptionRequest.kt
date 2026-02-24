package app.spilki.monobank.acquiring.model.subscription

/**
 * Request body for subscription creation.
 *
 * @property amount Recurring payment amount in minor units.
 * @property ccy Numeric ISO 4217 currency code.
 * @property interval Recurring interval descriptor.
 * @property redirectUrl Redirect URL after subscription setup.
 * @property webHookUrls Webhook URLs for subscription callbacks.
 * @property validity Setup page validity in seconds.
 */
public data class CreateSubscriptionRequest(
    val amount: Long,
    val ccy: Int,
    val interval: String,
    val redirectUrl: String? = null,
    val webHookUrls: List<String>? = null,
    val validity: Long? = null,
)
