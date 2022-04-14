package com.example.search.criterion

import com.example.search.visitor.SearchCriteriaVisitor

open class SearchCriteria(private var criterionElements: Array<Criterion<*>>) {

    fun add(criterion: Criterion<*>) {
        criterionElements = criterionElements.plus(criterion)
    }

    open fun apply(visitor: SearchCriteriaVisitor) {
        criterionElements.forEach { it.accept(visitor) }
    }

    fun contains(criterion: Criterion<*>): Boolean {
        return criterionElements.contains(criterion)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SearchCriteria

        if (!criterionElements.contentEquals(other.criterionElements)) return false

        return true
    }

    override fun hashCode(): Int {
        return criterionElements.contentHashCode()
    }

    override fun toString(): String {
        return "SearchCriteria(criterionElements=${criterionElements.contentToString()})"
    }
}
