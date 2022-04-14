package com.example.search.property

class StringProperty(alias: String, propertyPath: String, val exactMatch: Boolean = false) :
    Property<String>(alias, propertyPath) {
    override fun parse(rawValue: String): String {
        return rawValue
    }
}
