package com.styl.materialmessenger.entities

data class UnreadMessageEntity (
    var message: String,
    val userEntity: UserEntity
)