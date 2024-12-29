package vegabobo.languageselector.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AppInfoDao {
    @Query("SELECT * FROM appinfoentity")
    fun getAll(): List<AppInfoEntity>

    @Query("SELECT * FROM appinfoentity WHERE pkg = :pkg")
    fun findByPkg(pkg: String): AppInfoEntity?

    @Insert
    fun insert(aie: AppInfoEntity)

    @Insert
    fun insertAll(vararg aie: AppInfoEntity)

    @Delete
    fun delete(aie: AppInfoEntity)

    @Query("DELETE FROM appinfoentity")
    fun deleteAll(): Int

    //

    @Query("SELECT (SELECT COUNT(*) FROM appinfoentity) == 0")
    fun isEmpty(): Boolean

    //

    @Query("UPDATE appinfoentity SET last_selected = NULL")
    fun cleanLastSelectedAll()

    @Query("UPDATE appinfoentity SET last_selected = :lastSelected WHERE pkg = :pkg")
    fun setLastSelected(pkg: String, lastSelected: Long)

    @Query("SELECT * FROM appinfoentity WHERE last_selected IS NOT NULL ORDER BY last_selected DESC")
    fun getHistory(): List<AppInfoEntity>
}