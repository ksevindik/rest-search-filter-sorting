package com.example.search

import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test

class SortParserFactoryTests {
    @Test
    fun `it should create parser for user`() {
        // Given
        val map = mapOf(
            Pair("fn", "firstName"),
            Pair("ln", "lastName"),
            Pair("bd", "birthDate"),
            Pair("rt", "rating"),
            Pair("act", "active"),
            Pair("bl","balance"),
            Pair("clk","clicks"),
            Pair("cc", "country.code"),
            Pair("ut", "userType")
        )
        val sortParser = SortParserFactory().createSortParserForUser()

        // When, Then
        MatcherAssert.assertThat(sortParser.aliasToPropertyPathMap, Matchers.equalTo(map))
    }
}
