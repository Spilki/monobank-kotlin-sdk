package app.spilki.monobank.acquiring.api

import app.spilki.monobank.acquiring.internal.HttpTransport
import app.spilki.monobank.acquiring.model.subscription.CreateSubscriptionRequest
import app.spilki.monobank.acquiring.model.subscription.CreateSubscriptionResponse
import app.spilki.monobank.acquiring.model.subscription.EditSubscriptionRequest
import app.spilki.monobank.acquiring.model.subscription.RemoveSubscriptionRequest
import app.spilki.monobank.acquiring.model.subscription.SubscriptionPaymentItem
import app.spilki.monobank.acquiring.model.subscription.SubscriptionStatusResponse

/**
 * API client for subscription endpoints.
 *
 * @constructor Internal constructor used by [app.spilki.monobank.acquiring.MonobankAcquiring].
 */
public class SubscriptionApi internal constructor(
    private val transport: HttpTransport,
) {
    /**
     * Creates a new subscription.
     *
     * @param request Subscription creation payload.
     * @return Created subscription response.
     */
    public fun create(request: CreateSubscriptionRequest): CreateSubscriptionResponse =
        transport.post("/api/merchant/subscription/create", request, CreateSubscriptionResponse::class.java)

    /**
     * Loads subscription status.
     *
     * @param subId Subscription identifier.
     * @return Subscription status response.
     */
    public fun status(subId: String): SubscriptionStatusResponse =
        transport.get(
            "/api/merchant/subscription/status?subId=${transport.encode(subId)}",
            SubscriptionStatusResponse::class.java,
        )

    /**
     * Loads subscription payment history.
     *
     * @param subId Subscription identifier.
     * @return Subscription payment items.
     */
    public fun payments(subId: String): List<SubscriptionPaymentItem> =
        transport.getList(
            "/api/merchant/subscription/payments?subId=${transport.encode(subId)}",
            SubscriptionPaymentItem::class.java,
        )

    /**
     * Loads subscriptions for a wallet.
     *
     * @param walletId Wallet identifier.
     * @return Subscription list.
     */
    public fun list(walletId: String): List<SubscriptionStatusResponse> =
        transport.getList(
            "/api/merchant/subscription/list?walletId=${transport.encode(walletId)}",
            SubscriptionStatusResponse::class.java,
        )

    /**
     * Removes a subscription.
     *
     * @param subId Subscription identifier.
     */
    public fun remove(subId: String): Unit =
        transport.postUnit("/api/merchant/subscription/remove", RemoveSubscriptionRequest(subId))

    /**
     * Updates subscription parameters.
     *
     * @param request Edit request payload.
     */
    public fun edit(request: EditSubscriptionRequest): Unit =
        transport.postUnit("/api/merchant/subscription/edit", request)
}
