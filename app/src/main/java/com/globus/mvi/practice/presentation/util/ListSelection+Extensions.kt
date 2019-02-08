package com.globus.mvi.practice.presentation.util

import com.gojuno.koptional.None
import com.gojuno.koptional.Optional
import com.gojuno.koptional.Some
import com.gojuno.koptional.toOptional
import io.reactivex.Observable

data class ListSelection<T : Any>(
        val items: List<T> = emptyList(),
        val selected: Optional<T> = None
)

typealias ItemSelector<T> = List<T>.() -> Optional<T>

fun <T : Any> none(): ItemSelector<T> = { None }

fun <T : Any> first(): ItemSelector<T> = { firstOrNull().toOptional() }

fun <T : Any> withPredicate(predicate: (T) -> Boolean): ItemSelector<T> = { find(predicate).toOptional() }

typealias KeySelector<T, K> = (T) -> K

fun <T : Any> identity(): KeySelector<T, T> = { item -> item }

private typealias ListSelectionPartialState<T> = (ListSelection<T>) -> ListSelection<T>

object ListSelectionPartialStates {

    fun <T : Any> withSelector(items: List<T>, selector: ItemSelector<T>): ListSelectionPartialState<T> = { previousListSelection ->
        previousListSelection.copy(items = items, selected = selector(items))
    }

    fun <T : Any, K : Any> items(items: List<T>, keySelector: KeySelector<T, K>): ListSelectionPartialState<T> = { previousListSelection ->
        val previousSelected = previousListSelection.selected
        previousListSelection.copy(
                items = items,
                selected = when (previousSelected) {
                    is Some -> {
                        val previousSelectedKey = keySelector(previousSelected.value)
                        items.find { item -> previousSelectedKey == keySelector(item) }.toOptional()
                    }
                    is None -> None
                }
        )
    }

    fun <T : Any> selected(selected: Optional<T>): ListSelectionPartialState<T> = { previousListSelection ->
        previousListSelection.copy(selected = selected)
    }

}

fun <T : Any> Observable<List<T>>.listSelection(
        selected: Observable<Optional<T>>,
        firstItemSelector: ItemSelector<T>
): Observable<ListSelection<T>> = listSelection<T, T>(selected, firstItemSelector, identity())

fun <T : Any, K : Any> Observable<List<T>>.listSelection(
        selected: Observable<Optional<T>>,
        firstItemSelector: ItemSelector<T>,
        keySelector: KeySelector<T, K>
): Observable<ListSelection<T>> {
    val itemsSourceObservable = this.replay(1).refCount()

    val initialSelectionObservable = itemsSourceObservable
            .take(1)
            .map { items -> ListSelectionPartialStates.withSelector(items, firstItemSelector) }

    val itemsObservable = itemsSourceObservable
            .skip(1)
            .map { items -> ListSelectionPartialStates.items(items, keySelector) }

    val selectedObservable = selected
            .distinctUntilChanged()
            .map { item -> ListSelectionPartialStates.selected(item) }

    return Observable.merge(initialSelectionObservable, itemsObservable, selectedObservable)
            .scan(ListSelection(), PartialViewStates.apply())
            .distinctUntilChanged()
}