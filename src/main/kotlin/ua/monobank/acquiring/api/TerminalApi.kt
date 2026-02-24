package ua.monobank.acquiring.api

import ua.monobank.acquiring.internal.HttpTransport
import ua.monobank.acquiring.model.terminal.Terminal

/**
 * API client for T2P terminal endpoints.
 *
 * @constructor Internal constructor used by [ua.monobank.acquiring.MonobankAcquiring].
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
