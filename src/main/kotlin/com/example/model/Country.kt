package com.example.model

import javax.persistence.Entity

@Entity
class Country(var name:String, var code:String) : BaseEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Country

        if (name != other.name) return false
        if (code != other.code) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + code.hashCode()
        return result
    }

    override fun toString(): String {
        return "Country(name='$name', code='$code')"
    }
}