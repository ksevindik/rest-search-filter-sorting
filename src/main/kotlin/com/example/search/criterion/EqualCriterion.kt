package com.example.search.criterion

import com.example.search.property.Property
import com.example.search.visitor.SearchCriteriaVisitor

class EqualCriterion<T>(property: Property<T>, values: Array<T>) : Criterion<T>(property, values) {
    override fun accept(visitor: SearchCriteriaVisitor) {
        visitor.visit(this)
    }
}
