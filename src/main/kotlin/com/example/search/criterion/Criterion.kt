package com.example.search.criterion

import com.example.search.property.Property
import com.example.search.visitor.SearchCriteriaVisitor

abstract class Criterion<T>(val property: Property<T>, val values: Array<T>) {
    abstract fun accept(visitor: SearchCriteriaVisitor)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Criterion<*>) {
            return false
        }
        if (!other::class.equals(this::class)) return false

        if (property != other.property) return false
        if (!values.contentEquals(other.values)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = property.hashCode()
        result = 31 * result + values.contentHashCode()
        return result
    }

    override fun toString(): String {
        return "${this::class.simpleName} (property=$property, rawValues=${values.contentToString()})"
    }
}
