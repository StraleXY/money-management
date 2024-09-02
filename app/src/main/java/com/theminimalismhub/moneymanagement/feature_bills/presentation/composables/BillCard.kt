package com.theminimalismhub.moneymanagement.feature_bills.presentation.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.theminimalismhub.jobmanagerv2.utils.Dater
import com.theminimalismhub.moneymanagement.core.utils.Colorer
import com.theminimalismhub.moneymanagement.core.utils.Currencier
import com.theminimalismhub.moneymanagement.feature_bills.domain.model.Bill
import java.text.SimpleDateFormat
import java.util.Date

@SuppressLint("SimpleDateFormat")
@Composable
fun BillCard(
    bill: Bill,
    currency: String = "RSD",
    onEdit: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEdit() }
            .alpha(if(bill.bill.isLastMonthPaid) 0.5f else 1f)
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .height(54.dp)
                    .background(if (bill.category?.color != null) Colorer.getAdjustedDarkColor(bill.category.color) else MaterialTheme.colors.onSurface, RoundedCornerShape(100))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    modifier = Modifier.alpha(0.75f),
                    text = bill.bill.name,
                    style = MaterialTheme.typography.body2
                )
                Text(
                    text = "${Currencier.formatAmount(bill.bill.amount)} $currency",
                    style = MaterialTheme.typography.h3
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
                onClick = { /*TODO*/ },
                enabled = !bill.bill.isLastMonthPaid,
                colors = IconButtonDefaults.filledTonalIconButtonColors(
                    containerColor = MaterialTheme.colors.secondaryVariant,
                    contentColor = MaterialTheme.colors.onSurface
                )
            ) {
                Icon(imageVector = Icons.Default.Check, contentDescription = "Pay")
            }
        }
    }
}