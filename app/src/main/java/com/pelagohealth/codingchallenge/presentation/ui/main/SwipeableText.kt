package com.pelagohealth.codingchallenge.presentation.ui.main

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import com.pelagohealth.codingchallenge.presentation.theme.Dimens

@Composable
fun SwipeableText(
    text: String,
    onSwipe: (String) -> Unit
) {
    var offsetX by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.marginLarge)
    ) {
        Box(
            modifier = Modifier
                //.padding(top = Dimens.marginLarge)
                .graphicsLayer {
                    this.translationX = offsetX
                }
                .testTag("Swipeable")
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                        if (offsetX > 150) {
                            onSwipe(text)
                        }
                    }
                }

        ) {
            Text(text = text)
        }
    }
}