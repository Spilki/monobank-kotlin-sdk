package app.spilki.monobank.acquiring.model.common

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Single partial-cancel operation record.
 *
 * @property status Cancel operation status.
 * @property amount Cancelled amount in minor units.
 * @property ccy Numeric ISO 4217 currency code.
 * @property createdDate ISO-8601 creation timestamp.
 * @property modifiedDate ISO-8601 modification timestamp.
 * @property approvalCode Acquirer approval code.
 * @property rrn Retrieval reference number.
 * @property extRef External merchant reference.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public data class CancelListItem(
    val status: CancelStatus? = null,
    val amount: Long? = null,
    val ccy: Int? = null,
    val createdDate: String? = null,
    val modifiedDate: String? = null,
    val approvalCode: String? = null,
    val rrn: String? = null,
    val extRef: String? = null,
)
