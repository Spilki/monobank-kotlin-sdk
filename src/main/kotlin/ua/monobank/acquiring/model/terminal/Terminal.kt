package ua.monobank.acquiring.model.terminal

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Terminal descriptor for T2P operations.
 *
 * @property terminalId Terminal identifier.
 * @property name Terminal name.
 * @property city Terminal city.
 * @property address Terminal address.
 * @property status Terminal status.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public data class Terminal(
    val terminalId: String? = null,
    val name: String? = null,
    val city: String? = null,
    val address: String? = null,
    val status: String? = null,
)
