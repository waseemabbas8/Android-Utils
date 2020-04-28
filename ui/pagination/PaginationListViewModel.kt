package com.softsolutions.bookryt.ui.common.pagination

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.softsolutions.bookryt.model.common.RequestState

abstract class PaginationListViewModel<T> : ViewModel() {

    val items: LiveData<List<T>>
    protected val _items = MutableLiveData<List<T>>()

    val isRefreshing: LiveData<Boolean>
    protected val _isRefreshing = MutableLiveData<Boolean>()

    val hasLoadedAll: LiveData<Boolean>
    protected val _hasLoadedAll = MutableLiveData<Boolean>()

    val state: LiveData<RequestState>
    protected val _state = MutableLiveData<RequestState>()

    init {
        items = _items
        _items.value = emptyList()

        hasLoadedAll = _hasLoadedAll
        isRefreshing = _isRefreshing
        state = _state
    }

    abstract fun loadData(row: Int)

}