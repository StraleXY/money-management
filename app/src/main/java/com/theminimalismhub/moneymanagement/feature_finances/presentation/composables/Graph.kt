package com.theminimalismhub.moneymanagement.feature_finances.presentation.composables

import android.graphics.Paint
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.ColorUtils
import com.theminimalismhub.moneymanagement.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class GraphEntry(
    val value: Double,
    val color: Int,
    val displayedValue: String = if(value == 0.0) "" else value.toInt().toString(),
    val label: String
)

@Composable
fun Graph(
    modifier: Modifier = Modifier,
    totalHeight: Dp = 190.dp,
    earnings: List<GraphEntry>
) {
    fun normalize(x: Double) : Double {
        return Math.max(Math.min(Math.log(x + 0.58) + 0.54, 1.0), 0.0)
    }

    val topPadding = 75.dp
    val bottomPadding = 25.dp
    val zeroOffset = 10.dp
    val graphHeight = totalHeight - topPadding - bottomPadding - zeroOffset
    val animatedHeight = remember { Animatable(0f) }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    var graphEntries by remember { mutableStateOf(earnings) }
    var maxVal by remember { mutableStateOf(earnings.maxOf { it.value }) }
    var renderJob: Job? = null

    LaunchedEffect(earnings) {
        renderJob?.cancel()
        renderJob = coroutineScope.launch {
            animatedHeight.animateTo(
                0f,
                animationSpec = tween(100),
            )
            delay(10)
            maxVal = earnings.maxOf { it.value }
            graphEntries = earnings
            animatedHeight.animateTo(
                graphHeight.value,
                animationSpec = tween(450, easing = EaseOutCubic),
            )
        }
    }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(totalHeight)
    ) {
        val width = size.width
        val height = Dp(animatedHeight.value).toPx()

        val offset = width / (graphEntries.size - 1)

        var prev = Offset(0f, topPadding.value)
        var prevEarning: GraphEntry? = null
        var first = true

        val path = Path().apply {
            graphEntries.forEach { earning ->
                val current = Offset(prev.x + if(first) 0f else offset,
                    (graphHeight.toPx() - (normalize(earning.value / maxVal) * height) + topPadding.toPx()).toFloat()
                )
                if (first) {
                    moveTo(current.x, current.y)
                    first = false
                } else {
                    cubicTo(
                        prev.x + offset/2.6f,  prev.y + (current.y - prev.y)/4,
                        current.x - offset/2.6f, current.y + (prev.y - current.y)/4,
                        current.x, current.y
                    )
                    drawCircle(
                        color = Color(earning.color),
                        center = prev,
                        radius = 3.dp.toPx()
                    )
                    rotate(degrees = -90f, center) {
                        drawIntoCanvas { canvas ->
                            canvas.nativeCanvas.drawText(
                                prevEarning!!.displayedValue,
                                (width - totalHeight.toPx()) / 2 + totalHeight.toPx(),
                                (totalHeight.toPx() - width + 14.sp.toPx()/2) / 2 + prev.x,
                                Paint().apply {
                                    color = prevEarning!!.color
                                    textSize = 14.sp.toPx()
                                    textAlign = Paint.Align.RIGHT
                                    typeface = ResourcesCompat.getFont(context, R.font.economica)
                                }
                            )
                            canvas.nativeCanvas.drawText(
                                prevEarning!!.label,
                                (width - totalHeight.toPx() + bottomPadding.toPx()) / 2,
                                (totalHeight.toPx() - width + 13.sp.toPx()/2) / 2 + prev.x,
                                Paint().apply {
                                    color = if(prevEarning!!.value == 0.0) ColorUtils.setAlphaComponent(
                                        Color.White.toArgb(), 150) else prevEarning!!.color
                                    textSize = 13.sp.toPx()
                                    textAlign = Paint.Align.RIGHT
                                    typeface = if(prevEarning!!.value == 0.0) ResourcesCompat.getFont(context, R.font.tw_extra_light) else ResourcesCompat.getFont(context, R.font.tw_regular)
                                }
                            )
                        }
                    }
                }
                prev = Offset(current.x, current.y)
                prevEarning = earning
            }
            drawCircle(
                color = Color(prevEarning!!.color),
                center = prev,
                radius = 3.dp.toPx()
            )
            rotate(degrees = -90f, center) {
                drawIntoCanvas { canvas ->
                    canvas.nativeCanvas.drawText(
                        prevEarning!!.displayedValue,
                        (width - totalHeight.toPx()) / 2 + totalHeight.toPx(),
                        (totalHeight.toPx() - width + 14.sp.toPx()/2) / 2 + prev.x,
                        Paint().apply {
                            color = prevEarning!!.color
                            textSize = 14.sp.toPx()
                            textAlign = Paint.Align.RIGHT
                            typeface = ResourcesCompat.getFont(context, R.font.economica)
                        }
                    )
                    canvas.nativeCanvas.drawText(
                        prevEarning!!.label,
                        (width - totalHeight.toPx() + bottomPadding.toPx()) / 2,
                        (totalHeight.toPx() - width + 13.sp.toPx()/2) / 2 + prev.x,
                        Paint().apply {
                            color = if(prevEarning!!.value == 0.0) ColorUtils.setAlphaComponent(
                                Color.White.toArgb(), 115) else prevEarning!!.color
                            textSize = 13.sp.toPx()
                            textAlign = Paint.Align.RIGHT
                            typeface = if(prevEarning!!.value == 0.0) ResourcesCompat.getFont(context, R.font.tw_extra_light) else ResourcesCompat.getFont(context, R.font.tw_regular)
                        }
                    )
                }
            }
        }

        prev = Offset(0f, 0f)
        first = true
        val pathFill = Path().apply {
            graphEntries.forEach { earning ->
                val current = Offset(prev.x + if(first) 0f else offset,
                    (graphHeight.toPx() - (normalize(earning.value / maxVal) * height) + topPadding.toPx()).toFloat()
                )
                if (first) {
                    moveTo(current.x, current.y)
                    first = false
                } else {
                    cubicTo(
                        prev.x + offset/2f,  prev.y + (current.y - prev.y)/8,
                        current.x - offset/2f, current.y + (prev.y - current.y)/8,
                        current.x, current.y
                    )
                }
                prev = Offset(current.x, current.y)
            }
            lineTo(width, (zeroOffset + graphHeight + topPadding).toPx())
            lineTo(0f, (zeroOffset + graphHeight + topPadding).toPx())
            close()
        }

        drawPath(
            path = pathFill,
            color = Color(ColorUtils.setAlphaComponent(graphEntries.first().color, 40))
        )
        drawPath(
            path = path,
            color = Color(graphEntries.first().color),
            style = Stroke(1.5.dp.toPx()),
        )
    }
}