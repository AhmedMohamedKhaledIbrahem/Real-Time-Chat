package com.example.realtimechatapp.features.authentication.data.mapper

import com.example.realtimechatapp.core.database.data.entity.user.UserEntity
import com.example.realtimechatapp.features.authentication.data.model.SignUpModel
import com.example.realtimechatapp.features.authentication.data.model.UserModel
import com.example.realtimechatapp.features.authentication.domain.entity.SignUpEntity

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
fun SignUpModel.toUserProfileModel(): UserModel = UserModel(
    name = name,
    email = email,
    phone = phoneNumber,
    imageUrl = "",
)

