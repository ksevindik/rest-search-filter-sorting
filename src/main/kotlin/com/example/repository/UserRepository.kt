package com.example.repository

import com.example.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface UserRepository : JpaRepository<User,Long>, JpaSpecificationExecutor<User> {
}