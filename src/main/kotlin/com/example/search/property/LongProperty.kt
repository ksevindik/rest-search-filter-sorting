package com.example.search.property

import com.example.search.exception.PropertyParseException

class LongProperty(alias: String, propertyPath: String) : Property<Long>(alias, propertyPath) {
    override fun parse(rawValue: String): Long {
        try {
            return rawValue.toLong()
        } catch (ex: Exception) {
            throw PropertyParseException("Cannot parse value $rawValue for property $alias", ex)
        }
    }
}
