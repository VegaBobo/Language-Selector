package vegabobo.languageselector.ui.screen.main

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rikka.shizuku.Shizuku
import javax.inject.Inject

@HiltViewModel
class MainScreenVm @Inject constructor(
    val app: Application
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainScreenState())
    val uiState: StateFlow<MainScreenState> = _uiState.asStateFlow()

    fun refreshShizukuAvail() {
        val isAvail = Shizuku.pingBinder() &&
                Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED
        if (isAvail)
            _uiState.update { it.copy(isShizukuAvail = true) }
    }

    init {
        fillListOfApps()
        refreshShizukuAvail()
    }

    fun fillListOfApps(getAlsoSystemApps: Boolean = false) {
        _uiState.value.listOfApps.clear()
        viewModelScope.launch(Dispatchers.IO) {
            val packageList = getInstalledPackages(getAlsoSystemApps).map { it }
            val sortedList = packageList.sortedBy { app.packageManager.getLabel(it).lowercase() }
            _uiState.value.listOfApps.addAll(sortedList)
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun getInstalledPackages(getAlsoSystemApps: Boolean = false): List<ApplicationInfo> {
        return app.packageManager.getInstalledApplications(
            PackageManager.ApplicationInfoFlags.of(0)
        ).mapNotNull {
            if (getAlsoSystemApps)
                it
            else
                if (it.flags and ApplicationInfo.FLAG_SYSTEM == 0)
                    it
                else
                    null

        }
    }

    fun toggleDropdown() {
        val newDropdownVisibility = !uiState.value.isDropdownVisible
        _uiState.update { it.copy(isDropdownVisible = newDropdownVisibility) }
    }

    fun onClickToggleSystemApps() {
        val newShowSystemApps = !uiState.value.isShowingSystemApps
        _uiState.update { it.copy(isLoading = true, isShowingSystemApps = newShowSystemApps) }
        fillListOfApps(newShowSystemApps)
        toggleDropdown()
    }

    fun onClickProceedShizuku() {
        refreshShizukuAvail()
    }

    fun toggleSearch() {
        val newSearchVisibility = !uiState.value.isSearchVisible
        if (!newSearchVisibility)
            _uiState.update { it.copy(searchQuery = "") }
        _uiState.update { it.copy(isSearchVisible = newSearchVisibility) }
    }

    fun onSearchTextFieldChange(newText: String) {
        _uiState.update { it.copy(searchQuery = newText) }
    }

}