package app.spilki.monobank.acquiring.api

import app.spilki.monobank.acquiring.internal.HttpTransport
import app.spilki.monobank.acquiring.model.qr.Employee
import app.spilki.monobank.acquiring.model.qr.QrCashRegister
import app.spilki.monobank.acquiring.model.qr.QrDetailsResponse
import app.spilki.monobank.acquiring.model.qr.ResetQrAmountRequest

/**
 * API client for QR and employee endpoints.
 *
 * @constructor Internal constructor used by [app.spilki.monobank.acquiring.MonobankAcquiring].
 */
public class QrApi internal constructor(
    private val transport: HttpTransport,
) {
    /**
     * Loads QR details.
     *
     * @param qrId QR identifier.
     * @return QR details response.
     */
    public fun details(qrId: String): QrDetailsResponse =
        transport.get("/api/merchant/qr/details?qrId=${transport.encode(qrId)}", QrDetailsResponse::class.java)

    /**
     * Resets QR amount to default value.
     *
     * @param qrId QR identifier.
     */
    public fun resetAmount(qrId: String): Unit =
        transport.postUnit("/api/merchant/qr/reset-amount", ResetQrAmountRequest(qrId))

    /**
     * Lists merchant QR cash registers.
     *
     * @return QR cash register list.
     */
    public fun list(): List<QrCashRegister> = transport.getList("/api/merchant/qr/list", QrCashRegister::class.java)

    /**
     * Lists merchant employees.
     *
     * @return Employee list.
     */
    public fun employees(): List<Employee> = transport.getList("/api/merchant/employee/list", Employee::class.java)
}
