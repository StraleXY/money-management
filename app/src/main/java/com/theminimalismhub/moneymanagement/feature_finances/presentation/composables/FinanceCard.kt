package com.theminimalismhub.moneymanagement.feature_finances.presentation.composables

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.theminimalismhub.moneymanagement.core.enums.AccountType
import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import com.theminimalismhub.moneymanagement.core.utils.Colorer
import com.theminimalismhub.moneymanagement.core.utils.Currencier
import com.theminimalismhub.moneymanagement.feature_finances.domain.model.Finance

@Composable
fun FinanceCard(
    modifier: Modifier = Modifier,
    finance: Finance,
    previousSegmentDate: Int?,
    currency: String = "RSD",
    showSeparator: Boolean = true,
    onEdit: (Finance) -> Unit
) {
    Column(modifier = modifier) {
        previousSegmentDate?.let {
            if(showSeparator && previousSegmentDate != finance.getDay())
                Divider(
                    modifier = Modifier
                        .padding(horizontal = 34.dp)
                        .padding(top = 8.dp, bottom = 16.dp),
                    color = MaterialTheme.colors.secondaryVariant
                )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onEdit(finance) }
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.Start
            ) {
                Box(
                    modifier = Modifier
                        .width(5.dp)
                        .height(54.dp)
                        .background(if(finance.category?.color != null) Colorer.getAdjustedDarkColor(finance.category.color) else MaterialTheme.colors.onSurface, RoundedCornerShape(100))
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        modifier = Modifier.alpha(0.75f),
                        text = finance.finance.name,
                        style = MaterialTheme.typography.body2
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if(finance.finance.type == FinanceType.INCOME || finance.finance.type == FinanceType.TRANSACTION || !finance.finance.trackable) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .alpha(0.85f)
                                    .border(
                                        1.dp,
                                        MaterialTheme.colors.primary,
                                        RoundedCornerShape(19.dp)
                                    )
                                    .background(Color.Transparent),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector =
                                    if(!finance.finance.trackable) Icons.Default.MobiledataOff
                                    else when (finance.finance.type) {
                                            FinanceType.INCOME -> Icons.Default.ArrowDownward
                                            FinanceType.TRANSACTION -> Icons.Default.SyncAlt
                                            else -> Icons.Default.QuestionMark
                                        },
                                    contentDescription = "Finance type icon",
                                    tint = MaterialTheme.colors.primary,
                                    modifier = Modifier
                                        .size(13.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                        }
                        if(finance.account.type == AccountType.CRYPTO && finance.finance.type != FinanceType.TRANSACTION) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .alpha(0.85f)
                                    .border(
                                        1.dp,
                                        MaterialTheme.colors.primary,
                                        RoundedCornerShape(19.dp)
                                    )
                                    .background(Color.Transparent),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CurrencyBitcoin,
                                    contentDescription = "Finance type icon",
                                    tint = MaterialTheme.colors.primary,
                                    modifier = Modifier
                                        .size(13.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                        }
                        Text(
                            text = "${Currencier.formatAmount(finance.finance.amount)} $currency",
                            style = MaterialTheme.typography.h3
                        )
                    }
                }
            }
            Column(
                modifier = Modifier.padding(end = 2.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = finance.getDay().toString(),
                    style = MaterialTheme.typography.body1.copy(
                        fontSize = 20.sp
                    )
                )
                Text(
                    modifier = Modifier.alpha(0.65f),
                    text = finance.getMonth().uppercase(),
                    style = MaterialTheme.typography.body2.copy(
                        fontSize = 15.sp,
                        lineHeight = 16.sp
                    )
                )
            }
        }
    }
}