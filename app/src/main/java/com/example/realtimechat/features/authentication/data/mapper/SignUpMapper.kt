package com.example.realtimechat.features.authentication.data.mapper

import com.example.realtimechat.core.database.data.entity.user.UserEntity
import com.example.realtimechat.features.authentication.data.model.SignUpModel
import com.example.realtimechat.features.authentication.data.model.UserModel
import com.example.realtimechat.features.authentication.domain.entity.SignUpEntity

fun SignUpModel.toEntity(): SignUpEntity = SignUpEntity(
    name = name,
    email = email,
    phoneNumber = phoneNumber,
    password = password,
)

fun SignUpModel.toUserEntity(uid: String): UserEntity = UserEntity(
    id = uid,
    name = name,
    email = email,
    phoneNumber = phoneNumber,
    image = "",
)
fun SignUpEntity.toModel(): SignUpModel = SignUpModel(
    name = name,
    email = email,
    phoneNumber = phoneNumber,
    password = password,
)
fun SignUpModel.toUserModel(): UserModel = UserModel(
    name = name,
    email = email,
    phone = phoneNumber,
    imageUrl = "",
)

