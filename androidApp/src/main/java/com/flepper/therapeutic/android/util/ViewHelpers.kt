package com.flepper.therapeutic.android.util

import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import com.flepper.therapeutic.android.R
import com.flepper.therapeutic.android.presentation.theme.large_bold_text_size
import com.flepper.therapeutic.android.presentation.widgets.GenericCircleProgressIndicator
import com.flepper.therapeutic.android.presentation.widgets.RegularText
import java.lang.Double.max
import kotlin.math.max

@Composable
fun <T> GenericListViewRenderer(list:List<T>? = null, loadComplete:Boolean, isError:Boolean = false, emptyStateValue:String = stringResource(id =  R.string.no_items_here), content: @Composable () -> Unit){
    Log.e("loadComplete", loadComplete.toString())
    Crossfade(targetState = list){ retrievedList ->
        when{
            isError -> {

            }
            retrievedList == null  -> {
                GenericCircleProgressIndicator()
            }
            loadComplete && retrievedList!!.isEmpty()  -> {
                EmptyStateText(text = emptyStateValue)
            }
            loadComplete -> {
                content()
            }

            //loadComplete and List is not Empty

        }
    }

}

@Composable
fun EmptyStateText(text:String){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        RegularText(
            text = text,
            color = MaterialTheme.colors.onBackground,
            size = large_bold_text_size
        )
    }
}

@Composable
fun SimpleFlowRow(
    modifier: Modifier = Modifier,
    alignment: Alignment.Horizontal = Alignment.Start,
    verticalGap: Dp = 0.dp,
    horizontalGap: Dp = 0.dp,
    content: @Composable () -> Unit
) = Layout(content, modifier) { measurables, constraints ->
    val hGapPx = horizontalGap.roundToPx()
    val vGapPx = verticalGap.roundToPx()

    val rows = mutableListOf<MeasuredRow>()
    val itemConstraints = constraints.copy(minWidth = 0)

    for (measurable in measurables) {
        val lastRow = rows.lastOrNull()
        val placeable = measurable.measure(itemConstraints)

        if (lastRow != null && lastRow.width + hGapPx + placeable.width <= constraints.maxWidth) {
            lastRow.items.add(placeable)
            lastRow.width += hGapPx + placeable.width
            lastRow.height = max(lastRow.height, placeable.height)
        } else {
            val nextRow = MeasuredRow(
                items = mutableListOf(placeable),
                width = placeable.width,
                height = placeable.height
            )

            rows.add(nextRow)
        }
    }

    val width = rows.maxOfOrNull { row -> row.width } ?: 0
    val height = rows.sumBy { row -> row.height } + max(vGapPx.times(rows.size - 1), 0)

    val coercedWidth = width.coerceIn(constraints.minWidth, constraints.maxWidth)
    val coercedHeight = height.coerceIn(constraints.minHeight, constraints.maxHeight)

    layout(coercedWidth, coercedHeight) {
        var y = 0

        for (row in rows) {
            var x = when(alignment) {
                Alignment.Start -> 0
                Alignment.CenterHorizontally -> (coercedWidth - row.width) / 2
                Alignment.End -> coercedWidth - row.width

                else -> throw Exception("unsupported alignment")
            }

            for (item in row.items) {
                item.place(x, y)
                x += item.width + hGapPx
            }

            y += row.height + vGapPx
        }
    }
}

private data class MeasuredRow(
    val items: MutableList<Placeable>,
    var width: Int,
    var height: Int
)

