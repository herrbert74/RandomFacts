package com.pelagohealth.codingchallenge.data

import com.pelagohealth.codingchallenge.common.testhelper.FactDtoMother
import com.pelagohealth.codingchallenge.common.testhelper.FactMother
import com.pelagohealth.codingchallenge.data.db.FactDataSource
import com.pelagohealth.codingchallenge.data.network.FactApi
import com.pelagohealth.codingchallenge.data.repository.FactAccessor
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

internal class FactAccessorTest {

	private lateinit var underTest: FactAccessor

	private var api = mockk<FactApi>()
	private var dataSource = mockk<FactDataSource>()

	@Before
	fun setUp() {
		underTest = FactAccessor(
			api, dataSource, Dispatchers.Unconfined
		)
	}

	@Test
	internal fun `when get() and getFact() are called then should return fact from database`() = runTest {
		val apiResponse = FactDtoMother.createDefaultFactDto()

		coEvery { api.getFact() } returns apiResponse
		coEvery { dataSource.getFacts() } returns flowOf(FactMother.createFactList())

		underTest.get()
		val result = underTest.getFacts().first()

		coVerify { api.getFact() }
		coVerify { dataSource.getFacts() }
		result shouldBe FactMother.createFactList()

	}

	@Test
	internal fun `when delete is called then should call delete`() = runTest {

		coEvery { dataSource.deleteFact(any()) } returns Unit

		underTest.deleteFact("")

		coVerify { dataSource.deleteFact("") }

	}

}
