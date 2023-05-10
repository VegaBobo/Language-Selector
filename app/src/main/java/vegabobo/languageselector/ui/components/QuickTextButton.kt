package vegabobo.languageselector.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun QuickTextButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: ImageVector,
    text: String
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(18.dp)
            .then(modifier),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(28.dp),
            imageVector = icon,
            contentDescription = text,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.padding(2.dp))
        Text(textAlign = TextAlign.Center, text = text, color = MaterialTheme.colorScheme.primary)
    }

}