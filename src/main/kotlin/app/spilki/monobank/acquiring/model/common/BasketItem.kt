package app.spilki.monobank.acquiring.model.common

/**
 * Basket line item used in invoice operations.
 *
 * @property name Item name.
 * @property qty Quantity value.
 * @property sum Total line amount in minor currency units.
 * @property icon Optional icon URL.
 * @property unit Unit of measurement.
 * @property code Merchant item code.
 * @property barcode Item barcode.
 * @property header Header text printed above item.
 * @property footer Footer text printed below item.
 * @property tax Tax metadata.
 * @property uktzed UKTZED code.
 * @property discountType Discount type identifier.
 */
public data class BasketItem(
    val name: String? = null,
    val qty: Double? = null,
    val sum: Long? = null,
    val icon: String? = null,
    val unit: String? = null,
    val code: String? = null,
    val barcode: String? = null,
    val header: String? = null,
    val footer: String? = null,
    val tax: String? = null,
    val uktzed: String? = null,
    val discountType: String? = null,
)
