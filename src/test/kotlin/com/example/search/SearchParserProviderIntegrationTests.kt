package com.example.search

import com.example.model.User
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SearchParserProviderIntegrationTests @Autowired(required = false) constructor(
    private val searchParserProvider: SearchParserProvider
){

    @Test
    fun `searchParserProvider should be initialized correctly after application context bootstrap`() {
        // given
        // when
        val sortParserForUser = searchParserProvider.getSortParserFor(User::class.java)

        val searchCriteriaParserForUser =
            searchParserProvider.getSearchCriteriaParserFor(User::class.java)
        // then
        MatcherAssert.assertThat(sortParserForUser, Matchers.notNullValue())

        MatcherAssert.assertThat(searchCriteriaParserForUser, Matchers.notNullValue())
    }
}
