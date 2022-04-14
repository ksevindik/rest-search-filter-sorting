package com.example.search.property

import com.example.search.exception.PropertyParseException
import java.text.SimpleDateFormat
import java.util.Date

class DateProperty(alias: String, propertyPath: String, private val dateFormat: String) :
    Property<Date>(alias, propertyPath) {
    override fun parse(rawValue: String): Date {
        try {
            val simpleDateFormat = SimpleDateFormat(dateFormat)
            return simpleDateFormat.parse(rawValue)
        } catch (ex: Exception) {
            throw PropertyParseException("Cannot parse value $rawValue for property $alias with format $dateFormat", ex)
        }
    }
}
