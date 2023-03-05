package com.theminimalismhub.moneymanagement.feature_categories.presentation.manage_categories

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ManageCategoriesViewModel @Inject constructor(

) : ViewModel() {

    private val _state = mutableStateOf(ManageCategoriesState())
    val state: State<ManageCategoriesState> = _state

    fun onEvent(event: ManageCategoriesEvent) {

    }

}