package com.globus.mvi.practice.view

import android.text.Editable
import android.text.TextWatcher

class SimpleTextWatcher(
        private val onTextChanged: (String) -> Unit
) : TextWatcher {

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) =
            onTextChanged.invoke(s.toString())

    override fun afterTextChanged(s: Editable) = Unit


}