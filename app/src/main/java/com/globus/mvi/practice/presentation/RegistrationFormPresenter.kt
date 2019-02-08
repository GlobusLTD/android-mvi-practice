package com.globus.mvi.practice.presentation

import com.globus.mvi.practice.presentation.util.PartialViewStates
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.exceptions.Exceptions
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy

class RegistrationFormPresenter(
        private val view: RegistrationFormView
) {

    // TODO: Add input event subjects
    private val disposables = CompositeDisposable()

    fun onCreate() {
        // TODO: Observe input events and prepare partial view states

        disposables += Observable.merge(/* TODO: add partial states observables */)
                .scan(RegistrationFormViewState(), PartialViewStates.apply())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = { viewState -> view.onViewStateChanged(viewState) },
                        onError = { throwable -> Exceptions.propagate(throwable) }
                )
    }

    fun onDestroy() = disposables.clear()

}