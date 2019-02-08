package com.globus.mvi.practice.presentation.util

 typealias PartialViewState<ViewState> = (ViewState) -> ViewState

object PartialViewStates {

    fun <ViewState> apply(): (ViewState, PartialViewState<ViewState>) -> ViewState = { viewState, partial -> partial.invoke(viewState) }

}