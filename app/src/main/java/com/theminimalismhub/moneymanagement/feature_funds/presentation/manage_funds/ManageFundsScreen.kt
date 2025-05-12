package com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.Text
import androidx.compose.material.primarySurface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import com.ramcosta.composedestinations.annotation.Destination
import com.theminimalismhub.moneymanagement.core.composables.ActionChip
import com.theminimalismhub.moneymanagement.core.composables.CancelableFAB
import com.theminimalismhub.moneymanagement.core.composables.ScreenHeader
import com.theminimalismhub.moneymanagement.core.composables.SelectableChip
import com.theminimalismhub.moneymanagement.core.enums.AccountType
import com.theminimalismhub.moneymanagement.core.transitions.BaseTransition
import com.theminimalismhub.moneymanagement.core.utils.Colorer
import com.theminimalismhub.moneymanagement.core.utils.Currencier
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_accounts.presentation.composables.AccountChip
import com.theminimalismhub.moneymanagement.feature_accounts.presentation.composables.getAccountIcon
import com.theminimalismhub.moneymanagement.feature_finances.presentation.composables.CategoryChip
import com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds.presentation.CouponContainer

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Destination(style = BaseTransition::class)
@Composable
fun ManageFundsScreen(

) {

    Scaffold(
        floatingActionButton = { CancelableFAB(isExpanded = false)  {
            // TODO Fab Click
        }},
        scaffoldState = rememberScaffoldState()
    ) {
        Column {
            ScreenHeader(
                title = "Manage Funds",
                hint = "Use Funds to budget, save or reserve money!",
                spacerHeight = 48.dp
            )

            CouponContainer(
                segmentsCount = 9,
                strokeColor = MaterialTheme.colors.secondaryVariant
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                ) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "${Currencier.formatAmount(35000)} RSD",
                            style = MaterialTheme.typography.h2.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = MaterialTheme.colors.onBackground
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            modifier = Modifier.alpha(0.7f).padding(bottom = 2.dp),
                            text = "RESERVED",
                            style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Medium)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.alpha(0.7f),
                            text = "FOR",
                            style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Medium)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            modifier = Modifier.alpha(1f),
                            text = "New Glasses",
                            style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Black),
                            color = MaterialTheme.colors.onBackground
                        )
                    }
                    Spacer(modifier = Modifier.height(28.dp))
                    Row {
                        ActionChip(
                            modifier = Modifier,
                            text = "Odeća",
                            textColor = MaterialTheme.colors.background,
                            backgroundColor = Colorer.getAdjustedDarkColor(-3538953),
                            borderThickness = 0.dp,
                            onClick = {},
                            backgroundStrength = 1f
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        AccountChip(
                            account = Account(
                                "Visa",
                                balance = 250000.0,
                                active = true,
                                deleted = false,
                                primary = true,
                                type = AccountType.CARD,
                                description = "",
                                labels = ""
                            )
                        ) { }
                    }
                }
            }

        }
    }
}