package ua.monobank.acquiring

/**
 * Configuration for [MonobankAcquiring].
 *
 * @property token Merchant acquiring API token used in `X-Token` header.
 * @property baseUrl Base Monobank API URL.
 * @property connectTimeoutMs Connection timeout in milliseconds.
 * @property readTimeoutMs Read timeout in milliseconds.
 * @property cmsName Optional CMS name sent in `X-Cms` header.
 * @property cmsVersion Optional CMS version sent in `X-Cms-Version` header.
 */
public data class MonobankAcquiringConfig(
    val token: String,
    val baseUrl: String = "https://api.monobank.ua",
    val connectTimeoutMs: Long = 10_000,
    val readTimeoutMs: Long = 30_000,
    val cmsName: String? = null,
    val cmsVersion: String? = null,
)
