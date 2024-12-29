package vegabobo.languageselector.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AppInfoEntity(
    // Package name
    @PrimaryKey @ColumnInfo(name = "pkg") val pkg: String,
    // App name
    @ColumnInfo(name = "name") val name: String,
    // Last time user selected this app, history feature
    @ColumnInfo(name = "last_selected") val lastSelected: Long?,
)