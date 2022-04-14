package com.example.search.property

import com.example.search.exception.PropertyParseException
import java.math.BigDecimal

class BigDecimalProperty(alias: String, propertyPath: String) : Property<BigDecimal>(alias, propertyPath) {
    override fun parse(rawValue: String): BigDecimal {
        try {
            return BigDecimal(rawValue)
        } catch (ex: Exception) {
            throw PropertyParseException("Cannot parse value $rawValue for property $alias", ex)
        }
    }
}
