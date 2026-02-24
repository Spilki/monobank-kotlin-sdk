package app.spilki.monobank.acquiring.internal

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.kotlinModule

/**
 * Shared Jackson codec for Monobank API payloads.
 */
internal object JsonCodec {
    internal val mapper: ObjectMapper =
        ObjectMapper()
            .registerModule(kotlinModule())
            .setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

    internal fun write(value: Any): String = mapper.writeValueAsString(value)

    internal fun <T> read(
        json: String,
        clazz: Class<T>,
    ): T = mapper.readValue(json, clazz)

    internal fun <T> readList(
        json: String,
        elementType: Class<T>,
    ): List<T> =
        mapper.readValue(
            json,
            mapper.typeFactory.constructCollectionType(List::class.java, elementType),
        )
}
