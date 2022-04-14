package com.example.search.property

import com.example.search.exception.PropertyParseException

class BooleanProperty(alias: String, propertyPath: String) : Property<Boolean>(alias, propertyPath) {
    override fun parse(rawValue: String): Boolean {
        val v = rawValue.trim()
        if (v.equals("true", true)) return true
        else if (v.equals("false", true)) return false
        else throw PropertyParseException("Cannot parse value $rawValue for property $alias")
    }
}
