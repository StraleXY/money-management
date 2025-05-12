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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.theminimalismhub.moneymanagement.core.composables.CircleIcon
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
            if(showSeparator && previousSegmentDate != finance.getDay()) Spacer(modifier = Modifier.height(36.dp))
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
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        modifier = Modifier
                            .alpha(0.85f)
                            .padding(start = 2.dp),
                        text = finance.finance.name,
                        style = MaterialTheme.typography.h4
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .widthIn(min = 23.dp)
                                .height(23.dp)
                                .background(if (finance.category?.color != null) Colorer.getAdjustedDarkColor(finance.category.color) else MaterialTheme.colors.onSurface, RoundedCornerShape(100))
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if(finance.finance.type == FinanceType.INCOME || finance.finance.type == FinanceType.TRANSACTION || !finance.finance.trackable) {
                                    Spacer(modifier = Modifier.width(22.dp))
                                    CircleIcon(icon =
                                        if(!finance.finance.trackable) Icons.Default.MobiledataOff
                                        else when (finance.finance.type) {
                                            FinanceType.INCOME -> Icons.Default.ArrowDownward
                                            FinanceType.TRANSACTION -> Icons.Default.SyncAlt
                                            else -> Icons.Default.QuestionMark
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                }
                                if(finance.account.type == AccountType.CRYPTO && finance.finance.type != FinanceType.TRANSACTION) {
                                    if(!(finance.finance.type == FinanceType.INCOME || finance.finance.type == FinanceType.TRANSACTION || !finance.finance.trackable)) Spacer(modifier = Modifier.width(22.dp))
                                    else Spacer(modifier = Modifier.width(1.dp))
                                    CircleIcon(icon = Icons.Default.CurrencyBitcoin)
                                    Spacer(modifier = Modifier.width(2.dp))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${Currencier.formatAmount(finance.finance.amount)} $currency",
                            style = MaterialTheme.typography.h3.copy(fontWeight = FontWeight.Medium),
                            color = MaterialTheme.colors.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
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
                        fontSize = 21.sp
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