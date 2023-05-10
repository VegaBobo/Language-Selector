package vegabobo.languageselector.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

@Composable
fun BackButton(
    onClick: () -> Unit
){
    IconButton(onClick = { onClick() }) {
        Icon(
            imageVector = Icons.Outlined.ArrowBack,
            contentDescription = "Back arrow"
        )
    }
}