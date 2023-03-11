package com.theminimalismhub.moneymanagement.feature_finances.presentation.composables

import android.app.DatePickerDialog
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.theminimalismhub.moneymanagement.R
import com.theminimalismhub.moneymanagement.core.enums.RangeType
import com.theminimalismhub.moneymanagement.feature_finances.domain.utils.RangePickerService
import java.util.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RangePicker(
    modifier: Modifier = Modifier,
    rangeService: RangePickerService,
    rangePicked: (Pair<Long, Long>) -> Unit
) {

    var rangePreview by remember { mutableStateOf(rangeService.formattedDate()) }
    var isToday by remember { mutableStateOf(true) }

    fun update() {
        rangePreview = rangeService.formattedDate()
        isToday = rangeService.isToday()
        rangePicked(Pair(rangeService.getStartTimestamp(), rangeService.getEndTimestamp()))
    }

    val picker = datePickerDialog(
        initialTime = rangeService.getCurrentTimestamp(),
        datePicked = {
            rangeService.set(it)
            update()
        }
    )

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            ToggleButton(
                text = "DAILY",
                isToggled = rangeService.type == RangeType.DAILY
            ) {
                rangeService.setModeDay()
                update()
            }
            ToggleButton(
                text = "WEEKLY",
                isToggled = rangeService.type == RangeType.WEEKLY
            ) {
                rangeService.setModeWeek()
                update()
            }
            ToggleButton(
                text = "MONTHLY",
                isToggled = rangeService.type == RangeType.MONTHLY
            ) {
                rangeService.setModeMonth()
                update()
            }
        }
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RangePrevious {
                rangeService.previous()
                update()
            }
            AnimatedContent(targetState = rangePreview ?: "Not Selected") {
                Text(
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            if(rangeService.type == RangeType.DAILY) {
                                picker.updateDate(
                                    rangeService.getStartDay().get(Calendar.YEAR),
                                    rangeService.getStartDay().get(Calendar.MONTH),
                                    rangeService.getStartDay().get(Calendar.DAY_OF_MONTH)
                                )
                                picker.show()
                            }
                        },
                    text = it,
                    style = MaterialTheme.typography.body2.copy(fontSize = 18.sp)
                )
            }
            RangeNext(
                onTodayClick = {
                    rangeService.setToday()
                    update()
                },
                isToday = isToday
            ) {
                rangeService.next()
                update()
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun RangePrevious(
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.width(96.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = Icons.Default.ArrowLeft.name
            )
        }
    }
}

@Composable
private fun RangeNext(
    isToday: Boolean,
    onTodayClick: () -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.width(96.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        AnimatedVisibility(
            visible = !isToday,
            enter = fadeIn() + slideInVertically(initialOffsetY = { h -> -h/4 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { h -> -h/4 })
        ) {
            IconButton(onClick = onTodayClick) {
                Icon(
                    imageVector = Icons.Default.Today,
                    contentDescription = Icons.Default.ArrowLeft.name
                )
            }
        }
        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = Icons.Default.ArrowLeft.name
            )
        }
    }
}

@Composable
private fun ToggleButton(
    text: String,
    isToggled: Boolean,
    onClick: () -> Unit
) {
    Text(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 24.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() }
            .alpha(animateFloatAsState(targetValue = if (isToggled) 1f else 0.5f).value),
        text = text,
        style = MaterialTheme.typography.button
    )
}

@Composable
private fun datePickerDialog(
    initialTime: Long = System.currentTimeMillis(),
    datePicked: (Long) -> Unit
) : DatePickerDialog {
    var timestamp by remember { mutableStateOf(initialTime) }
    val mContext = LocalContext.current
    val mCalendar = Calendar.getInstance()
    mCalendar.time = Date(timestamp)

    val mDate = remember {
        mutableStateOf(
            "${mCalendar.get(Calendar.DAY_OF_MONTH)}/${mCalendar.get(Calendar.MONTH) + 1}/${mCalendar.get(
                Calendar.YEAR)}"
        )
    }

    val mDatePickerDialog = DatePickerDialog(
        mContext,
        R.style.my_dialog_theme,
        { _, mYear, mMonth, mDayOfMonth ->
            mDate.value = "$mDayOfMonth/${mMonth+1}/$mYear"
            mCalendar.clear()
            mCalendar.set(mYear, mMonth, mDayOfMonth)
            timestamp = mCalendar.timeInMillis
            datePicked(timestamp)
        },
        mCalendar.get(Calendar.YEAR),
        mCalendar.get(Calendar.MONTH),
        mCalendar.get(Calendar.DAY_OF_MONTH)
    )

    return mDatePickerDialog
}