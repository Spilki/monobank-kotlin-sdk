package app.spilki.monobank.acquiring.webhook

import app.spilki.monobank.acquiring.api.MerchantApi
import java.security.KeyFactory
import java.security.PublicKey
import java.security.Signature
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

/**
 * Verifies Monobank webhook `X-Sign` signatures using ECDSA SHA256.
 *
 * @constructor Creates verifier and uses [merchantApi] to load public key.
 * @property merchantApi Merchant API used to fetch current public key.
 */
public class WebhookVerifier(
    private val merchantApi: MerchantApi,
) {
    @Volatile
    private var cachedPublicKey: PublicKey? = null

    /**
     * Verifies webhook body signature.
     *
     * @param xSignBase64 Base64 signature from `X-Sign` header.
     * @param bodyBytes Raw request body bytes.
     * @return `true` when signature is valid.
     */
    public fun verify(
        xSignBase64: String,
        bodyBytes: ByteArray,
    ): Boolean {
        if (xSignBase64.isBlank() || bodyBytes.isEmpty()) {
            return false
        }
        return runCatching {
            val signature = Signature.getInstance("SHA256withECDSA")
            signature.initVerify(getOrLoadPublicKey())
            signature.update(bodyBytes)
            signature.verify(Base64.getDecoder().decode(xSignBase64))
        }.getOrDefault(false)
    }

    private fun getOrLoadPublicKey(): PublicKey {
        val existing = cachedPublicKey
        if (existing != null) {
            return existing
        }
        return synchronized(this) {
            val current = cachedPublicKey
            if (current != null) {
                current
            } else {
                val loaded = fetchPublicKey()
                cachedPublicKey = loaded
                loaded
            }
        }
    }

    private fun fetchPublicKey(): PublicKey {
        val encodedPem = merchantApi.publicKey().key
        val pemString = String(Base64.getDecoder().decode(encodedPem))
        val sanitized =
            pemString
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replace("\n", "")
                .replace("\r", "")
                .trim()
        val derBytes = Base64.getDecoder().decode(sanitized)
        val keySpec = X509EncodedKeySpec(derBytes)
        return KeyFactory.getInstance("EC").generatePublic(keySpec)
    }
}
