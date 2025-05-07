package com.theminimalismhub.moneymanagement.feature_bills.presentation.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.theminimalismhub.jobmanagerv2.utils.Dater
import com.theminimalismhub.moneymanagement.core.utils.Colorer
import com.theminimalismhub.moneymanagement.core.utils.Currencier
import com.theminimalismhub.moneymanagement.feature_accounts.presentation.composables.getAccountIcon
import com.theminimalismhub.moneymanagement.feature_bills.domain.model.Bill
import java.text.SimpleDateFormat
import java.util.Date

@SuppressLint("SimpleDateFormat")
@Composable
fun BillCard(
    bill: Bill,
    currency: String = "RSD",
    onEdit: () -> Unit,
    onPay: (Bill) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEdit() }
            .alpha(if (bill.bill.isLastMonthPaid) 0.5f else 1f)
            .padding(start = 24.dp, end = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                modifier = Modifier
                    .alpha(0.85f)
                    .padding(start = 2.dp),
                text = bill.bill.name,
                style = MaterialTheme.typography.h4
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .widthIn(min = 23.dp)
                        .height(23.dp)
                        .background(Colorer.getAdjustedDarkColor(bill.category.color), RoundedCornerShape(100))
                ) {
                    Row {
                        Spacer(modifier = Modifier.width(22.dp))
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .offset(y = (1.5).dp)
                                .alpha(0.85f)
                                .border(
                                    (1.5).dp,
                                    MaterialTheme.colors.background,
                                    RoundedCornerShape(19.dp)
                                )
                                .background(Color.Transparent),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = getAccountIcon(bill.account.type),
                                contentDescription = "Account Type Icon",
                                tint = MaterialTheme.colors.background,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(2.dp))
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${Currencier.formatAmount(bill.bill.amount)} $currency",
                    style = MaterialTheme.typography.h3.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colors.primary
                )
            }
        }
        Row(horizontalArrangement = Arrangement.End) {
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    modifier = Modifier.alpha(0.75f),
                    text = "Payment Date",
                    style = MaterialTheme.typography.body2,
                )
                Text(
                    text = SimpleDateFormat("EEEE dd").format(Date(bill.bill.due)),
                    style = MaterialTheme.typography.h3,
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            FilledTonalIconButton(
                onClick = { onPay(bill) },
                enabled = !bill.bill.isLastMonthPaid,
                colors = IconButtonDefaults.filledTonalIconButtonColors(
                    containerColor = MaterialTheme.colors.secondaryVariant,
                    contentColor = MaterialTheme.colors.onSurface
                )
            ) {
                Icon(imageVector = if(!bill.bill.isLastMonthPaid) Icons.Default.AttachMoney else Icons.Default.Check, contentDescription = "Pay")
            }
        }
    }
}