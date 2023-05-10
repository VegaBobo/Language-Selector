package vegabobo.languageselector.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LocaleItemList(
    itemText: String,
    onLongClick: () -> Unit = {},
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .combinedClickable(
                onClick = { onClick() },
                onLongClick = { onLongClick() }
            )
            .fillMaxWidth()
            .height(72.dp)
            .padding(18.dp)
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterStart),
            text = itemText,
            fontSize = 19.sp
        )
    }
}