package com.example.search

import com.example.model.BaseEntity
import com.example.search.exception.SearchParserBeanNotRegisteredException
import com.example.search.sort.SortParser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class SearchParserProvider {
    private val sortParserMap: MutableMap<Class<out BaseEntity>, SortParser> = mutableMapOf()
    private val searchCriteriaParserMap: MutableMap<Class<out BaseEntity>, SearchCriteriaParser> = mutableMapOf()

    @Autowired
    private lateinit var sortParsers: List<SortParser>

    @Autowired
    private lateinit var searchCriteriaParsers: List<SearchCriteriaParser>

    @PostConstruct
    fun init() {
        sortParsers.forEach { sortParserMap.put(it.getEntityType(), it) }
        searchCriteriaParsers.forEach { searchCriteriaParserMap.put(it.getEntityType(), it) }
    }

    fun getSortParserFor(entityType: Class<out BaseEntity>): SortParser {
        val sortParser = sortParserMap.get(entityType)
        if (sortParser == null) throw SearchParserBeanNotRegisteredException("No SortParser bean registered for this entity type $entityType")
        return sortParser
    }

    fun addSortParser(entityType: Class<out BaseEntity>, sortParser: SortParser) {
        sortParserMap.put(entityType, sortParser)
    }

    fun getSearchCriteriaParserFor(entityType: Class<out BaseEntity>): SearchCriteriaParser {
        val searchCriteriaParser = searchCriteriaParserMap.get(entityType)
        if (searchCriteriaParser == null) throw SearchParserBeanNotRegisteredException("No SearchCriteriaParser bean registered for this entity type $entityType")
        return searchCriteriaParser
    }

    fun addSearchCriteriaParser(entityType: Class<out BaseEntity>, searchCriteriaParser: SearchCriteriaParser) {
        searchCriteriaParserMap.put(entityType, searchCriteriaParser)
    }
}

interface EntityTypeAware {
    fun getEntityType(): Class<out BaseEntity>
}
