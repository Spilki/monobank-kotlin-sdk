package app.spilki.monobank.acquiring.api

import app.spilki.monobank.acquiring.internal.HttpTransport
import app.spilki.monobank.acquiring.model.monopay.ImportPubKeyRequest
import app.spilki.monobank.acquiring.model.monopay.PubKeyItem

/**
 * API client for Monopay public key management.
 *
 * @constructor Internal constructor used by [app.spilki.monobank.acquiring.MonobankAcquiring].
 */
public class MonopayApi internal constructor(
    private val transport: HttpTransport,
) {
    /**
     * Imports Monopay public key.
     *
     * @param request Public key import payload.
     */
    public fun importPubKey(request: ImportPubKeyRequest): Unit =
        transport.postUnit("/api/merchant/monopay/pubkey-import", request)

    /**
     * Deletes Monopay public key.
     *
     * @param keyId Key identifier.
     */
    public fun deletePubKey(keyId: String): Unit =
        transport.postUnit("/api/merchant/monopay/pubkey-delete", mapOf("keyId" to keyId))

    /**
     * Lists Monopay public keys.
     *
     * @return Public key metadata list.
     */
    public fun listPubKeys(): List<PubKeyItem> =
        transport.getList("/api/merchant/monopay/pubkey-list", PubKeyItem::class.java)
}
