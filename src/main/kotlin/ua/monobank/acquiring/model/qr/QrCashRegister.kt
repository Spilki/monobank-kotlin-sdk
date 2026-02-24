package ua.monobank.acquiring.model.qr

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * QR cash register descriptor.
 *
 * @property qrId QR identifier.
 * @property name Display name.
 * @property amount Default amount in minor units.
 * @property ccy Numeric ISO 4217 currency code.
 * @property active Whether QR register is active.
 * @property employeeId Linked employee identifier.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public data class QrCashRegister(
    val qrId: String,
    val name: String? = null,
    val amount: Long? = null,
    val ccy: Int? = null,
    val active: Boolean? = null,
    val employeeId: String? = null,
)
