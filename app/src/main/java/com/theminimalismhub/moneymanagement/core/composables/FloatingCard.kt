package com.theminimalismhub.moneymanagement.core.composables

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun FloatingCard(
    visible: Boolean,
    modifier: Modifier = Modifier,
    header: @Composable ColumnScope.() -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInVertically(initialOffsetY = { fullHeight -> fullHeight / 2 }),
        exit = fadeOut(tween(450))
                + slideOutVertically(
            targetOffsetY = { fullHeight -> fullHeight / 2 }, animationSpec = tween(450)
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.BottomCenter),
                verticalArrangement = Arrangement.Bottom
            ) {
                header()
                Card(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .padding(bottom = 58.dp),
                    elevation = Dp(8f),
                    shape = RoundedCornerShape(15.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .padding(vertical = 20.dp)
                            .padding(top = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        content = content
                    )
                }
            }
        }
    }
}