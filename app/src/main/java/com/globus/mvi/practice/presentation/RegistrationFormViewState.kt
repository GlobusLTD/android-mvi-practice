package com.globus.mvi.practice.presentation

import com.globus.mvi.practice.model.Gender
import com.globus.mvi.practice.model.RegistrationFormTransaction
import com.globus.mvi.practice.presentation.util.ListSelection
import com.globus.mvi.practice.presentation.util.Validation
import com.gojuno.koptional.None
import com.gojuno.koptional.Optional

data class RegistrationFormViewState(
        val name: Validation<String> = Validation.Empty(""),
        val gender: ListSelection<Gender> = ListSelection(),
        val age: Validation<Optional<Int>> = Validation.Empty(None),
        val about: Validation<String> = Validation.Empty(""),
        val transaction: Optional<RegistrationFormTransaction> = None
)