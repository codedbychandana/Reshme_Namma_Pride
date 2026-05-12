package com.example.reshme_nammapride.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reshme_nammapride.R
import com.example.reshme_nammapride.domain.model.InstarStage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ManagementViewModel : ViewModel() {

    private val _selectedStage = MutableStateFlow(InstarStage.FIRST_INSTAR)
    val selectedStage: StateFlow<InstarStage> = _selectedStage


    fun updateStage(stage: InstarStage) {
        _selectedStage.value = stage
    }


    val managementAdviceResId: StateFlow<Int> = _selectedStage.map { stage ->
        when (stage) {
            InstarStage.FIRST_INSTAR -> R.string.adv_mgt_1st
            InstarStage.SECOND_INSTAR -> R.string.adv_mgt_2nd
            InstarStage.THIRD_INSTAR -> R.string.adv_mgt_3rd
            InstarStage.FOURTH_INSTAR -> R.string.adv_mgt_4th
            InstarStage.FIFTH_INSTAR -> R.string.adv_mgt_5th
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = R.string.adv_mgt_1st
    )


    val managementIconResId: StateFlow<Int> = _selectedStage.map { stage ->
        when (stage) {
            InstarStage.FIRST_INSTAR, InstarStage.SECOND_INSTAR -> R.drawable.ic_chopped_leaf
            else -> R.drawable.ic_whole_leaf
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = R.drawable.ic_chopped_leaf
    )
}