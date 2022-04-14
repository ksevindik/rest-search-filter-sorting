package com.example.search.visitor

import org.springframework.stereotype.Component

@Component
class SearchCriteriaSpecificationVisitorFactory {
    fun <T> createVisitor(): SearchCriteriaSpecificationVisitor<T> {
        return SearchCriteriaSpecificationVisitor()
    }
}
