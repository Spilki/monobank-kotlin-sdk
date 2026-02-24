package ua.monobank.acquiring.api

import ua.monobank.acquiring.internal.HttpTransport
import ua.monobank.acquiring.model.invoice.CancelInvoiceRequest
import ua.monobank.acquiring.model.invoice.CancelInvoiceResponse
import ua.monobank.acquiring.model.invoice.CreateInvoiceRequest
import ua.monobank.acquiring.model.invoice.CreateInvoiceResponse
import ua.monobank.acquiring.model.invoice.FinalizeInvoiceRequest
import ua.monobank.acquiring.model.invoice.FinalizeInvoiceResponse
import ua.monobank.acquiring.model.invoice.FiscalCheck
import ua.monobank.acquiring.model.invoice.InvoiceStatusResponse
import ua.monobank.acquiring.model.invoice.PaymentDirectRequest
import ua.monobank.acquiring.model.invoice.PaymentDirectResponse
import ua.monobank.acquiring.model.invoice.RemoveInvoiceRequest

/**
 * API client for invoice endpoints.
 *
 * @constructor Internal constructor used by [ua.monobank.acquiring.MonobankAcquiring].
 */
public class InvoiceApi internal constructor(
    private val transport: HttpTransport,
) {
    /**
     * Creates a new invoice.
     *
     * @param request Invoice creation payload.
     * @return Created invoice response.
     */
    public fun create(request: CreateInvoiceRequest): CreateInvoiceResponse =
        transport.post("/api/merchant/invoice/create", request, CreateInvoiceResponse::class.java)

    /**
     * Loads current invoice status.
     *
     * @param invoiceId Invoice identifier.
     * @return Full invoice status payload.
     */
    public fun status(invoiceId: String): InvoiceStatusResponse =
        transport.get(
            "/api/merchant/invoice/status?invoiceId=${transport.encode(invoiceId)}",
            InvoiceStatusResponse::class.java,
        )

    /**
     * Cancels an invoice fully or partially.
     *
     * @param request Cancel request payload.
     * @return Cancel operation response.
     */
    public fun cancel(request: CancelInvoiceRequest): CancelInvoiceResponse =
        transport.post("/api/merchant/invoice/cancel", request, CancelInvoiceResponse::class.java)

    /**
     * Removes invoice from merchant cabinet.
     *
     * @param invoiceId Invoice identifier.
     */
    public fun remove(invoiceId: String): Unit =
        transport.postUnit("/api/merchant/invoice/remove", RemoveInvoiceRequest(invoiceId))

    /**
     * Finalizes a hold invoice.
     *
     * @param request Finalize request payload.
     * @return Finalization operation response.
     */
    public fun finalize(request: FinalizeInvoiceRequest): FinalizeInvoiceResponse =
        transport.post("/api/merchant/invoice/finalize", request, FinalizeInvoiceResponse::class.java)

    /**
     * Performs direct card payment for an invoice.
     *
     * @param request Direct payment payload.
     * @return Direct payment response.
     */
    public fun paymentDirect(request: PaymentDirectRequest): PaymentDirectResponse =
        transport.post("/api/merchant/invoice/payment-direct", request, PaymentDirectResponse::class.java)

    /**
     * Downloads invoice receipt binary payload.
     *
     * @param invoiceId Invoice identifier.
     * @return Receipt bytes.
     */
    public fun receipt(invoiceId: String): ByteArray =
        transport.getBytes("/api/merchant/invoice/receipt?invoiceId=${transport.encode(invoiceId)}")

    /**
     * Loads fiscal checks linked to invoice.
     *
     * @param invoiceId Invoice identifier.
     * @return Fiscal checks list.
     */
    public fun fiscalChecks(invoiceId: String): List<FiscalCheck> =
        transport.getList(
            "/api/merchant/invoice/fiscal-checks?invoiceId=${transport.encode(invoiceId)}",
            FiscalCheck::class.java,
        )
}
