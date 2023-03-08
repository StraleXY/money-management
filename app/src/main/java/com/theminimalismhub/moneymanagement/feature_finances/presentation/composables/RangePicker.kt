package com.theminimalismhub.moneymanagement.feature_finances.presentation.composables

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.theminimalismhub.moneymanagement.feature_finances.domain.utils.RangePickerService

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RangePicker(
    modifier: Modifier = Modifier,
    rangeService: RangePickerService
) {

    var rangePreview by remember { mutableStateOf(rangeService.formattedDate()) }
    var isToday by remember { mutableStateOf(true) }

    fun update() {
        rangePreview = rangeService.formattedDate()
        isToday = rangeService.isToday()
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Previous
        Row(
            modifier = Modifier.width(96.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                rangeService.previous()
                update()
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = Icons.Default.ArrowLeft.name
                )
            }
        }
        // Text
        AnimatedContent(targetState = rangePreview ?: "Not Selected") {
            Text(
                text = it ,
                style = MaterialTheme.typography.body2.copy(
                    fontSize = 18.sp
                )
            )
        }

        // Next
        Row(
            modifier = Modifier.width(96.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            AnimatedVisibility(
                visible = !isToday,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                IconButton(onClick = {
                    rangeService.setToday()
                    update()
                }) {
                    Icon(
                        imageVector = Icons.Default.Today,
                        contentDescription = Icons.Default.ArrowLeft.name
                    )
                }
            }
            IconButton(onClick = {
                rangeService.next()
                update()
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowForwardIos,
                    contentDescription = Icons.Default.ArrowLeft.name
                )
            }
        }
    }
}