package com.pelagohealth.codingchallenge.presentation.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.pelagohealth.codingchallenge.domain.model.Fact
import com.pelagohealth.codingchallenge.presentation.MainViewModel
import com.pelagohealth.codingchallenge.presentation.theme.Colors
import com.pelagohealth.codingchallenge.presentation.theme.Dimens
import com.pelagohealth.codingchallenge.presentation.theme.PelagoTheme
import com.pelagohealth.codingchallenge.presentation.theme.PelagoTypography
import com.pelagohealth.codingchallenge.presentation.theme.titleMediumBold
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    uiState: MainViewModel.UiState,
    onMoreClick: () -> Unit,
    onSwipe: (String) -> Unit
) {

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.marginLarge, vertical = Dimens.marginNormal)
            ) {
                if (uiState.isLoading) {
                    Box(
                        modifier = Modifier
                            .padding(Dimens.marginLarge)
                            .fillParentMaxWidth(),
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(Dimens.marginLarge)
                                .align(Alignment.Center)
                                .width(Dimens.fabSize),
                            color = Colors.secondary,
                            trackColor = Colors.surfaceVariant,
                        )
                    }
                } else if (uiState.error != null) {
                    Text(
                        modifier = modifier
                            .padding(Dimens.marginLarge),
                        text = "Something went wrong",
                        color = Colors.error,
                    )
                } else {
                    Text(
                        text = uiState.fact?.text ?: "",
                        modifier = modifier
                            .padding(Dimens.marginLarge)
                            .testTag("FactText")
                    )
                }

                Button(
                    modifier = modifier.padding(Dimens.marginLarge),
                    onClick = {
                        onMoreClick()
                    }) {
                    Text("More facts!")
                }
            }
        }
        if (uiState.previousFacts.isNotEmpty()) {
            item {
                Text(
                    text = "Previous facts",
                    style = PelagoTypography.titleMediumBold,
                    modifier = modifier
                        .fillParentMaxWidth()
                        .padding(
                            top = Dimens.marginLarge,
                            start = Dimens.marginDoubleLarge,
                            bottom = Dimens.marginNormal
                        )
                )
            }
        }
        items(
            uiState.previousFacts.size,
            { index -> uiState.previousFacts[index].hashCode() }
        ) { index ->
            val fact = uiState.previousFacts[index]
            SwipeableText(text = fact.text, onSwipe)
            HorizontalDivider(thickness = Dimens.marginSmallest, color = Colors.onSurface)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    PelagoTheme {
        MainScreen(
            uiState = MutableStateFlow(
                MainViewModel.UiState(
                    fact = Fact("This is a fact"),
                    previousFacts = listOf(
                        Fact("This is a fact too"),
                        Fact("This is another fact"),
                        Fact("This is yet another fact")
                    )
                )
            ).collectAsState().value,
            onMoreClick = {}
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenLoadingPreview() {
    PelagoTheme {
        MainScreen(
            uiState = MutableStateFlow(
                MainViewModel.UiState(
                    isLoading = true,
                )
            ).collectAsState().value,
            onMoreClick = {}
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenErrorPreview() {
    PelagoTheme {
        MainScreen(
            uiState = MutableStateFlow(
                MainViewModel.UiState(
                    error = IllegalStateException(),
                )
            ).collectAsState().value,
            onMoreClick = {}
        ) {}
    }
}