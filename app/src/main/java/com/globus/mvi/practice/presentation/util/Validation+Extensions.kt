package com.globus.mvi.practice.presentation.util

sealed class Validation<out T> {

    abstract val value: T
    abstract val isPassed: Boolean

    data class Empty<T>(override val value: T) : Validation<T>() {
        override val isPassed: Boolean = false
    }

    data class Passed<T>(override val value: T) : Validation<T>() {
        override val isPassed: Boolean = true
    }

    data class Failed<T>(override val value: T, val text: String) : Validation<T>() {
        override val isPassed: Boolean = false
    }

    data class Counter<T : CharSequence>(override val value: T, val minLength: Int = 0, val maxLength: Int) : Validation<T>() {
        val length: Int = value.length
        override val isPassed: Boolean = length in minLength..maxLength
    }

}

typealias Validator<T> = (T) -> Validation<T>

infix fun <T> Validator<T>.and(other: Validator<T>): Validator<T> = { value ->
    val validation = this.invoke(value)
    if (validation.isPassed) other.invoke(value) else validation
}

fun notEmpty(): Validator<String> = { value ->
    if (value.isBlank()) Validation.Empty(value) else Validation.Passed(value)
}

fun minLength(minLength: Int): Validator<String> = { value ->
    when {
        value.length < minLength -> Validation.Failed(value, "Value should contain at least $minLength symbols")
        else -> Validation.Passed(value)
    }
}

fun maxLength(maxLength: Int): Validator<String> = { value ->
    when {
        value.length > maxLength -> Validation.Failed(value, "Value should contain less than ${maxLength + 1} symbols")
        else -> Validation.Passed(value)
    }
}

fun pattern(pattern: Regex): Validator<String> = { value ->
    when {
        pattern.matches(value) -> Validation.Passed(value)
        else -> Validation.Failed(value, "Value does not match pattern='$pattern'")
    }
}

fun <T : CharSequence> counter(minLength: Int = 0, maxLength: Int): Validator<T> = { value ->
    Validation.Counter(value, minLength, maxLength)
}