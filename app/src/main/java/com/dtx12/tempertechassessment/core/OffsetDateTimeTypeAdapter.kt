package com.dtx12.tempertechassessment.core

import com.google.gson.*
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.lang.reflect.Type

internal class OffsetDateTimeTypeAdapter : JsonSerializer<OffsetDateTime>,
    JsonDeserializer<OffsetDateTime> {
    override fun serialize(
        src: OffsetDateTime,
        typeOfSrc: Type,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext?
    ): OffsetDateTime {
        return OffsetDateTime.parse(json.asString, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }
}
