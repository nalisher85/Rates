package com.example.rates.sort

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SortOptionsViewModel @Inject constructor(): ViewModel() {

    private val _sortState = mutableStateOf<SortOption?>(null)
    val sortState = _sortState

    val onOptionSelected: (SortOption) -> Unit = {
        _sortState.value = it
    }

    enum class SortOption {
        NAME_AZ, NAME_ZA, VALUE_ASCENDING, VALUER_DESCENDING
    }

    companion object {
        const val SORT_OPTION_ARGUMENT = "sortOption"
    }
}