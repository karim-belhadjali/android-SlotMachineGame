package com.nikoarap.slotmachine.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikoarap.slotmachine.db.classes.User
import com.nikoarap.slotmachine.repositories.MainRepository
import kotlinx.coroutines.launch

class RegisterViewModel @ViewModelInject constructor(
    val mainRepository: MainRepository
) : ViewModel() {

    var userCount= mainRepository.getUsersCount()

    fun insertUser(user: User) = viewModelScope.launch {
        mainRepository.insertUser(user)

    }
}