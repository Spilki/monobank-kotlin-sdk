package ua.monobank.acquiring.api

import ua.monobank.acquiring.internal.HttpTransport
import ua.monobank.acquiring.model.submerchant.Submerchant

/**
 * API client for submerchant endpoints.
 *
 * @constructor Internal constructor used by [ua.monobank.acquiring.MonobankAcquiring].
 */
public class SubmerchantApi internal constructor(
    private val transport: HttpTransport,
) {
    /**
     * Lists submerchants available to merchant account.
     *
     * @return Submerchant list.
     */
    public fun list(): List<Submerchant> = transport.getList("/api/merchant/submerchant/list", Submerchant::class.java)
}
