package com.example.search

import com.example.model.User
import com.example.search.sort.SortParser
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class SortParserFactory {
    @Bean("sortParserForUser")
    fun createSortParserForUser(): SortParser {
        return SortParser(
            mapOf(
                Pair("fn", "firstName"),
                Pair("ln", "lastName"),
                Pair("bd", "birthDate"),
                Pair("rt", "rating"),
                Pair("act", "active"),
                Pair("bl","balance"),
                Pair("clk","clicks"),
                Pair("cc", "country.code"),
                Pair("ut", "userType")
            ),
            User::class.java
        )
    }
}
