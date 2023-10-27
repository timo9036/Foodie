package com.example.foodie.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodie.util.Message
import com.example.foodie.util.User
import java.util.Calendar

class GptViewModel : ViewModel() {

    // Live data to trigger UI actions
    private val _uiActions = MutableLiveData<UiAction>()
    val uiActions: LiveData<UiAction> = _uiActions

    val us = User("1", "Tim", "")
    val chatGpt = User("2", "ChatGPT", "")

    fun handleInput(input: String) {
        if (input.trim().isEmpty()) {
            return
        }
        val message = Message("m1", input, us, Calendar.getInstance().time, "")
        _uiActions.value = UiAction.AddMessage(message)

        if (input.startsWith("generate image")) {
            val placeholderMessage = Message(
                "m_temp",
                "image",
                chatGpt,
                Calendar.getInstance().time,
                "https://static.vecteezy.com/system/resources/thumbnails/011/299/215/small/simple-loading-or-buffering-icon-design-png.png"
            )
            _uiActions.value = UiAction.AddMessage(placeholderMessage)
            _uiActions.value = UiAction.GenerateImage(input, placeholderMessage)
        } else {
            _uiActions.value = UiAction.GenerateText(input)
        }
    }

    sealed class UiAction {
        data class AddMessage(val message: Message) : UiAction()
        data class GenerateImage(val input: String, val placeholderMessage: Message) : UiAction()
        data class GenerateText(val input: String) : UiAction()
    }
}