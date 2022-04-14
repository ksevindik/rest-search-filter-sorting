package com.example.model

import java.math.BigDecimal
import java.util.Date
import javax.persistence.Entity
import javax.persistence.ManyToOne


@Entity
class User(
    var firstName:String,
    var lastName:String,
    var birthDate:Date,
    var rating:Int,
    var active:Boolean,
    var balance:BigDecimal,
    var clicks:Long,
    @ManyToOne var country:Country,
    var userType:UserType) : BaseEntity() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (birthDate != other.birthDate) return false
        if (rating != other.rating) return false
        if (active != other.active) return false
        if (balance != other.balance) return false
        if (clicks != other.clicks) return false
        if (country != other.country) return false
        if (userType != other.userType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + birthDate.hashCode()
        result = 31 * result + rating
        result = 31 * result + active.hashCode()
        result = 31 * result + balance.hashCode()
        result = 31 * result + clicks.hashCode()
        result = 31 * result + country.hashCode()
        result = 31 * result + userType.hashCode()
        return result
    }

    override fun toString(): String {
        return "User(firstName='$firstName', lastName='$lastName', birthDate=$birthDate, rating=$rating, active=$active, balance=$balance, clicks=$clicks, country=$country, userType=$userType)"
    }
}