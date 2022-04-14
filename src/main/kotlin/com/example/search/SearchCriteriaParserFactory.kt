package com.example.search

import com.example.model.User
import com.example.model.UserType
import com.example.search.property.BigDecimalProperty
import com.example.search.property.BooleanProperty
import com.example.search.property.DateProperty
import com.example.search.property.EnumProperty
import com.example.search.property.IntegerProperty
import com.example.search.property.LongProperty
import com.example.search.property.Property
import com.example.search.property.StringProperty
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class SearchCriteriaParserFactory () {

    @Bean("searchCriteriaParserForUser")
    fun createParserForUser(): SearchCriteriaParser {
        val properties = arrayOf(
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
        return SearchCriteriaParser(properties, User::class.java)
    }
}
