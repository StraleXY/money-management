package com.theminimalismhub.moneymanagement.core.composables.ColorWheel

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.github.ajalt.colormath.model.HSV
import com.github.ajalt.colormath.model.RGB
import kotlin.math.*

@Composable
fun HarmonyColorPicker(
    modifier: Modifier = Modifier,
    harmonyMode: ColorHarmonyMode = ColorHarmonyMode.NONE,
    color: HSVColor,
    isBrightnessFixed: Boolean = true,
    value: Float = 1f,
    onColorChanged: (HSVColor) -> Unit
) {
    val updatedColor by rememberUpdatedState(color)
    val updatedOnValueChanged by rememberUpdatedState(onColorChanged)

    LaunchedEffect(value) {
        updatedOnValueChanged(updatedColor.copy(value = value))
    }

    BoxWithConstraints(modifier) {
        Column(
            Modifier
                .padding(16.dp)
                .fillMaxHeight()
                .fillMaxWidth()
        ) {

            HarmonyColorPickerWithMagnifiers(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.8f),
                hsvColor = updatedColor,
                onColorChanged = { updatedOnValueChanged(it) },
                harmonyMode = harmonyMode
            )
            if (isBrightnessFixed) {
                BrightnessBar(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth()
                        .weight(0.2f),
                    onValueChange = { value -> updatedOnValueChanged(updatedColor.copy(value = value)) },
                    currentColor = updatedColor
                )
            }
        }
    }
}

@Composable
internal fun BrightnessBar(
    modifier: Modifier = Modifier,
    onValueChange: (Float) -> Unit,
    currentColor: HSVColor
) {
    Slider(
        modifier = modifier,
        value = currentColor.value,
        onValueChange = {
            onValueChange(it)
        },
        colors = SliderDefaults.colors(
            activeTrackColor = MaterialTheme.colors.primary,
            thumbColor = MaterialTheme.colors.primary
        )
    )
}


@Composable
internal fun HarmonyColorMagnifiers(
    diameterPx: Int,
    hsvColor: HSVColor,
    animateChanges: Boolean,
    currentlyChangingInput: Boolean,
    harmonyMode: ColorHarmonyMode
) {
    val size = IntSize(diameterPx, diameterPx)
    val position = remember(hsvColor, size) {
        positionForColor(hsvColor, size)
    }

    val positionAnimated = remember {
        Animatable(position, typeConverter = Offset.VectorConverter)
    }
    LaunchedEffect(hsvColor, size, animateChanges) {
        if (!animateChanges) {
            positionAnimated.snapTo(positionForColor(hsvColor, size))
        } else {
            positionAnimated.animateTo(
                positionForColor(hsvColor, size),
                animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy)
            )
        }
    }

    val diameterDp = with(LocalDensity.current) {
        diameterPx.toDp()
    }

    val animatedDiameter = animateDpAsState(
        targetValue = if (!currentlyChangingInput) {
            diameterDp * diameterMainColorDragging
        } else {
            diameterDp * diameterMainColor
        }
    )

    hsvColor.getColors(harmonyMode).forEach { color ->
        val positionForColor = remember {
            Animatable(positionForColor(color, size), typeConverter = Offset.VectorConverter)
        }
        LaunchedEffect(color, size, animateChanges) {
            if (!animateChanges) {
                positionForColor.snapTo(positionForColor(color, size))
            } else {
                positionForColor.animateTo(
                    positionForColor(color, size),
                    animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy)
                )
            }
        }
        Magnifier(position = positionForColor.value, color = color, diameter = diameterDp * diameterHarmonyColor)
    }
    Magnifier(position = positionAnimated.value, color = hsvColor, diameter = animatedDiameter.value)
}

@Composable
private fun HarmonyColorPickerWithMagnifiers(
    modifier: Modifier = Modifier,
    hsvColor: HSVColor,
    onColorChanged: (HSVColor) -> Unit,
    harmonyMode: ColorHarmonyMode = ColorHarmonyMode.NONE
) {
    val hsvColorUpdated by rememberUpdatedState(hsvColor)
    BoxWithConstraints(
        modifier = modifier
            .defaultMinSize(minWidth = 48.dp)
            .wrapContentSize()
            .aspectRatio(1f, matchHeightConstraintsFirst = true)

    ) {
        val updatedOnColorChanged by rememberUpdatedState(onColorChanged)
        val diameterPx by remember(constraints.maxWidth) {
            mutableStateOf(constraints.maxWidth)
        }

        var animateChanges by remember {
            mutableStateOf(false)
        }
        var currentlyChangingInput by remember {
            mutableStateOf(false)
        }

        fun updateColorWheel(newPosition: Offset, animate: Boolean) {
            // Work out if the new position is inside the circle we are drawing, and has a
            // valid color associated to it. If not, keep the current position
            val newColor = colorForPosition(newPosition, IntSize(diameterPx, diameterPx), hsvColorUpdated.value)
            if (newColor != null) {
                animateChanges = animate
                updatedOnColorChanged(newColor)
            }
        }

        val inputModifier = Modifier.pointerInput(diameterPx) {
            forEachGesture {
                awaitPointerEventScope {
                    val down = awaitFirstDown(false)
                    currentlyChangingInput = true
                    updateColorWheel(down.position, animate = true)
                    drag(down.id) { change ->
                        updateColorWheel(change.position, animate = false)
                        change.consumePositionChange()
                    }
                    currentlyChangingInput = false
                }
            }
        }

        Box(inputModifier.fillMaxSize()) {
            ColorWheel(hsvColor = hsvColor, diameter = diameterPx)
            HarmonyColorMagnifiers(
                diameterPx,
                hsvColor,
                animateChanges,
                currentlyChangingInput,
                harmonyMode
            )
        }
    }
}

enum class ColorHarmonyMode {
    NONE,
    COMPLEMENTARY,
    ANALOGOUS,
    SPLIT_COMPLEMENTARY,
    TRIADIC,
    TETRADIC,
    MONOCHROMATIC,
    SHADES;
}

@Composable
private fun ColorWheel(
    hsvColor: HSVColor,
    diameter: Int
) {
    val saturation = 1.0f
    val value = hsvColor.value

    val radius = diameter / 2f
    val alpha = 1.0f
    val colorSweepGradientBrush = remember(hsvColor.value, diameter) {
        val wheelColors = arrayOf(
            HSVColor(0f, saturation, value, alpha),
            HSVColor(60f, saturation, value, alpha),
            HSVColor(120f, saturation, value, alpha),
            HSVColor(180f, saturation, value, alpha),
            HSVColor(240f, saturation, value, alpha),
            HSVColor(300f, saturation, value, alpha),
            HSVColor(360f, saturation, value, alpha)
        ).map {
            it.toColor()
        }
        Brush.sweepGradient(wheelColors, Offset(radius, radius))
    }
    val saturationGradientBrush = remember(diameter) {
        Brush.radialGradient(
            listOf(Color.White, Color.Transparent),
            Offset(radius, radius),
            radius,
            TileMode.Clamp
        )
    }
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(colorSweepGradientBrush)
        drawCircle(saturationGradientBrush)
        drawCircle(
            hsvColor.copy(
                hue = 0f,
                saturation = 0f
            ).toColor(),
            blendMode = BlendMode.Modulate
        )
    }
}

@Composable
internal fun Magnifier(position: Offset, color: HSVColor, diameter: Dp) {
    val offset = with(LocalDensity.current) {
        Modifier.offset(
            position.x.toDp() - diameter / 2,
            // Align with the center of the selection circle
            position.y.toDp() - diameter / 2
        )
    }

    Column(offset.size(width = diameter, height = diameter)) {
        MagnifierSelectionCircle(Modifier.size(diameter), color)
    }
}

@Composable
private fun MagnifierSelectionCircle(modifier: Modifier, color: HSVColor) {
    Surface(
        modifier,
        shape = CircleShape,
        elevation = 4.dp,
        color = color.toColor(),
        border = BorderStroke(2.dp, SolidColor(Color.White)),
        content = {}
    )
}

internal fun positionForColor(color: HSVColor, size: IntSize): Offset {
    val radians = color.hue.toRadian()
    val phi = color.saturation
    val x: Float = ((phi * cos(radians)) + 1) / 2f
    val y: Float = ((phi * sin(radians)) + 1) / 2f
    return Offset(
        x = (x * size.width),
        y = (y * size.height)
    )
}

private const val diameterHarmonyColor = 0.10f
private const val diameterMainColorDragging = 0.18f
private const val diameterMainColor = 0.15f


private fun colorForPosition(position: Offset, size: IntSize, value: Float): HSVColor? {
    val centerX: Double = size.width / 2.0
    val centerY: Double = size.height / 2.0
    val radius: Double = min(centerX, centerY)
    val xOffset: Double = position.x - centerX
    val yOffset: Double = position.y - centerY
    val centerOffset = hypot(xOffset, yOffset)
    val rawAngle = atan2(yOffset, xOffset).toDegree()
    val centerAngle = (rawAngle + 360.0) % 360.0
    return if (centerOffset <= radius) {
        HSVColor(
            hue = centerAngle.toFloat(),
            saturation = (centerOffset / radius).toFloat(),
            value = value,
            alpha = 1.0f
        )
    } else {
        null
    }
}

internal fun Float.toRadian(): Float = this / 180.0f * PI.toFloat()
internal fun Double.toRadian(): Double = this / 180 * PI
internal fun Float.toDegree(): Float = this * 180.0f / PI.toFloat()
internal fun Double.toDegree(): Double = this * 180 / PI

data class HSVColor(
    val hue: Float,
    val saturation: Float,
    val value: Float,
    val alpha: Float
) {

    fun toColor(): Color {
        val hsv = HSV(hue, saturation, value, alpha)
        val rgb = hsv.toSRGB()
        return Color(rgb.redInt, rgb.greenInt, rgb.blueInt, rgb.alphaInt)
    }

    fun getComplementaryColor(): List<HSVColor> {
        return listOf(
            this.copy(saturation = (saturation + 0.1f).coerceAtMost(1f), value = (value + 0.3f).coerceIn(0.0f, 1f)),
            this.copy(saturation = (saturation - 0.1f).coerceAtMost(1f), value = (value - 0.3f).coerceIn(0.0f, 1f)),
            this.copy(hue = (hue + 180) % 360), // actual complementary
            this.copy(hue = (hue + 180) % 360, saturation = (saturation + 0.2f).coerceAtMost(1f), value = (value - 0.3f).coerceIn(0.0f, 1f))
        )
    }

    fun getSplitComplementaryColors(): List<HSVColor> {
        return listOf(
            this.copy(hue = (hue + 150) % 360, saturation = (saturation - 0.05f).coerceIn(0.0f, 1f), value = (value - 0.3f).coerceIn(0.0f, 1f)),
            this.copy(hue = (hue + 210) % 360, saturation = (saturation - 0.05f).coerceIn(0.0f, 1f), value = (value - 0.3f).coerceIn(0.0f, 1f)),
            this.copy(hue = (hue + 150) % 360), // actual
            this.copy(hue = (hue + 210) % 360) // actual
        )
    }

    fun getTriadicColors(): List<HSVColor> {
        return listOf(
            this.copy(hue = (hue + 120) % 360, saturation = (saturation - 0.05f).coerceIn(0.0f, 1f), value = (value - 0.3f).coerceIn(0.0f, 1f)),
            this.copy(hue = (hue + 120) % 360),
            this.copy(hue = (hue + 240) % 360, saturation = (saturation - 0.05f).coerceIn(0.0f, 1f), value = (value - 0.3f).coerceIn(0.0f, 1f)),
            this.copy(hue = (hue + 240) % 360)
        )
    }

    fun getTetradicColors(): List<HSVColor> {
        return listOf(
            this.copy(saturation = (saturation + 0.2f).coerceIn(0.0f, 1f)), // bonus one
            this.copy(hue = (hue + 90) % 360),
            this.copy(hue = (hue + 180) % 360),
            this.copy(hue = (hue + 270) % 360)
        )
    }

    fun getAnalagousColors(): List<HSVColor> {
        return listOf(
            this.copy(hue = (hue + 30) % 360),
            this.copy(hue = (hue + 60) % 360),
            this.copy(hue = (hue + 90) % 360),
            this.copy(hue = (hue + 120) % 360)
        )
    }

    fun getMonochromaticColors(): List<HSVColor> {
        return listOf(
            this.copy(saturation = (saturation + 0.2f).mod(1f)),
            this.copy(saturation = (saturation + 0.4f).mod(1f)),
            this.copy(saturation = (saturation + 0.6f).mod(1f)),
            this.copy(saturation = (saturation + 0.8f).mod(1f))
        )
    }

    fun getShadeColors(): List<HSVColor> {
        return listOf(
            this.copy(value = (value - 0.10f).mod(1.0f).coerceAtLeast(0.2f)),
            this.copy(value = (value + 0.55f).mod(1.0f).coerceAtLeast(0.55f)),
            this.copy(value = (value + 0.30f).mod(1.0f).coerceAtLeast(0.3f)),
            this.copy(value = (value + 0.05f).mod(1.0f).coerceAtLeast(0.2f))
        )
    }

    fun getColors(colorHarmonyMode: ColorHarmonyMode): List<HSVColor> {
        return when (colorHarmonyMode) {
            ColorHarmonyMode.NONE -> emptyList()
            ColorHarmonyMode.COMPLEMENTARY -> getComplementaryColor()
            ColorHarmonyMode.ANALOGOUS -> getAnalagousColors()
            ColorHarmonyMode.SPLIT_COMPLEMENTARY -> getSplitComplementaryColors()
            ColorHarmonyMode.TRIADIC -> getTriadicColors()
            ColorHarmonyMode.TETRADIC -> getTetradicColors()
            ColorHarmonyMode.MONOCHROMATIC -> getMonochromaticColors()
            ColorHarmonyMode.SHADES -> getShadeColors()
        }
    }

    companion object {

        val DEFAULT = HSVColor(360f, 1.0f, 1.0f, 1.0f)

        /**
         *  the color math hsv to local hsv color
         */
        private fun HSV.toColor(): HSVColor {
            return HSVColor(
                hue = if (this.h.isNaN()) 0f else this.h,
                saturation = this.s,
                value = this.v,
                alpha = this.alpha
            )
        }

        fun from(color: Color): HSVColor {
            return RGB(
                color.red,
                color.green,
                color.blue,
                color.alpha
            ).toHSV().toColor()
        }

        val Saver: Saver<HSVColor, *> = listSaver(
            save = {
                listOf(
                    it.hue,
                    it.saturation,
                    it.value,
                    it.alpha
                )
            },
            restore = {
                HSVColor(
                    it[0],
                    it[1],
                    it[2],
                    it[3]
                )
            }
        )
    }
}
