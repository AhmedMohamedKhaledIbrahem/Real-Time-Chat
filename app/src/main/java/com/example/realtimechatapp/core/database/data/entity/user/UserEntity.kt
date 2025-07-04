package com.example.realtimechatapp.core.database.data.entity.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val isVerified: Boolean = false,
    val image: String? = "",
)
