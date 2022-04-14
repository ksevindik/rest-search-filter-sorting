package com.example.search.visitor

import com.example.search.criterion.EqualCriterion
import com.example.search.criterion.GreaterThanCriterion
import com.example.search.criterion.InCriterion
import com.example.search.criterion.LessThanCriterion
import com.example.search.criterion.LikeCriterion
import com.example.search.criterion.RangeCriterion
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import java.util.Date
import javax.persistence.criteria.Expression
import javax.persistence.criteria.Path
import javax.persistence.criteria.Root

@Component
class SearchCriteriaSpecificationVisitor<T> : SearchCriteriaVisitor {

    var specification: Specification<T>? = null

    private fun <X> resolvePathExpression(root: Root<T>, propertyPath: String): Expression<X> {
        val tokens = StringUtils.delimitedListToStringArray(propertyPath, ".")
        var path: Path<Any>? = null
        for (i in tokens.indices) {
            path = if (i == 0)
                root.get<Any>(tokens[i])
            else
                path!!.get<Any>(tokens[i])
        }
        @Suppress("UNCHECKED_CAST")
        return path!! as Expression<X>
    }

    private fun addSpecification(spec: Specification<T>) {
        specification = if (specification == null) Specification.where(spec) else specification!!.and(spec)
    }

    override fun visit(criterion: LikeCriterion<*>) {
        val spec = Specification<T> { root, _, criteriaBuilder ->
            val propertyPath = criterion.property.propertyPath
            val pathExpr: Expression<String> = resolvePathExpression(root, propertyPath)
            val valueExpr = criteriaBuilder.literal("%" + criterion.values[0].toString() + "%")
            val predicate = criteriaBuilder.like(pathExpr, valueExpr)
            predicate
        }
        addSpecification(spec)
    }

    override fun visit(criterion: EqualCriterion<*>) {
        val spec = Specification<T> { root, _, criteriaBuilder ->
            val propertyPath = criterion.property.propertyPath
            val pathExpr: Expression<Any> = resolvePathExpression(root, propertyPath)
            val valueExpr = criteriaBuilder.literal(criterion.values[0])
            criteriaBuilder.equal(pathExpr, valueExpr)
        }
        addSpecification(spec)
    }

    override fun visit(criterion: LessThanCriterion<*>) {
        val spec = Specification<T> { root, _, criteriaBuilder ->
            val propertyPath = criterion.property.propertyPath
            val pathExpr: Expression<Number> = resolvePathExpression(root, propertyPath)
            @Suppress("UNCHECKED_CAST")
            val valueExpr: Expression<Number> = criteriaBuilder.literal(criterion.values[0]) as Expression<Number>
            criteriaBuilder.le(pathExpr, valueExpr)
        }
        addSpecification(spec)
    }

    override fun visit(criterion: GreaterThanCriterion<*>) {
        val spec = Specification<T> { root, _, criteriaBuilder ->
            val propertyPath = criterion.property.propertyPath
            val pathExpr: Expression<Number> = resolvePathExpression(root, propertyPath)
            @Suppress("UNCHECKED_CAST")
            val valueExpr: Expression<Number> = criteriaBuilder.literal(criterion.values[0]) as Expression<Number>
            criteriaBuilder.ge(pathExpr, valueExpr)
        }
        addSpecification(spec)
    }

    override fun visit(criterion: RangeCriterion<*>) {
        val spec = Specification<T> { root, _, criteriaBuilder ->
            val propertyPath = criterion.property.propertyPath
            val pathExpr: Expression<Date> = resolvePathExpression(root, propertyPath)
            @Suppress("UNCHECKED_CAST")
            val value1Expr = criteriaBuilder.literal(criterion.values[0]) as Expression<Date>
            @Suppress("UNCHECKED_CAST")
            val value2Expr = criteriaBuilder.literal(criterion.values[1]) as Expression<Date>
            criteriaBuilder.between(pathExpr, value1Expr, value2Expr)
        }
        addSpecification(spec)
    }

    override fun visit(criterion: InCriterion<*>) {
        val spec = Specification<T> { root, _, criteriaBuilder ->
            val propertyPath = criterion.property.propertyPath
            val pathExpr: Expression<Any> = resolvePathExpression(root, propertyPath)
            var inExpr = criteriaBuilder.`in`(pathExpr)

            for (value in criterion.values) {
                inExpr = inExpr.value(value)
            }
            inExpr
        }
        addSpecification(spec)
    }
}
