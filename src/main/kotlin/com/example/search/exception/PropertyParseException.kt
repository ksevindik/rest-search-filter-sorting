package com.example.search.exception

class PropertyParseException(override val message: String, override val cause: Throwable? = null) :
    RuntimeException(message, cause)
