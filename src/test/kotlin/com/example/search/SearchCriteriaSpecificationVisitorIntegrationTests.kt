package com.example.search

import com.example.model.Country
import com.example.model.User
import com.example.model.UserType
import com.example.repository.CountryRepository
import com.example.repository.UserRepository
import com.example.search.property.DateProperty
import com.example.search.property.EnumProperty
import com.example.search.property.IntegerProperty
import com.example.search.property.StringProperty
import com.example.search.visitor.SearchCriteriaSpecificationVisitor
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.jpa.domain.Specification
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Date

@SpringBootTest
@Transactional
class SearchCriteriaSpecificationVisitorIntegrationTests @Autowired(required = false) constructor(
    private val userRepository: UserRepository,
    private val countryRepository: CountryRepository
) {

    private lateinit var user1: User
    private lateinit var user2: User
    private lateinit var user3: User

    private lateinit var country1: Country
    private lateinit var country2: Country

    @BeforeEach
    fun setUp() {

        country1 = Country("Türkiye","TR")
        country2 = Country("United States","US")

        countryRepository.saveAll(listOf(country1,country2))

        val df = SimpleDateFormat("yyyy-MM-dd")

        user1 = User("John","Doe",df.parse("2022-01-01"),1,true, BigDecimal.ONE,2,country1,UserType.NORMAL)
        user2 = User("Joe","Doe",df.parse("2022-02-01"),2,false, BigDecimal.TEN,5,country2,UserType.PREMIUM)
        user3 = User("Johnny","Does",df.parse("2021-01-01"),5,true, BigDecimal.ZERO,5,country2,UserType.PREMIUM)

        userRepository.saveAll(listOf(user1,user2,user3))
    }

    @Test
    fun shouldWorkWithLikeCriterion() {
        // Given
        val filterString = "fn:John"
        val parser = SearchCriteriaParser(arrayOf(StringProperty("fn", "firstName")))
        val searchCriteria = parser.parse(filterString)

        // When
        val visitor = SearchCriteriaSpecificationVisitor<User>()
        searchCriteria.apply(visitor)
        val specification: Specification<User> = visitor.specification!!
        val users = userRepository.findAll(specification)

        // Then
        MatcherAssert.assertThat(
            users,
            Matchers.containsInAnyOrder(user1, user3)
        )
    }

    @Test
    fun shouldWorkWithEqualCriterion() {
        // Given
        val filterString = "ln:Doe"
        val parser = SearchCriteriaParser(arrayOf(StringProperty("ln", "lastName", true)))
        val searchCriteria = parser.parse(filterString)

        // When
        val visitor = SearchCriteriaSpecificationVisitor<User>()
        searchCriteria.apply(visitor)
        val specification: Specification<User> = visitor.specification!!
        val users = userRepository.findAll(specification)

        // Then
        MatcherAssert.assertThat(users, Matchers.containsInAnyOrder(user1,user2))
    }

    @Test
    fun findAllWithSpecificationContainingInCriterionShouldWork() {
        // Given
        val filterString = "ut[PREMIUM]"
        val parser = SearchCriteriaParser(arrayOf(EnumProperty<UserType>("ut", "userType",UserType.values())))
        val searchCriteria = parser.parse(filterString)

        // When
        val visitor = SearchCriteriaSpecificationVisitor<User>()
        searchCriteria.apply(visitor)
        val specification: Specification<User> = visitor.specification!!
        val users = userRepository.findAll(specification)

        // Then
        MatcherAssert.assertThat(
            users,
            Matchers.containsInAnyOrder(user2, user3)
        )
    }

    @Test
    fun shouldWorkWithLessThanCriterion() {
        // Given
        val filterString = "rt<4"
        val parser = SearchCriteriaParser(arrayOf(IntegerProperty("rt", "rating")))
        val searchCriteria = parser.parse(filterString)

        // When
        val visitor = SearchCriteriaSpecificationVisitor<User>()
        searchCriteria.apply(visitor)
        val specification: Specification<User> = visitor.specification!!
        val users = userRepository.findAll(specification)

        // Then
        MatcherAssert.assertThat(
            users,
            Matchers.containsInAnyOrder(user1, user2)
        )
    }

    @Test
    fun filterStringWithLessThanCriterionOnDatePropertyShouldWork() {
        // Given
        val filterString = "bd<2021-01-02"
        val parser = SearchCriteriaParser(
            arrayOf(
                DateProperty(
                    "bd",
                    "birthDate",
                    "yyyy-MM-dd"
                )
            )
        )
        val searchCriteria = parser.parse(filterString)

        // When
        val visitor = SearchCriteriaSpecificationVisitor<User>()
        searchCriteria.apply(visitor)
        val specification: Specification<User> = visitor.specification!!
        val users = userRepository.findAll(specification)

        // Then
        MatcherAssert.assertThat(
            users,
            Matchers.containsInAnyOrder(user3)
        )
    }


    @Test
    fun findAllWithSpecificationContainingGreaterThanCriterionShouldWork() {
        // Given
        val filterString = "rt>3"
        val parser = SearchCriteriaParser(arrayOf(IntegerProperty("rt", "rating")))
        val searchCriteria = parser.parse(filterString)

        // When
        val visitor = SearchCriteriaSpecificationVisitor<User>()
        searchCriteria.apply(visitor)
        val specification: Specification<User> = visitor.specification!!
        val users = userRepository.findAll(specification)

        // Then
        MatcherAssert.assertThat(
            users,
            Matchers.containsInAnyOrder(user3)
        )
    }


    @Test
    fun findAllWithSpecificationContainingRangeCriterionShouldWork() {
        // Given
        val filterString = "bd[2020-01-01..2021-01-02]"
        val parser = SearchCriteriaParser(
            arrayOf(
                DateProperty(
                    "bd",
                    "birthDate",
                    "yyyy-MM-dd"
                )
            )
        )
        val searchCriteria = parser.parse(filterString)

        // When
        val visitor = SearchCriteriaSpecificationVisitor<User>()
        searchCriteria.apply(visitor)
        val specification: Specification<User> = visitor.specification!!
        val users = userRepository.findAll(specification)

        // Then
        MatcherAssert.assertThat(
            users,
            Matchers.containsInAnyOrder(user3)
        )
    }


    @Test
    fun findAllWithSpecificationContainingRangeCriterionOnIntegerPropertyShouldWork() {
        // Given
        val filterString = "rt[1..3]"
        val parser = SearchCriteriaParser(arrayOf(IntegerProperty("rt", "rating")))
        val searchCriteria = parser.parse(filterString)

        // When
        val visitor = SearchCriteriaSpecificationVisitor<User>()
        searchCriteria.apply(visitor)
        val specification: Specification<User> = visitor.specification!!
        val users = userRepository.findAll(specification)

        // Then
        MatcherAssert.assertThat(
            users,
            Matchers.containsInAnyOrder(user1, user2)
        )
    }
}
