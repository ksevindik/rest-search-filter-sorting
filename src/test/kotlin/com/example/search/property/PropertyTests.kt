package com.example.search.property

import com.example.search.exception.PropertyParseException
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Date

class PropertyTests {
    @Test
    fun parse_ShouldReturnStringValue_ForStringProperty() {
        // Given
        val property = StringProperty("p1", "property1")
        val rawValue = "Foo"

        // When
        val value: String = property.parse(rawValue)

        // Then
        MatcherAssert.assertThat(value, Matchers.equalTo(rawValue))
    }

    @Test
    fun parse_ShouldReturnIntegerValue_ForIntegerProperty() {
        // Given
        val rawValue = "100"
        val property = IntegerProperty("p1", "property1")

        // When
        val value: Int = property.parse(rawValue)

        // Then
        MatcherAssert.assertThat(value, Matchers.equalTo(100))
    }

    @Test
    fun parse_ShouldThrowPropertyParseException_ForIntegerProperty_WhenInvalidRawValueGiven() {
        // Given
        val rawValue = "xxx"
        val property = IntegerProperty("p1", "property1")

        // When, Then
        try {
            property.parse(rawValue)
            Assertions.fail<String>("Should not reach here ")
        } catch (ex: PropertyParseException) {
        } catch (ex: Exception) {
            Assertions.fail<String>("Should not throw this exception " + ex::class.qualifiedName)
        }
    }

    @Test
    fun parse_ShouldReturnBigDecimalValue_ForBigDecimalProperty() {
        // Given
        val rawValue = "100.00"
        val property = BigDecimalProperty("p1", "property1")

        // When
        val value: BigDecimal = property.parse(rawValue)

        // Then
        MatcherAssert.assertThat(value, Matchers.equalTo(BigDecimal("100.00")))
    }

    @Test
    fun parse_ShouldThrowPropertyParseException_ForBigDecimalProperty_WhenInvalidRawValueGiven() {
        // Given
        val rawValue = "xxx"
        val property = BigDecimalProperty("p1", "property1")

        // When, Then
        try {
            property.parse(rawValue)
            Assertions.fail<String>("Should not reach here ")
        } catch (ex: PropertyParseException) {
        } catch (ex: Exception) {
            Assertions.fail<String>("Should not throw this exception " + ex::class.qualifiedName)
        }
    }

    @Test
    fun parse_ShouldReturnDateValue_ForDateProperty() {
        // Given
        val rawValue = "20200101"
        val dateFormat = "yyyyMMdd"
        val property: Property<Date> = DateProperty("p1", "property1", dateFormat)
        val simpleDateFormat = SimpleDateFormat(dateFormat)

        // When
        val value: Date = property.parse(rawValue)

        // Then
        MatcherAssert.assertThat(value, Matchers.equalTo(simpleDateFormat.parse(rawValue)))
    }

    @Test
    fun parse_ShouldThrowPropertyParseException_ForDateProperty_WhenInvalidRawValueGiven() {
        // Given
        val rawValue = "xxx"
        val dateFormat = "yyyyMMdd"
        val property: Property<Date> = DateProperty("p1", "property1", dateFormat)

        // When, Then
        try {
            property.parse(rawValue)
            Assertions.fail<String>("Should not reach here ")
        } catch (ex: PropertyParseException) {
        } catch (ex: Exception) {
            Assertions.fail<String>("Should not throw this exception " + ex::class.qualifiedName)
        }
    }

    @Test
    fun parse_ShouldReturnBooleanTrue_ForBooleanProperty() {
        // Given
        val rawValue = "true"
        val property = BooleanProperty("p1", "property1")

        // When
        val value: Boolean = property.parse(rawValue)

        // Then
        MatcherAssert.assertThat(value, Matchers.equalTo(true))
    }

    @Test
    fun parse_ShouldReturnBooleanTrue_ForBooleanProperty_WhenCaseInsensitiveRawValueGiven() {
        // Given
        val rawValue = "TrUe"
        val property = BooleanProperty("p1", "property1")

        // When
        val value: Boolean = property.parse(rawValue)

        // Then
        MatcherAssert.assertThat(value, Matchers.equalTo(true))
    }

    @Test
    fun parse_ShouldReturnBooleanFalse_ForBooleanProperty() {
        // Given
        val rawValue = "false"
        val property = BooleanProperty("p1", "property1")

        // When
        val value: Boolean = property.parse(rawValue)

        // Then
        MatcherAssert.assertThat(value, Matchers.equalTo(false))
    }

    @Test
    fun parse_ShouldReturnBooleanFalse_ForBooleanProperty_WhenCaseInsensitiveRawValueGiven() {
        // Given
        val rawValue = "FaLsE"
        val property = BooleanProperty("p1", "property1")

        // When
        val value: Boolean = property.parse(rawValue)

        // Then
        MatcherAssert.assertThat(value, Matchers.equalTo(false))
    }

    @Test
    fun parse_ShouldThrowPropertyParseException_ForBooleanProperty_WhenInvalidRawValueGiven() {
        // Given
        val rawValue = "xxx"
        val property = BooleanProperty("e", "enabled")

        // When, Then
        try {
            property.parse(rawValue)
            Assertions.fail<String>("Should not reach here ")
        } catch (ex: PropertyParseException) {
        } catch (ex: Exception) {
            Assertions.fail<String>("Should not throw this exception " + ex::class.qualifiedName)
        }
    }

    @Test
    fun parse_ShouldReturnEnumValue_ForEnumProperty() {
        // Given
        val rawValue = "V1"
        val enumValues: Array<SampleEnum> = SampleEnum.values()
        val property = EnumProperty("p1", "property1", enumValues)

        // When
        val value = property.parse(rawValue)

        // Then
        MatcherAssert.assertThat(value, Matchers.equalTo(SampleEnum.V1))
    }

    @Test
    fun parse_ShouldThrowPropertyParseException_ForEnumProperty_WhenInvalidRawValueGiven() {
        // Given
        val rawValue = "xxx"
        val enumValues: Array<SampleEnum> = SampleEnum.values()
        val property = EnumProperty("p1", "property1", enumValues)

        // When, Then
        try {
            property.parse(rawValue)
            Assertions.fail<String>("Should not reach here ")
        } catch (ex: PropertyParseException) {
        } catch (ex: Exception) {
            Assertions.fail<String>("Should not throw this exception " + ex::class.qualifiedName)
        }
    }
}

enum class SampleEnum {
    V1,V2
}
