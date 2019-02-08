package com.globus.mvi.practice.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.globus.mvi.practice.R
import com.globus.mvi.practice.presentation.RegistrationFormPresenter
import com.globus.mvi.practice.presentation.RegistrationFormView
import com.globus.mvi.practice.presentation.RegistrationFormViewState

class RegistrationFormActivity : AppCompatActivity(), RegistrationFormView {

    private val presenter: RegistrationFormPresenter = RegistrationFormPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter.onCreate()
    }

    override fun onViewStateChanged(viewState: RegistrationFormViewState) {
        // TODO: render view state
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

}
