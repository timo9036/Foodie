package com.example.foodie

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.foodie.viewmodels.GptViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule


class GptViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    // This rule ensures LiveData updates happen immediately.
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: GptViewModel

    @Mock
    private lateinit var uiActionsObserver: Observer<GptViewModel.UiAction>

    @Captor
    private lateinit var uiActionsCaptor: ArgumentCaptor<GptViewModel.UiAction>

    @Before
    fun setUp() {
        viewModel = GptViewModel()
        viewModel.uiActions.observeForever(uiActionsObserver)
    }

    @Test
    fun `given empty input, nothing happens`() {
        viewModel.handleInput("")

        verifyNoInteractions(uiActionsObserver)
    }

    @Test
    fun `given generate image command, placeholder message and image generation is triggered`() {
        viewModel.handleInput("generate image of a cat")

        verify(uiActionsObserver, times(2)).onChanged(uiActionsCaptor.capture())

        val values = uiActionsCaptor.allValues
        assert(values[0] is GptViewModel.UiAction.AddMessage) // Check that a message was added
        assert(values[1] is GptViewModel.UiAction.GenerateImage) // Check that image generation was triggered
    }

    @Test
    fun `given non-generate image command, text generation is triggered`() {
        val input = "Hello, World!"
        viewModel.handleInput(input)

        verify(uiActionsObserver).onChanged(GptViewModel.UiAction.AddMessage(any()))
        verify(uiActionsObserver).onChanged(GptViewModel.UiAction.GenerateText(input))
        verify(uiActionsObserver).onChanged(GptViewModel.UiAction.HandleUI)
    }

    @Test
    fun `given any input, UI handling actions are performed`() {
        viewModel.handleInput("test")

        verify(uiActionsObserver).onChanged(GptViewModel.UiAction.HandleUI)
    }

}