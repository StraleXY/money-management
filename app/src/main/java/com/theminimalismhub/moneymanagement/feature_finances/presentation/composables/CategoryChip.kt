package com.theminimalismhub.moneymanagement.feature_finances.presentation.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.theminimalismhub.moneymanagement.core.composables.ActionChip
import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import com.theminimalismhub.moneymanagement.core.utils.Colorer
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_accounts.presentation.composables.SelectableAccountChipLarge
import com.theminimalismhub.moneymanagement.feature_categories.domain.model.Category

@Composable
fun CategoryChip(
    modifier: Modifier = Modifier,
    text: String,
    color: Color,
    onToggled: (Boolean) -> Unit = {},
    isToggled: Boolean = false
) {

    @Composable
    fun getTextColor() : Color {
        return if(MaterialTheme.colors.isLight) Color.White
        else Color.Black
    }

    val backgroundColor = animateColorAsState(targetValue = if(isToggled) color else MaterialTheme.colors.secondaryVariant, tween(durationMillis = 250))
    val textColor = animateColorAsState(targetValue = if(isToggled) getTextColor() else MaterialTheme.colors.onSurface, tween(durationMillis = 250))

    ActionChip(
        modifier = modifier,
        text = text,
        textColor = textColor.value,
        backgroundColor = backgroundColor.value,
        borderThickness = 0.dp,
        onClick = { onToggled(!isToggled) },
        backgroundStrength = 1f
    )
}

@Composable
fun CategoryChipsSelectable(
    modifier: Modifier = Modifier,
    categories: List<Category>,
    selectedCategories: List<Category>,
    multiple: Boolean,
    listState: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(horizontal = 32.dp),
    spacing: Dp = 8.dp,
    selectionChanged: (List<Int>) -> Unit
) {

    val selection: MutableList<Int> = remember { mutableStateListOf() }
    fun categoryClicked(id: Int) {
        if(!multiple) {
            if (selection.contains(id)) return
            selection.clear()
            selection.add(id)
        }
        else if (!selection.remove(id)) selection.add(id)
        selectionChanged(selection.toList())
    }
    LaunchedEffect(selectedCategories) {
        selection.clear()
        selection.addAll(selectedCategories.map { it.categoryId!! })
    }

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.Start,
        state = listState
    ) {
        items(categories.filter { it.type == FinanceType.OUTCOME }) { category ->
            CategoryChip(
                text = category.name,
                color = Colorer.getAdjustedDarkColor(category.color),
                isToggled = selection.contains(category.categoryId),
                onToggled = { categoryClicked(category.categoryId!!) }
            )
            Spacer(modifier = Modifier.width(spacing))
        }
    }
}