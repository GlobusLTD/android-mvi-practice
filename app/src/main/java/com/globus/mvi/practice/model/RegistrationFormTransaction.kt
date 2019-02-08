package com.globus.mvi.practice.model

data class RegistrationFormTransaction(
        val name: String,
        val gender: Gender,
        val age: Int,
        val about: String
)