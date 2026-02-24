package ua.monobank.acquiring.model.subscription

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Response of subscription creation.
 *
 * @property subId Subscription identifier.
 * @property pageUrl Hosted setup page URL.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public data class CreateSubscriptionResponse(
    val subId: String,
    val pageUrl: String,
)
