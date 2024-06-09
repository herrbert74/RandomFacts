package com.pelagohealth.codingchallenge.presentation

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.hasParent
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipe
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pelagohealth.codingchallenge.common.IoDispatcher
import com.pelagohealth.codingchallenge.common.MainDispatcher
import com.pelagohealth.codingchallenge.domain.api.FactRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var factRepository: FactRepository

    @Inject
    @MainDispatcher
    lateinit var mainContext: CoroutineDispatcher

    @Inject
    @IoDispatcher
    lateinit var ioContext: CoroutineDispatcher

    @Before
    fun setUp() {

        hiltAndroidRule.inject()

    }

    @After
    fun tearDown() {
        CoroutineScope(mainContext).launch {
            factRepository.deleteAll()
        }
    }

    @Test
    fun showFact() {

        composeTestRule.waitUntilAtLeastOneExists(hasTestTag("FactText"), 1000L)

        composeTestRule.onNodeWithText("This is a random fact", ignoreCase = true).assertExists()

    }

    @Test
    fun showMoreFacts() {
        runTest {

            composeTestRule.waitUntilAtLeastOneExists(hasTestTag("FactText"), 1000L)

            composeTestRule.onNodeWithText("More facts!", ignoreCase = true).performClick()

            val factTextMatcher = hasText("This is another random fact") and hasTestTag("FactText")
            val swipeableMatcher = hasText("This is a random fact") and hasParent(hasTestTag("Swipeable"))

            composeTestRule.onNode(factTextMatcher).assertExists()
            composeTestRule.onNode(swipeableMatcher).assertExists()

        }
    }

    @Test
    fun swipeDeleteFacts() = runTest {

        composeTestRule.waitUntilAtLeastOneExists(hasTestTag("FactText"), 1000L)

        composeTestRule.onNodeWithText("More facts!", ignoreCase = true).performClick()

        val swipeableMatcher = hasText("This is a random fact") and hasParent(hasTestTag("Swipeable"))
        composeTestRule.onNode(swipeableMatcher).performTouchInput{
            this.swipe(start = Offset(30f, 30f), end = Offset(300f, 0f), durationMillis = 20)
        }

        composeTestRule.onNode(swipeableMatcher).assertDoesNotExist()

    }

    @Test
    fun showOnlyThreeFacts() = runTest {

        composeTestRule.waitUntilAtLeastOneExists(hasTestTag("FactText"), 1000L)

        composeTestRule.onNodeWithText("More facts!", ignoreCase = true).performClick()
        composeTestRule.onNodeWithText("More facts!", ignoreCase = true).performClick()
        composeTestRule.onNodeWithText("More facts!", ignoreCase = true).performClick()
        composeTestRule.onNodeWithText("More facts!", ignoreCase = true).performClick()

        val swipeableMatcher = hasText("This is yet another random fact")
        val swipeableNotMatcher = hasText("This is a random fact")

        composeTestRule.onNode(swipeableMatcher).assertExists()
        composeTestRule.onNode(swipeableNotMatcher).assertDoesNotExist()

    }

}