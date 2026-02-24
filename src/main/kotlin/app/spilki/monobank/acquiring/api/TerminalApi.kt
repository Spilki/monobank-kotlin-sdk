package app.spilki.monobank.acquiring.api

import app.spilki.monobank.acquiring.internal.HttpTransport
import app.spilki.monobank.acquiring.model.terminal.Terminal

/**
 * API client for T2P terminal endpoints.
 *
 * @constructor Internal constructor used by [app.spilki.monobank.acquiring.MonobankAcquiring].
 */
public class TerminalApi internal constructor(
    private val transport: HttpTransport,
) {
    /**
     * Lists merchant terminals.
     *
     * @return Terminal list.
     */
    public fun list(): List<Terminal> = transport.getList("/api/merchant/t2p/terminal/list", Terminal::class.java)
}
