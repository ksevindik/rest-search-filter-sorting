package com.example.search.visitor

import com.example.search.criterion.EqualCriterion
import com.example.search.criterion.GreaterThanCriterion
import com.example.search.criterion.InCriterion
import com.example.search.criterion.LessThanCriterion
import com.example.search.criterion.LikeCriterion
import com.example.search.criterion.RangeCriterion

interface SearchCriteriaVisitor {
    fun visit(criterion: LikeCriterion<*>)
    fun visit(criterion: EqualCriterion<*>)
    fun visit(criterion: LessThanCriterion<*>)
    fun visit(criterion: GreaterThanCriterion<*>)
    fun visit(criterion: RangeCriterion<*>)
    fun visit(criterion: InCriterion<*>)
}
