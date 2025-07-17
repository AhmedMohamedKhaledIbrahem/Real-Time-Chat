package com.example.realtimechatapp.features.authentication.data.mapper

import com.example.realtimechatapp.core.database.data.entity.user.UserEntity
import com.example.realtimechatapp.features.authentication.data.model.SignUpModel
import com.example.realtimechatapp.features.authentication.data.model.UserModel

fun UserModel.toUserEntity(uid: String): UserEntity = UserEntity(
    id = uid,
    name = name,
    email = email,
    image = imageUrl,
    phoneNumber = phone,
)
fun UserModel.toSignUpModel(): SignUpModel = SignUpModel(
    name = name,
    email = email,
    phoneNumber = phone,
    password = ""
)