package com.nikoarap.slotmachine.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikoarap.slotmachine.db.classes.Configuration
import com.nikoarap.slotmachine.repositories.MainRepository
import kotlinx.coroutines.launch

class ConfigurationViewModel @ViewModelInject constructor(
    val mainRepository: MainRepository
) : ViewModel() {


}