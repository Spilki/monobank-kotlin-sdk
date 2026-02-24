package app.spilki.monobank.acquiring.model.merchant

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Statement transaction item.
 *
 * @property id Statement entry identifier.
 * @property time Unix timestamp in seconds.
 * @property description Transaction description.
 * @property mcc Merchant category code.
 * @property originalMcc Original MCC.
 * @property amount Transaction amount in minor units.
 * @property operationAmount Operation amount in account currency minor units.
 * @property ccy Numeric ISO 4217 currency code.
 * @property commissionRate Commission amount in minor units.
 * @property cashbackAmount Cashback amount in minor units.
 * @property balance Balance after operation in minor units.
 * @property hold Whether operation is held.
 * @property receiptId Receipt identifier.
 * @property invoiceId Related invoice identifier.
 * @property counterEdrpou Counterparty EDRPOU.
 * @property counterIban Counterparty IBAN.
 * @property counterName Counterparty name.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public data class StatementItem(
    val id: String? = null,
    val time: Long? = null,
    val description: String? = null,
    val mcc: Int? = null,
    val originalMcc: Int? = null,
    val amount: Long? = null,
    val operationAmount: Long? = null,
    val ccy: Int? = null,
    val commissionRate: Long? = null,
    val cashbackAmount: Long? = null,
    val balance: Long? = null,
    val hold: Boolean? = null,
    val receiptId: String? = null,
    val invoiceId: String? = null,
    val counterEdrpou: String? = null,
    val counterIban: String? = null,
    val counterName: String? = null,
)
