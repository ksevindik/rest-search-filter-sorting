package com.example.search

import com.example.model.UserType
import com.example.search.criterion.EqualCriterion
import com.example.search.criterion.GreaterThanCriterion
import com.example.search.criterion.InCriterion
import com.example.search.criterion.LessThanCriterion
import com.example.search.criterion.LikeCriterion
import com.example.search.criterion.RangeCriterion
import com.example.search.criterion.SearchCriteria
import com.example.search.exception.SearchCriteriaParseException
import com.example.search.property.DateProperty
import com.example.search.property.EnumProperty
import com.example.search.property.IntegerProperty
import com.example.search.property.StringProperty
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SearchCriteriaParserTests {

    @Test
    fun searchCriteriaParser_ShouldNotAllowDuplicateAliases() {
        // Given
        val property1 = StringProperty("c", "code")
        val property2 = StringProperty("c", "monetaryAmount.currency.currencyCode")

        // When, Then
        try {
            SearchCriteriaParser(arrayOf(property1, property2))
            Assertions.fail<String>("Should not come here")
        } catch (e: IllegalArgumentException) {
        } catch (e: Exception) {
            Assertions.fail<String>("Should not have this exception ${e::class.qualifiedName}")
        }
    }

    @Test
    fun parse_ShouldReturnSearchCriteriaWithLikeCriterion() {
        // Given
        val filterString = "p1:Foo"
        val property = StringProperty("p1", "property1")
        val parser = SearchCriteriaParser(arrayOf(property))

        // When
        val searchCriteria: SearchCriteria = parser.parse(filterString)

        // Then
        val expectedSearchCriteria = SearchCriteria(
            arrayOf(
                LikeCriterion(property, arrayOf("Foo"))
            )
        )
        MatcherAssert.assertThat(searchCriteria, Matchers.equalTo(expectedSearchCriteria))
    }

    @Test
    fun parse_ShouldThrowParseException_WhenAliasNotExists() {
        // Given
        val filterString = "p11:Amazon"
        val property = StringProperty("p1", "property1")
        val parser = SearchCriteriaParser(arrayOf(property))

        // When, Then
        try {
            parser.parse(filterString)
            Assertions.fail<String>("Should not come here")
        } catch (ex: SearchCriteriaParseException) {
        } catch (ex: Exception) {
            Assertions.fail<String>("Should not catch :${ex::class.qualifiedName}")
        }
    }

    @Test
    fun parse_ShouldTrimSpacesAroundTokens() {
        // Given
        val filterString = " p1 : Foo Bar "
        val property = StringProperty("p1", "property1")
        val parser = SearchCriteriaParser(arrayOf(property))

        // When
        val searchCriteria: SearchCriteria = parser.parse(filterString)

        // Then
        val expectedSearchCriteria = SearchCriteria(
            arrayOf(
                LikeCriterion(property, arrayOf("Foo Bar"))
            )
        )
        MatcherAssert.assertThat(searchCriteria, Matchers.equalTo(expectedSearchCriteria))
    }

    @Test
    fun parse_ShouldThrowParseException_WhenAliasNotGiven() {
        // Given
        val filterString = ":Foo"
        val property = StringProperty("p1", "property1")
        val parser = SearchCriteriaParser(arrayOf(property))

        // When, Then
        try {
            parser.parse(filterString)
            Assertions.fail<String>("Should not come here")
        } catch (ex: SearchCriteriaParseException) {
        } catch (ex: Exception) {
            Assertions.fail<String>("Should not catch :${ex::class.qualifiedName}")
        }
    }

    @Test
    fun parse_ShouldThrowParseException_WhenRawValueNotGiven() {
        // Given
        val filterString = "p1:"
        val property = StringProperty("p1", "property1")
        val parser = SearchCriteriaParser(arrayOf(property))

        // When, Then
        try {
            parser.parse(filterString)
            Assertions.fail<String>("Should not come here")
        } catch (ex: SearchCriteriaParseException) {
        } catch (ex: Exception) {
            Assertions.fail<String>("Should not catch :${ex::class.qualifiedName}")
        }
    }

    @Test
    fun parse_ShouldReturnSearchCriteriaWithEqualCriterion() {
        // Given
        val filterString = "p1:100"
        val property = IntegerProperty("p1", "property1")

        // When
        val parser = SearchCriteriaParser(arrayOf(property))
        val searchCriteria = parser.parse(filterString)

        // Then
        val expectedSearchCriteria = SearchCriteria(arrayOf(EqualCriterion(property, arrayOf(100))))
        MatcherAssert.assertThat(searchCriteria, Matchers.equalTo(expectedSearchCriteria))
    }

    @Test
    fun parse_ShouldReturnSearchCriteriaWithLessThanCriterion() {
        // Given
        val filterString = "p1<100"
        val property = IntegerProperty("p1", "property1")

        // When
        val parser = SearchCriteriaParser(arrayOf(property))
        val searchCriteria = parser.parse(filterString)

        // Then
        val expectedSearchCriteria = SearchCriteria(arrayOf(LessThanCriterion(property, arrayOf(100))))
        MatcherAssert.assertThat(searchCriteria, Matchers.equalTo(expectedSearchCriteria))
    }

    @Test
    fun parse_ShouldReturnSearchCriteriaWithGreaterThanCriterion() {
        // Given
        val filterString = "p1>100"
        val property = IntegerProperty("p1", "property1")

        // When
        val parser = SearchCriteriaParser(arrayOf(property))
        val searchCriteria = parser.parse(filterString)

        // Then
        val expectedSearchCriteria = SearchCriteria(arrayOf(GreaterThanCriterion(property, arrayOf(100))))
        MatcherAssert.assertThat(searchCriteria, Matchers.equalTo(expectedSearchCriteria))
    }

    @Test
    fun parse_ShouldThrowParseException_WhenInvalidRawValueGiven() {
        // Given
        val filterString = "p1:xxx"
        val property = IntegerProperty("p1", "property1")
        val parser = SearchCriteriaParser(arrayOf(property))

        // When, Then
        try {
            parser.parse(filterString)
            Assertions.fail<String>("Should not come here")
        } catch (ex: SearchCriteriaParseException) {
        } catch (ex: Exception) {
            Assertions.fail<String>("Should not catch :${ex::class.qualifiedName}")
        }
    }

    @Test
    fun parse_ShouldReturnSearchCriteriaWithRangeCriterion() {
        // Given
        val filterString = "p1[2020-01-01..2020-01-02]"
        val property = DateProperty("p1", "property1", "yyy-MM-dd")

        // When
        val parser = SearchCriteriaParser(arrayOf(property))
        val searchCriteria = parser.parse(filterString)

        // Then
        val expectedSearchCriteria = SearchCriteria(
            arrayOf(
                RangeCriterion(
                    property,
                    arrayOf(property.parse("2020-01-01"), property.parse("2020-01-02"))
                )
            )
        )
        MatcherAssert.assertThat(searchCriteria, Matchers.equalTo(expectedSearchCriteria))
    }

    @Test
    fun parse_ShouldTrimSpacesFromRawValuesWithRangeCriterion() {
        // Given
        val filterString = "p1[ 2020-01-01 .. 2020-01-02 ]"
        val property = DateProperty("p1", "property1", "yyyy-MM-dd")

        // When
        val parser = SearchCriteriaParser(arrayOf(property))
        val searchCriteria = parser.parse(filterString)

        // Then
        val expectedSearchCriteria = SearchCriteria(
            arrayOf(
                RangeCriterion(
                    property,
                    arrayOf(property.parse("2020-01-01"), property.parse("2020-01-02"))
                )
            )
        )
        MatcherAssert.assertThat(searchCriteria, Matchers.equalTo(expectedSearchCriteria))
    }

    @Test
    fun parse_ShouldThrowParseException_WhenSecondRawValueNotGivenWithRangeCriterion() {
        // Given
        val filterString = "p1[2020-01-01..]"
        val property = DateProperty("p1", "property1", "yyyy-MM-dd")

        // When, Then
        val parser = SearchCriteriaParser(arrayOf(property))
        try {
            parser.parse(filterString)
            Assertions.fail<String>("Should not come here")
        } catch (ex: SearchCriteriaParseException) {
        } catch (ex: Exception) {
            Assertions.fail<String>("Should not catch :${ex::class.qualifiedName}")
        }
    }

    @Test
    fun parse_ShouldThrowParseException_WhenFirstRawValueNotGivenWithRangeCriterion() {
        // Given
        val filterString = "p1[..2020-01-01]"
        val property = DateProperty("p1", "property1", "yyyy-MM-dd")

        // When, Then
        val parser = SearchCriteriaParser(arrayOf(property))
        try {
            parser.parse(filterString)
            Assertions.fail<String>("Should not come here")
        } catch (ex: SearchCriteriaParseException) {
        } catch (ex: Exception) {
            Assertions.fail<String>("Should not catch :${ex::class.qualifiedName}")
        }
    }

    @Test
    fun parse_ShouldThrowParseException_WhenRawValuesMoreThanTwoGivenWithRangeCriterion() {
        // Given
        val filterString = "p1[2020-01-01..2020-01-01..2020-01-01]"
        val property = DateProperty("p1", "property1", "yyyy-MM-dd")

        // When, Then
        val parser = SearchCriteriaParser(arrayOf(property))
        try {
            parser.parse(filterString)
            Assertions.fail<String>("Should not come here")
        } catch (ex: SearchCriteriaParseException) {
        } catch (ex: Exception) {
            Assertions.fail<String>("Should not catch :${ex::class.qualifiedName}")
        }
    }

    @Test
    fun parse_ShouldThrowParseException_WhenNoRawValuesGivenWithRangeCriterion() {
        // Given
        val filterString = "p1[]"
        val property = DateProperty("p1", "property1", "yyyy-MM-dd")

        // When, Then
        val parser = SearchCriteriaParser(arrayOf(property))
        try {
            parser.parse(filterString)
            Assertions.fail<String>("Should not come here")
        } catch (ex: SearchCriteriaParseException) {
        } catch (ex: Exception) {
            Assertions.fail<String>("Should not catch :${ex::class.qualifiedName}")
        }
    }

    @Test
    fun parse_ShouldThrowParseException_WhenEmptyFilterStringGiven() {
        // Given
        val filterString = ""
        val parser = SearchCriteriaParser(arrayOf())

        // When, Then
        try {
            parser.parse(filterString)
            Assertions.fail<String>("Should not come here")
        } catch (ex: SearchCriteriaParseException) {
        } catch (ex: Exception) {
            Assertions.fail<String>("Should not catch :${ex::class.qualifiedName}")
        }
    }

    @Test
    fun parse_ShouldThrowParseException_WhenInvalidCriterionTokenGivenInFilterString() {
        // Given
        val filterString = "xyz"
        val parser = SearchCriteriaParser(arrayOf())

        // When, Then
        try {
            parser.parse(filterString)
            Assertions.fail<String>("Should not come here")
        } catch (ex: SearchCriteriaParseException) {
        } catch (ex: Exception) {
            Assertions.fail<String>("Should not catch :${ex::class.qualifiedName}")
        }
    }

    fun parse_ShouldReturnSearchCriteriaWithInCriterion() {
        // given
        val filterString = "p1[V1|V2]"
        val property = EnumProperty("p1", "property1", SampleEnum.values())
        val parser = SearchCriteriaParser(arrayOf(property))
        // when
        val searchCriteria = parser.parse(filterString)
        // then
        val expectedSearchCriteria = SearchCriteria(
            arrayOf(
                InCriterion(
                    property,
                    arrayOf(SampleEnum.V1,SampleEnum.V2)
                )
            )
        )
        MatcherAssert.assertThat(searchCriteria, Matchers.equalTo(expectedSearchCriteria))
    }

    @Test
    fun parse_ShouldTrimSpacesFromRawValuesWithInCriterion() {
        // Given
        val filterString = "p1[V1|V2]"
        val property = EnumProperty("p1", "property1", SampleEnum.values())
        val parser = SearchCriteriaParser(arrayOf(property))

        // When
        val searchCriteria = parser.parse(filterString)

        // Then
        val expectedSearchCriteria = SearchCriteria(
            arrayOf(
                InCriterion(
                    property,
                    arrayOf(SampleEnum.V1,SampleEnum.V2)
                )
            )
        )
        MatcherAssert.assertThat(searchCriteria, Matchers.equalTo(expectedSearchCriteria))
    }

    @Test
    fun parse_ShouldThrowParseException_WhenNoRawValuesGivenWithInCriterion() {
        // Given
        val filterString = "p1[ ]"
        val property = EnumProperty("p1", "property1", UserType.values())
        val parser = SearchCriteriaParser(arrayOf(property))

        // When, Then
        try {
            parser.parse(filterString)
            Assertions.fail<String>("Should not come here")
        } catch (ex: SearchCriteriaParseException) {
        } catch (ex: Exception) {
            Assertions.fail<String>("Should not catch :${ex::class.qualifiedName}")
        }
    }

    @Test
    fun parse_ShouldThrowParseException_WhenAnyRawValueAsEmptyStringGivenWithInCriterion() {
        // Given
        val filterString = "p1[V1||V2]"
        val property = EnumProperty("p1", "property1", SampleEnum.values())
        val parser = SearchCriteriaParser(arrayOf(property))

        // When, Then
        try {
            parser.parse(filterString)
            Assertions.fail<String>("Should not come here")
        } catch (ex: SearchCriteriaParseException) {
        } catch (ex: Exception) {
            Assertions.fail<String>("Should not catch :${ex::class.qualifiedName}")
        }
    }
}

enum class SampleEnum {
    V1,V2
}