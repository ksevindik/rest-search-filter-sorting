package com.example.search.sort

import com.example.search.SortParserFactory
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.springframework.data.domain.Sort

class SortParserTests {
    @Test
    fun parse_ShouldReturnSortWithAscDirection_WhenOnlycountryCodeGiven() {
        // Given
        val sortParam = "cc"
        val sortParser = SortParser(mapOf(Pair("cc", "country.code")))

        // When
        val sort: Sort = sortParser.parse(sortParam)

        // Then
        MatcherAssert.assertThat(sort, Matchers.equalTo(Sort.by(Sort.Direction.ASC, "country.code")))
    }

    @Test
    fun parse_ShouldReturnSortWithDescDirection_WhenCountryCodeWithDescDirectionGiven() {
        // Given
        val sortParam = "cc:desc"
        val sortParser = SortParser(mapOf(Pair("cc", "country.code")))

        // When
        val sort: Sort = sortParser.parse(sortParam)

        // Then
        MatcherAssert.assertThat(sort, Matchers.equalTo(Sort.by(Sort.Direction.DESC, "country.code")))
    }

    @Test
    fun parse_ShouldReturnSortWithAscDirection_WhenCountryCodeWithAscDirectionGiven() {
        // Given
        val sortParam = "cc:asc"
        val sortParser = SortParser(mapOf(Pair("cc", "country.code")))

        // When
        val sort: Sort = sortParser.parse(sortParam)

        // Then
        MatcherAssert.assertThat(sort, Matchers.equalTo(Sort.by(Sort.Direction.ASC, "country.code")))
    }

    @Test
    fun parse_ShouldHandleMultipleElementsInSortParam() {
        // Given
        val parser = SortParserFactory().createSortParserForUser()

        // When
        val sort = parser.parse("rt:asc,ln,fn:desc")

        // Then
        val s1 = Sort.by(Sort.Direction.ASC, "rating")
        val s2 = Sort.by(Sort.Direction.ASC, "lastName")
        val s3 = Sort.by(Sort.Direction.DESC, "firstName")
        val expectedSort = s1.and(s2).and(s3)

        MatcherAssert.assertThat(
            sort,
            Matchers.equalTo(
                expectedSort
            )
        )
    }

    @Test
    fun `it should return unsorted if empty sort parameter is given`() {
        // Given
        val parser = SortParserFactory().createSortParserForUser()

        // When
        val sort = parser.parse("")

        // Then
        val expectedSort = Sort.unsorted()

        MatcherAssert.assertThat(
            sort,
            Matchers.equalTo(
                expectedSort
            )
        )
    }
}
