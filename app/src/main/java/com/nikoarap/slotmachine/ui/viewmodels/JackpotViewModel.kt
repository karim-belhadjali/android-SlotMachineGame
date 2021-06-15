package com.nikoarap.slotmachine.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.nikoarap.slotmachine.repositories.MainRepository

class JackpotViewModel @ViewModelInject constructor(
    val mainRepository: MainRepository
) : ViewModel() {

    val prizesConfiguration = mainRepository.getConfiguration();


}