package com.example.search.property

import com.example.search.exception.PropertyParseException

abstract class Property<T>(val alias: String, val propertyPath: String) {

    @Throws(PropertyParseException::class)
    abstract fun parse(rawValue: String): T

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Property<*>) return false
        if (!other::class.equals(this::class)) return false
        if (alias != other.alias) return false

        return true
    }

    override fun hashCode(): Int {
        return alias.hashCode()
    }

    override fun toString(): String {
        return "${this::class.simpleName} (alias='$alias', propertyPath='$propertyPath')"
    }
}
