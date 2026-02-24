package app.spilki.monobank.acquiring.api

import app.spilki.monobank.acquiring.internal.HttpTransport
import app.spilki.monobank.acquiring.model.split.SplitReceiver

/**
 * API client for split receivers.
 *
 * @constructor Internal constructor used by [app.spilki.monobank.acquiring.MonobankAcquiring].
 */
public class SplitApi internal constructor(
    private val transport: HttpTransport,
) {
    /**
     * Lists split receivers configured for merchant.
     *
     * @return Split receiver list.
     */
    public fun receivers(): List<SplitReceiver> =
        transport.getList("/api/merchant/split-receiver/list", SplitReceiver::class.java)
}
