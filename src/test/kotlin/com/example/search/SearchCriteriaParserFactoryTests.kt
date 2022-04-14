package com.example.search

import com.example.model.UserType
import com.example.search.property.BigDecimalProperty
import com.example.search.property.BooleanProperty
import com.example.search.property.DateProperty
import com.example.search.property.EnumProperty
import com.example.search.property.IntegerProperty
import com.example.search.property.LongProperty
import com.example.search.property.Property
import com.example.search.property.StringProperty
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test

class SearchCriteriaParserFactoryTests {

    @Test
    fun `it should return parser for user`() {
        // Given
        val searchCriteriaParserFactory = SearchCriteriaParserFactory()
        val expectedProperties = arrayOf(
            StringProperty("fn", "firstName", exactMatch = true),
            StringProperty("ln", "lastName"),
            DateProperty("bd", "birthDate", "yyyy-MM-dd"),
            IntegerProperty("rt", "rating"),
            BooleanProperty("act", "active"),
            BigDecimalProperty("ba","balance"),
            LongProperty("clk","clicks"),
            StringProperty("cc", "country.code"),
            EnumProperty("ut", "userType", UserType.values())
        ) as Array<Property<out Any>>

        // When
        val actualParser = searchCriteriaParserFactory.createParserForUser()
        val actualProperties = actualParser.properties

        // Then
        MatcherAssert.assertThat(actualProperties, Matchers.equalTo(expectedProperties))
    }
}
