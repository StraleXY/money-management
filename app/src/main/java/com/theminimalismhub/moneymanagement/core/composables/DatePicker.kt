package com.theminimalismhub.moneymanagement.core.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.ArrowForwardIos
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@Composable
fun DatePicker() {


    var showingMonth by remember { mutableStateOf(Calendar.getInstance()) }
    var selected by remember { mutableStateOf(Calendar.getInstance().apply { timeInMillis = showingMonth.timeInMillis }) }
    var list by remember { mutableStateOf(getDatesList(showingMonth)) }
    val weeks = arrayOf("S", "M", "T", "W", "T", "F", "S")
    val cellSize = 40.dp





    Box(
        modifier = Modifier.fillMaxSize()
            .background(Color(0xFF000000)).noRippleClickable {

            }, contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.width(IntrinsicSize.Max)) {
            Column(modifier = Modifier.fillMaxWidth().background(Color(0xFF018677)).padding(16.dp)) {
                Text(SimpleDateFormat("yyyy", Locale.ENGLISH).format(selected.time), color = Color.White.copy(alpha = .7f))
                Spacer(modifier = Modifier.height(8.dp))
                Text(SimpleDateFormat("EEE, d MMM", Locale.ENGLISH).format(selected.time), color = Color.White, fontSize = 27.sp)
            }

            Column(modifier = Modifier.background(Color(0xFFffffff))) {

                Box(modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, bottom = 12.dp)) {
                    Icon(Icons.Outlined.ArrowBackIosNew, "", tint = Color(0xFF212121), modifier = Modifier.size((16 * 3).dp).align(Alignment.CenterStart).clickable {
                        showingMonth = Calendar.getInstance().apply { timeInMillis = showingMonth.timeInMillis;add(Calendar.MONTH, -1) }
                        list = getDatesList(showingMonth)
                    }.padding(16.dp))
                    Text(SimpleDateFormat("MMM yyyy", Locale.ENGLISH).format(showingMonth.time), color = Color(0xFF212121), modifier = Modifier.align(Alignment.Center))
                    Icon(Icons.Outlined.ArrowForwardIos, "", tint = Color(0xFF212121), modifier = Modifier.size((16 * 3).dp).align(Alignment.CenterEnd).clickable {
                        showingMonth = Calendar.getInstance().apply { timeInMillis = showingMonth.timeInMillis;add(Calendar.MONTH, +1) }
                        list = getDatesList(showingMonth)
                    }.padding(16.dp))

                }




                Row(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
                    weeks.forEach {
                        Text(it, color = Color(0xFF757575), fontSize = 14.sp,textAlign = TextAlign.Center, modifier = Modifier.size(cellSize))
                    }


                }
                list.chunked(7).forEach {
                    Row(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
                        it.forEach {

                            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(cellSize).clickable {
                                if (isSameMonth(it?.first, showingMonth)) {
                                    selected = Calendar.getInstance().apply { timeInMillis = it!!.first!! }
                                }
                            }.drawBehind {
                                drawCircle(
                                    color = if (it?.second == true && isSelected(it!!.first, selected, showingMonth)) Color(0xFF018677) else Color.Transparent,

                                    )
                            }) {
                                Text(
                                    toDate(it?.first), textAlign = TextAlign.Center,fontSize = 14.sp,
                                    color = if (it?.second == true) {
                                        if (isSelected(it!!.first, selected, showingMonth))
                                            Color(0xFFffffff)
                                        else
                                            Color(0xFF757575)

                                    } else if (it?.second == false) Color(0x11757575) else Color(0),
                                )
                            }

                        }
                    }

                }

                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth().padding(end = 16.dp)) {
                    Text("Cancel", color = Color(0xFF018677), modifier = Modifier.clickable {

                    }.padding(16.dp))
                    Text("OK", color = Color(0xFF018677), modifier = Modifier.clickable {

                    }.padding(16.dp))
                }

            }


        }

    }

}


private fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    clickable(indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}



private fun getDatesList(calIncoming: Calendar): ArrayList<Pair<Long, Boolean>?> {

    val cal = Calendar.getInstance().apply { timeInMillis = calIncoming.timeInMillis }
    val list = arrayListOf<Pair<Long, Boolean>?>()
    cal.set(Calendar.DATE, 1)
    val currentMont = cal.get(Calendar.MONTH)
    while (cal.get(Calendar.MONTH) == currentMont) {
        list.add(cal.timeInMillis to true)
        cal.add(Calendar.DATE, 1)
    }
    while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
        list.add(cal.timeInMillis to false)
        cal.add(Calendar.DATE, 1)
    }
    cal.set(Calendar.DATE, 1)
    cal.add(Calendar.MONTH, -1)
    cal.add(Calendar.DATE, -1)
    while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
        list.add(0, cal.timeInMillis to false)
        cal.add(Calendar.DATE, -1)
    }

    if (list.size < 7 * 6) {
        while (list.size < (7 * 6)) {
            list.add(null)
        }
    }

    return list


}


private fun isSelected(first: Long, selected: Calendar, showing: Calendar): Boolean {
    return (selected.get(Calendar.YEAR) == showing.get(Calendar.YEAR) &&
            selected.get(Calendar.MONTH) == showing.get(Calendar.MONTH)
            && selected.get(Calendar.DATE) == Calendar.getInstance().apply {
        timeInMillis = first
    }.get(Calendar.DATE)
            )

}

private fun isSameMonth(first: Long?, showing: Calendar): Boolean {
    return if (first == null) false
    else {
        val cal = Calendar.getInstance().apply {
            timeInMillis = first
        }
        cal.get(Calendar.MONTH) == showing.get(Calendar.MONTH) && cal.get(Calendar.YEAR) == showing.get(Calendar.YEAR)
    }
}

private fun toDate(first: Long?): String {
    if (first == null) return ""
    return SimpleDateFormat("d", Locale.ENGLISH).format(Date(first))
}




/*****
implementation("androidx.compose.foundation:foundation:1.3.1")
implementation("androidx.compose.ui:ui:1.3.2")
implementation("androidx.compose.ui:ui-graphics:1.3.2")
implementation("androidx.compose.material:material-icons-core:1.3.1")
implementation("androidx.compose.material:material:1.3.1")
implementation("androidx.compose.material:material-icons-extended:1.3.1")
 ******/