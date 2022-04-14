package com.example.search.property

import com.example.search.exception.PropertyParseException

class EnumProperty<T : Enum<T>>(alias: String, propertyPath: String, private val enumValues: Array<T>) :
    Property<T>(alias, propertyPath) {
    override fun parse(rawValue: String): T {
        for (i in enumValues) {
            if (rawValue.equals(i.name, true))
                return i
        }
        throw PropertyParseException("Cannot parse value $rawValue for property $alias")
    }
}
