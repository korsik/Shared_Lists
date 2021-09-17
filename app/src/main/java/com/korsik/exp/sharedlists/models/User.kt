package com.korsik.exp.sharedlists.models

data class User(
    val uid: String? = null,
    val id: String? = null,
    val username: String? = null,
    val email: String? = null,
    var checkState: Boolean = false
)

data class UserDto(
    val id: String? = null,
    val username: String? = null,
    val email: String? = null
)