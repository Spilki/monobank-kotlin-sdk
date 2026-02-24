package app.spilki.monobank.acquiring.model.qr

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Employee item from employee list endpoint.
 *
 * @property employeeId Employee identifier.
 * @property extRef External merchant reference.
 * @property name Employee display name.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public data class Employee(
    val employeeId: String,
    val extRef: String? = null,
    val name: String? = null,
)
