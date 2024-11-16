package com.nevertouchgrass.smartpertzalkaapplication

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val greetingMessage: String
): ViewModel() {
    fun getGreeting() = greetingMessage
}