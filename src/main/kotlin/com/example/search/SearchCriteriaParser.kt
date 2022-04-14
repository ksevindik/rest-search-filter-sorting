package com.example.search

import com.example.model.BaseEntity
import com.example.search.criterion.Criterion
import com.example.search.criterion.EqualCriterion
import com.example.search.criterion.GreaterThanCriterion
import com.example.search.criterion.InCriterion
import com.example.search.criterion.LessThanCriterion
import com.example.search.criterion.LikeCriterion
import com.example.search.criterion.RangeCriterion
import com.example.search.criterion.SearchCriteria
import com.example.search.exception.PropertyParseException
import com.example.search.exception.SearchCriteriaParseException
import com.example.search.property.Property
import com.example.search.property.StringProperty
import org.springframework.util.ObjectUtils.isEmpty
import org.springframework.util.StringUtils

open class SearchCriteriaParser(
    val properties: Array<Property<out Any>>,
    private val entityType: Class<out BaseEntity> = BaseEntity::class.java
) : EntityTypeAware {
    private val propertyMap: MutableMap<String, Property<out Any>> = mutableMapOf()

    init {
        properties.forEach {
            val v = propertyMap.put(it.alias, it)
            if (v != null) throw IllegalArgumentException("Given alias already exists in $it")
        }
    }

    override fun getEntityType(): Class<out BaseEntity> {
        return entityType
    }

    @Suppress("UNCHECKED_CAST")
    open fun getProperty(alias: String): Property<Any>? {
        return propertyMap.get(alias) as Property<Any>
    }

    @Throws(SearchCriteriaParseException::class)
    open fun parse(filterString: String): SearchCriteria {
        if (isEmpty(filterString)) throw SearchCriteriaParseException(
            "Empty filter string given"
        )
        val criterionList = mutableListOf<Criterion<*>>()
        val criterionTokens = StringUtils.commaDelimitedListToStringArray(filterString)
        try {
            for (criterionToken in criterionTokens) {
                val criterion = obtainCriterion(criterionToken)
                criterionList.add(criterion)
            }
            return SearchCriteria(criterionList.toTypedArray())
        } catch (ex: PropertyParseException) {
            throw SearchCriteriaParseException(ex.message)
        }
    }

    private fun obtainCriterion(filterString: String): Criterion<*> {
        if (filterString.contains(':')) {
            val pair = obtainPropertyRawValuePair(filterString, ':')
            val property = pair.first
            val rawValue = pair.second
            if (property is StringProperty && !property.exactMatch) {
                val value: String = property.parse(rawValue)
                return LikeCriterion(property, arrayOf(value))
            } else {
                val value = property.parse(rawValue)
                @Suppress("UNCHECKED_CAST")
                return EqualCriterion(property as Property<Any>, arrayOf(value))
            }
        } else if (filterString.contains('<')) {
            val pair = obtainPropertyRawValuePair(filterString, '<')
            val property = pair.first
            val rawValue = pair.second
            val value = property.parse(rawValue)
            @Suppress("UNCHECKED_CAST")
            return LessThanCriterion(property as Property<Any>, arrayOf(value))
        } else if (filterString.contains('>')) {
            val pair = obtainPropertyRawValuePair(filterString, '>')
            val property = pair.first
            val rawValue = pair.second
            val value = property.parse(rawValue)
            @Suppress("UNCHECKED_CAST")
            return GreaterThanCriterion(property as Property<Any>, arrayOf(value))
        } else if (filterString.contains('[')) {
            val pair = obtainPropertyRawValuePair(filterString, '[')
            val property = pair.first
            var rawValue = pair.second
            val index = rawValue.indexOf(']')
            rawValue = rawValue.substring(0, index)
            if (rawValue.contains("..")) {
                val tokens = rawValue.split("..")
                if (tokens.size != 2)
                    throw SearchCriteriaParseException("Invalid range values given $rawValue for property ${property.alias}")
                val rawValue1 = tokens[0].trim()
                val rawValue2 = tokens[1].trim()
                if (isEmpty(rawValue1))
                    throw SearchCriteriaParseException("First range value not given for property ${property.alias}")
                if (isEmpty(rawValue2))
                    throw SearchCriteriaParseException("Second range value not given for property ${property.alias}")
                val value1 = property.parse(rawValue1)
                val value2 = property.parse(rawValue2)
                @Suppress("UNCHECKED_CAST")
                return RangeCriterion(property as Property<Any>, arrayOf(value1, value2))
            } else {
                val rawValues = StringUtils.delimitedListToStringArray(rawValue, "|")
                if (rawValues.isEmpty()) throw SearchCriteriaParseException(
                    "Invalid values given for in criterion $rawValue for property ${property.alias}"
                )
                for (i in rawValues.indices) {
                    rawValues[i] = rawValues[i].trim()
                    if (isEmpty(rawValues[i]))
                        throw SearchCriteriaParseException(
                            "Empty in value given for in criterion with index $i for property ${property.alias}"
                        )
                }
                val values = mutableListOf<Any>()
                for (rawVal in rawValues) {
                    values.add(property.parse(rawVal))
                }
                @Suppress("UNCHECKED_CAST")
                return InCriterion(property as Property<Any>, values.toTypedArray())
            }
        } else {
            throw SearchCriteriaParseException("Invalid criterion element given $filterString")
        }
    }

    private fun obtainPropertyRawValuePair(filterString: String, operator: Char): Pair<Property<out Any>, String> {
        val tokens = filterString.split(operator)
        val alias = tokens[0].trim()
        if (isEmpty(alias)) throw SearchCriteriaParseException(
            "Alias not given for $filterString"
        )

        val rawValue = tokens[1].trim()
        if (isEmpty(rawValue)) throw SearchCriteriaParseException(
            "Raw value not given for property $alias"
        )
        val property =
            propertyMap[alias] ?: throw SearchCriteriaParseException(
                "Invalid alias given $alias in $filterString"
            )

        return Pair(property, rawValue)
    }
}
