package com.korsik.my_shared_lists

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform