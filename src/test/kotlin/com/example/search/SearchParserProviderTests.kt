package com.example.search

import com.example.model.User
import com.example.search.exception.SearchParserBeanNotRegisteredException
import com.example.search.sort.SortParser
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class SearchParserProviderTests {
    @Test
    fun `getSortParserFor should return SortParser bean instance corresponding to the given entity type`() {
        // given
        val provider = SearchParserProvider()
        val sortParser1: SortParser = Mockito.mock(SortParser::class.java)
        provider.addSortParser(User::class.java, sortParser1)
        // when
        val sortParser: SortParser = provider.getSortParserFor(User::class.java)
        // then
        MatcherAssert.assertThat(sortParser, Matchers.sameInstance(sortParser1))
    }

    @Test
    fun `getSortParser should throw exception when SortParser bean instance not found for the given entity type`() {
        // given
        val provider = SearchParserProvider()
        // when, then
        try {
            provider.getSortParserFor(User::class.java)
            Assertions.fail<String>("should not come here!")
        } catch (ex: SearchParserBeanNotRegisteredException) {
        } catch (ex: Exception) {
            Assertions.fail<String>("should not have exception of this type ${ex.javaClass}")
        }
    }

    @Test
    fun `getSearchCriteriaParserFor should return SearchCriteriaParser bean instance corresponding to the given entity type`() {
        // given
        val provider = SearchParserProvider()
        val searchCriteriaParser1: SearchCriteriaParser = Mockito.mock(SearchCriteriaParser::class.java)
        provider.addSearchCriteriaParser(User::class.java, searchCriteriaParser1)
        // when
        val searchCriteriaParser = provider.getSearchCriteriaParserFor(User::class.java)
        // then
        MatcherAssert.assertThat(searchCriteriaParser, Matchers.sameInstance(searchCriteriaParser1))
    }

    @Test
    fun `getSearchCriteriaParserFor should throw exception when SearchCriteriaParser bean instance not found for the given entity type`() {
        // given
        val provider = SearchParserProvider()
        // when, then
        try {
            provider.getSearchCriteriaParserFor(User::class.java)
            Assertions.fail<String>("should not come here!")
        } catch (ex: SearchParserBeanNotRegisteredException) {
        } catch (ex: Exception) {
            Assertions.fail<String>("should not have exception of this type ${ex.javaClass}")
        }
    }
}
