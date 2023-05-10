package vegabobo.languageselector.ui.components

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap

@Composable
fun AppListItem(
    packageName: String,
    appName: String,
    drawable: Drawable,
    onClickApp: () -> Unit
) {
    Row(
        modifier = Modifier.clickable { onClickApp() }.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(48.dp),
            bitmap = drawable.toBitmap().asImageBitmap(),
            contentDescription = "app icon"
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = appName, fontSize = 18.sp, fontWeight = FontWeight.Medium)
            Text(text = packageName, fontSize = 12.sp)
        }
    }
}