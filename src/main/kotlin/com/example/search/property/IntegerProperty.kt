package com.example.search.property

import com.example.search.exception.PropertyParseException

class IntegerProperty(alias: String, propertyPath: String) : Property<Int>(alias, propertyPath) {
    override fun parse(rawValue: String): Int {
        try {
            return Integer.parseInt(rawValue)
        } catch (ex: Exception) {
            throw PropertyParseException("Cannot parse value $rawValue for property $alias", ex)
        }
    }
}
