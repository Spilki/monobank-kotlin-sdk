package ua.monobank.acquiring.model.common

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Payment details returned for completed card operations.
 *
 * @property maskedPan Masked PAN.
 * @property approvalCode Acquirer approval code.
 * @property rrn Retrieval reference number.
 * @property amount Captured amount in minor units.
 * @property ccy Numeric ISO 4217 currency code.
 * @property terminal Terminal identifier.
 * @property paymentSystem Payment network name.
 * @property paymentMethod Payment method name.
 * @property fee Processing fee in minor units.
 * @property country Card issuer country code.
 * @property bank Issuer bank name.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public data class PaymentInfo(
    val maskedPan: String? = null,
    val approvalCode: String? = null,
    val rrn: String? = null,
    val amount: Long? = null,
    val ccy: Int? = null,
    val terminal: String? = null,
    val paymentSystem: String? = null,
    val paymentMethod: String? = null,
    val fee: Long? = null,
    val country: String? = null,
    val bank: String? = null,
)
