package com.theminimalismhub.moneymanagement.feature_finances.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.theminimalismhub.moneymanagement.R
import com.theminimalismhub.moneymanagement.core.composables.CircleIcon
import com.theminimalismhub.moneymanagement.core.composables.DashedBox
import com.theminimalismhub.moneymanagement.core.composables.HoldableActionButton
import com.theminimalismhub.moneymanagement.core.utils.Colorer
import com.theminimalismhub.moneymanagement.core.utils.Currencier
import com.theminimalismhub.moneymanagement.feature_accounts.presentation.composables.getAccountIcon
import com.theminimalismhub.moneymanagement.feature_finances.domain.model.RecommendedFinance

@Composable
fun RecommendedCard(
    recommended: RecommendedFinance,
    onDelete: (Int) -> Unit,
    onPay: (RecommendedFinance) -> Unit
) {
    DashedBox(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth(),
        widthPx = 4f,
        onPx = 23f,
        radius = 36.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 24.dp, top = 6.dp, bottom = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                        .background(if (recommended.category?.color != null) Colorer.getAdjustedDarkColor(recommended.category.color) else MaterialTheme.colors.onSurface, RoundedCornerShape(100))
                ) {
                    Icon(
                        modifier = Modifier
                            .size(20.dp)
                            .align(Alignment.Center),
                        imageVector = if (recommended.account != null) getAccountIcon(recommended.account.type) else Icons.Default.QuestionMark,
                        contentDescription = "Circle Icon",
                        tint = MaterialTheme.colors.background,
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        modifier = Modifier
                            .offset(y = 1.dp)
                            .alpha(0.85f),
                        text = recommended.recommended.placeName,
                        style = MaterialTheme.typography.h4
                    )
                    Text(
                        modifier = Modifier.offset(y = (-1).dp),
                        text = "${Currencier.formatAmount(recommended.recommended.amount)} ${recommended.recommended.currencyStr}",
                        style = MaterialTheme.typography.h3.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colors.primary
                    )
                }
            }
            Row {
                HoldableActionButton(
                    modifier = Modifier,
                    text = stringResource(id = R.string.action_delete),
                    icon = Icons.Default.Delete,
                    textStyle = MaterialTheme.typography.button,
                    duration = 2500,
                    circleColor = Color.Transparent,
                    alternatedColor = MaterialTheme.colors.error,
                    iconColor = MaterialTheme.colors.onBackground,
                    onHold = { onDelete(recommended.recommended.recommendedId!!) }
                )
            }
        }
    }
}