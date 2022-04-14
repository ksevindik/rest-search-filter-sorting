package com.example.search.sort

import com.example.model.BaseEntity
import com.example.search.EntityTypeAware
import org.springframework.data.domain.Sort
import org.springframework.util.StringUtils
import java.util.Locale

open class SortParser(
    val aliasToPropertyPathMap: Map<String, String>,
    private val entityType: Class<out BaseEntity> = BaseEntity::class.java
) : EntityTypeAware {

    @Throws(IllegalArgumentException::class)
    open fun parse(sortParam: String): Sort {
        val elements = StringUtils.commaDelimitedListToStringArray(sortParam)
        var first = true
        var sort = Sort.unsorted()
        for (element in elements) {
            val tokens = element.split(":")
            val propertyPath = aliasToPropertyPathMap[tokens[0]]
            val direction =
                if (tokens.size == 1) Sort.Direction.ASC else Sort.Direction.valueOf(tokens[1].uppercase(Locale.US))
            val s = Sort.by(direction, propertyPath)
            if (first) {
                sort = s
                first = false
            } else {
                sort = sort.and(s)
            }
        }

        return sort
    }

    override fun getEntityType(): Class<out BaseEntity> {
        return entityType
    }
}
