package com.example.foodie

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.foodie.viewmodels.GptViewModel
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.anyOrNull

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
    private val uiActionsCaptor: ArgumentCaptor<GptViewModel.UiAction> = ArgumentCaptor.forClass(GptViewModel.UiAction::class.java)

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
    fun `given whitespace input, no action is performed`() {
        viewModel.handleInput("   ")

        verify(uiActionsObserver, never()).onChanged(anyOrNull())
    }

    @Test
    fun `given any input, UI handling actions are performed`() {
        val input = "test"
        viewModel.handleInput(input)

        verify(uiActionsObserver).onChanged(GptViewModel.UiAction.HandleUI)
        println(input)
    }

    @Test
    fun `given non-generate image command, text generation is triggered`() {
        val input = "Hello, World!"
        viewModel.handleInput(input)

        verify(uiActionsObserver, times(3)).onChanged(anyOrNull())
        println(input)
    }

    @Test
    fun `given input not starting with generate image, text generation is triggered`() {
        val input = "What's up?"

        viewModel.handleInput(input)

        verify(uiActionsObserver, atLeastOnce()).onChanged(anyOrNull())
        val values = uiActionsCaptor.allValues

        // Verify GenerateText is triggered
        assertTrue(values.any { it is GptViewModel.UiAction.GenerateText && it.input == input })
        assert(values.any { it is GptViewModel.UiAction.GenerateText })

        values.forEach { println(it) }
    }

    @Test
    fun `given input starting with generate image, placeholder message and image generation are triggered`() {
        val input = "generate image of a cat"

        viewModel.handleInput(input)

        verify(uiActionsObserver, atLeastOnce()).onChanged(anyOrNull())
        val values = uiActionsCaptor.allValues

        // Verify AddMessage with "image" text
        assertTrue(values.any { it is GptViewModel.UiAction.AddMessage && it.message.text == "image" })

        // Verify GenerateImage is triggered
        assertTrue(values.any { it is GptViewModel.UiAction.GenerateImage && it.input == input })


        assertTrue(values.any { it is GptViewModel.UiAction.AddMessage })
        assertTrue(values.any { it is GptViewModel.UiAction.GenerateImage })

        values.forEach { println(it) }
    }

}