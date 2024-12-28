package com.demo.app.quiz.compose.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


private val colors = listOf(
    Color.Red,
    Color.Yellow,
    Color.Green,
    Color.Magenta,
)

@Composable
fun ColumnScope.Question(
    onOptionSelect: (optionIndex: Int) -> Unit,
    question: String,
    options: List<String>,
    selectedIndex: Int?,
    correctIndex: Int? = null,
) {
    Text(
        text = question,
        fontSize = 28.sp,
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Center,
        lineHeight = 32.sp,
    )

    Spacer(Modifier.weight(1F))

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(options) { i, answer ->
            Answer(
                answer,
                colors[i],
                modifier = Modifier.fillMaxWidth(),
                onClick = { onOptionSelect(i) },
                selected = i == selectedIndex || i == correctIndex,
                isThisCorrect = correctIndex?.let { i == it },
            )
        }
    }
}

@Composable
fun Answer(
    text: String,
    color: Color,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isThisCorrect: Boolean? = null,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        border = if (selected)
            BorderStroke(2.dp, color)
        else
            ButtonDefaults.outlinedButtonBorder()
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
        ) {
            Icon(
                imageVector = when {
                    selected && isThisCorrect == null -> Icons.Default.RemoveCircle
                    selected && isThisCorrect == true -> Icons.Default.CheckCircle
                    selected && isThisCorrect == false -> Icons.Default.AddCircle
                    else -> Icons.Default.Circle
                },
                contentDescription = null,
                tint = color,
                modifier = Modifier.rotate(if (selected && isThisCorrect == false) 45F else 0F)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = text,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

@SuppressWarnings("unused")
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun NumberPane(
    selectedIndex: Int,
    onChange: (Int) -> Unit,
    count: Int,
    modifier: Modifier = Modifier,
) {
    val range = buildList {
        repeat(count) { add(it) }
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        NumberItem(
            { onChange(selectedIndex - 1) },
            modifier = Modifier
                .fillMaxHeight()
                .width(ButtonDefaults.MinHeight)
        ) {
            Icon(Icons.Default.ChevronLeft, null)
        }

        FlowRow(
            modifier = Modifier
                .weight(1F, false)
                .padding(0.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            range.forEach {
                NumberItem(
                    { onChange(it) },
                    modifier = Modifier
                        .height(ButtonDefaults.MinHeight)
                        .aspectRatio(1F, true)
                        .padding(0.dp)
                ) {
                    Text(
                        text = (it + 1).toString(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

            }
        }

        NumberItem(
            { onChange(selectedIndex + 1) },
            modifier = Modifier
                .fillMaxHeight()
                .width(ButtonDefaults.MinHeight)
        ) {
            Icon(Icons.Default.ChevronRight, null)
        }
    }
}

@Composable
private fun NumberItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(8.dp),
    ) {
        content()
    }
}