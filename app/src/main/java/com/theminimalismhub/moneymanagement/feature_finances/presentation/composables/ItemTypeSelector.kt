package com.theminimalismhub.moneymanagement.feature_finances.presentation.composables

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.MobiledataOff
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.theminimalismhub.moneymanagement.R
import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import com.theminimalismhub.moneymanagement.core.utils.Shade
import com.theminimalismhub.moneymanagement.core.utils.shadedBackground

@Composable
fun ItemsTypeSelector(
    modifier: Modifier = Modifier,
    itemsTypeStates: Map<Int, MutableState<Boolean>>,
    itemToggled: (Int) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadedBackground(Shade.DARK)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 20.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            ToggleChip(
                name = "MIXED OVERVIEW",
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_mobiledata_off_24),
                        contentDescription = "MIXED VIEW"
                    )
                },
                toggled = itemsTypeStates[0]!!.value,
                onSelected = { itemToggled(0) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            ToggleChip(
                name = "OUTCOME ITEMS",
                icon = {
                    Icon(
                        imageVector = Icons.Default.ArrowUpward,
                        contentDescription = "OUTCOME ITEMS"
                    )
                },
                toggled = itemsTypeStates[1]!!.value,
                onSelected = { itemToggled(1) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            ToggleChip(
                name = "INCOME ITEMS",
                icon = {
                    Icon(
                        modifier = Modifier.rotate(180f),
                        imageVector = Icons.Default.ArrowUpward,
                        contentDescription = "INCOME ITEMS"
                    )
                },
                toggled = itemsTypeStates[2]!!.value,
                onSelected = { itemToggled(2) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            ToggleChip(
                name = "UNTRACKED ITEMS",
                icon = {
                    Icon(
                        imageVector = Icons.Default.MobiledataOff,
                        contentDescription = "UNTRACKED ITEMS"
                    )
                },
                toggled = itemsTypeStates[3]!!.value,
                onSelected = { itemToggled(3) }
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

private enum class FinanceDisplayTypes(val financeTypes: List<FinanceType>) {
    OUTCOME(listOf(FinanceType.OUTCOME)),
    INCOME(listOf(FinanceType.INCOME)),
    MIXED(listOf(FinanceType.OUTCOME, FinanceType.INCOME));

    fun next(): FinanceDisplayTypes {
        val values = values()
        return values[(ordinal + 1) % values.size]
    }
}

private enum class FinanceDisplayUntracked(val tracked: List<Boolean>) {
    TRACKED(listOf(true)),
    UNTRACKED(listOf(false)),
    BOTH(listOf(true, false));

    fun next(): FinanceDisplayUntracked {
        val values = values()
        return values[(ordinal + 1) % values.size]
    }
}

@Composable
fun ItemTypeSelectorV2(
    modifier: Modifier = Modifier,
    onTypeChanged: (List<FinanceType>) -> Unit,
    onTrackedChanged: (List<Boolean>) -> Unit
) {

    var type: FinanceDisplayTypes by remember { mutableStateOf(FinanceDisplayTypes.OUTCOME) }
    fun cycleType() {
        type = type.next()
        onTypeChanged(type.financeTypes)
    }
    fun getTypeText() : String {
        return when(type) {
            FinanceDisplayTypes.OUTCOME -> "Outcome"
            FinanceDisplayTypes.INCOME -> "Income"
            FinanceDisplayTypes.MIXED -> "Mixed"
        }
    }

    var untracked: FinanceDisplayUntracked by remember { mutableStateOf(FinanceDisplayUntracked.BOTH) }
    fun cycleUntracked() {
        untracked = untracked.next()
        onTrackedChanged(untracked.tracked)
    }
    fun getUntrackedText() : String {
        return when(untracked) {
            FinanceDisplayUntracked.TRACKED -> "Tracked"
            FinanceDisplayUntracked.UNTRACKED -> "Untracked"
            FinanceDisplayUntracked.BOTH -> "Everything"
        }
    }
    Box(
        modifier = Modifier
            .shadedBackground(Shade.DARK)
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .padding(start = 20.dp, top = 20.dp, end = 20.dp)
                .fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadedBackground(Shade.MID, RoundedCornerShape(100))
                    .height(44.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .shadedBackground(Shade.MID, RoundedCornerShape(100)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilledIconButton(
                        modifier = Modifier
                            .scale(0.9f)
                            .offset(x = (-2.5).dp),
                        onClick = { cycleType() },
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = if(MaterialTheme.colors.isLight) Color.White else Color.Black
                        )
                    ) {
                        if (type == FinanceDisplayTypes.MIXED) Icon(
                            painter = painterResource(id = R.drawable.baseline_mobiledata_off_24),
                            contentDescription = "MIXED VIEW"
                        )
                        else Icon(
                            imageVector = if(type == FinanceDisplayTypes.OUTCOME) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                            contentDescription = "Finance Type Selector Icon"
                        )
                    }
                    Text(
                        modifier = Modifier
                            .padding(start = 4.dp, end = 16.dp)
                            .animateContentSize(),
                        text = getTypeText().uppercase(),
                        color = MaterialTheme.colors.onSurface,
                        style = MaterialTheme.typography.button
                    )
                }

                Row(
                    modifier = Modifier
                        .shadedBackground(Shade.MID, RoundedCornerShape(100)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .padding(end = 4.dp, start = 16.dp)
                            .animateContentSize(),
                        text = getUntrackedText().uppercase(),
                        color = MaterialTheme.colors.onSurface,
                        style = MaterialTheme.typography.button
                    )
                    FilledIconButton(
                        modifier = Modifier
                            .scale(0.9f)
                            .offset(x = (2.5).dp),
                        onClick = { cycleUntracked() },
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = if(MaterialTheme.colors.isLight) Color.White else Color.Black
                        )
                    ) {
                        Icon(
                            imageVector = when(untracked) {
                                FinanceDisplayUntracked.TRACKED -> Icons.Default.Visibility
                                FinanceDisplayUntracked.UNTRACKED -> Icons.Default.VisibilityOff
                                FinanceDisplayUntracked.BOTH -> Icons.Default.BarChart
                            },
                            contentDescription = "Finance Untracked Selector Icon"
                        )
                    }
                }
            }
        }
    }
}