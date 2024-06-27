package com.pelagohealth.codingchallenge.presentation.ui.main

import com.github.michaelbull.result.Ok
import com.pelagohealth.codingchallenge.common.testhelper.FactMother
import com.pelagohealth.codingchallenge.domain.api.FactRepository
import com.pelagohealth.codingchallenge.domain.model.Fact
import com.pelagohealth.codingchallenge.presentation.MainViewModel
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class MainViewModelTest {

    private val factRepository = mockk<FactRepository>(relaxed = true)

    private lateinit var mainViewModel: MainViewModel

    private val factFlow = MutableSharedFlow<List<Fact>>(replay = 1)

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {

        Dispatchers.setMain(dispatcher)
        coEvery { factRepository.get() } returns Ok(Unit)
        coEvery { factRepository.getFacts() } answers { factFlow }
        mainViewModel = MainViewModel(factRepository)
        CoroutineScope(Dispatchers.Unconfined).launch {
            factFlow.emit(FactMother.createFactList())
        }

    }

    @After
    fun tearDown() {

        Dispatchers.resetMain()

    }

    @Test
    fun `when started then getFacts() and get() are called and return a fact`() = runTest {

        coVerify(exactly = 1) { factRepository.getFacts() }
        coVerify(exactly = 1) { factRepository.get() }
        mainViewModel.state.value.fact shouldBe FactMother.createFactList().last()

    }

    @Test
    fun `when started then returns previous list`() = runTest {

        mainViewModel.state.value.previousFacts shouldBe FactMother.createFactList().dropLast(1).reversed()

    }

    @Test
    fun `when more facts button is clicked then the previously displayed fact is added to the list`() = runTest {

        mainViewModel.fetchNewFact()

        //Simulate network and database response
        factFlow.emit(FactMother.createFactListAfterUpdate())

        advanceUntilIdle()
        mainViewModel.state.value.previousFacts.last().text shouldBe "name2"

    }

    @Test
    fun `when previous text is swiped then delete fact is called`() = runTest {

        mainViewModel.deleteFact("text1")

        advanceUntilIdle()

        coVerify(exactly = 1) { factRepository.deleteFact("text1") }

    }

}


