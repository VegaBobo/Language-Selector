package vegabobo.languageselector

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.topjohnwu.superuser.Shell
import com.topjohnwu.superuser.ipc.RootService
import dagger.hilt.android.AndroidEntryPoint
import rikka.shizuku.Shizuku
import vegabobo.languageselector.service.RootUserService
import vegabobo.languageselector.service.UserService
import vegabobo.languageselector.service.UserServiceProvider
import vegabobo.languageselector.ui.screen.Navigation
import vegabobo.languageselector.ui.screen.main.OperationMode
import vegabobo.languageselector.ui.theme.LanguageSelector

object ShizukuArgs {
    val userServiceArgs =
        Shizuku.UserServiceArgs(
            ComponentName(BuildConfig.APPLICATION_ID, UserService::class.java.name),
        )
            .daemon(false)
            .processNameSuffix("service")
            .debuggable(BuildConfig.DEBUG)
            .version(BuildConfig.VERSION_CODE)
}


@AndroidEntryPoint
class MainActivity : ComponentActivity(), Shizuku.OnRequestPermissionResultListener {

    init {
        Shell.enableVerboseLogging = BuildConfig.DEBUG
        Shell.setDefaultBuilder(Shell.Builder.create().setTimeout(10))
    }

    val acRequestCode = 1

    fun bindShizuku() {
        Shizuku.bindUserService(ShizukuArgs.userServiceArgs, UserServiceProvider.connection)
    }

    private val REQUEST_PERMISSION_RESULT_LISTENER = this::onRequestPermissionResult

    override fun onRequestPermissionResult(requestCode: Int, grantResult: Int) {
        if (grantResult == PackageManager.PERMISSION_GRANTED)
            bindShizuku()
    }

    private fun checkPermission(code: Int): Boolean {
        return if (Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED) {
            bindShizuku()
            true
        } else if (Shizuku.shouldShowRequestPermissionRationale()) {
            false
        } else {
            Shizuku.requestPermission(code)
            false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            LanguageSelector { Navigation() }
        }

        if (Shizuku.pingBinder() && savedInstanceState == null) {
            Shizuku.addRequestPermissionResultListener(REQUEST_PERMISSION_RESULT_LISTENER)
            checkPermission(acRequestCode)
        }

        RootReceivedListener.setListener(object : IRootListener {
            override fun onRootReceived() {
                val intent = Intent(application, RootUserService::class.java)
                RootService.bind(intent, UserServiceProvider.connection)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (
            Shizuku.pingBinder() &&
            Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED &&
            !UserServiceProvider.isConnected()
        ) {
            bindShizuku()
        }
    }

    override fun onDestroy() {
        Shizuku.removeRequestPermissionResultListener(REQUEST_PERMISSION_RESULT_LISTENER)
        RootReceivedListener.destroy()
        if (UserServiceProvider.isConnected()) {
            when (UserServiceProvider.opMode) {
                OperationMode.ROOT -> RootService.unbind(UserServiceProvider.connection)
                OperationMode.SHIZUKU -> Shizuku.unbindUserService(
                    ShizukuArgs.userServiceArgs,
                    UserServiceProvider.connection,
                    true
                )

                else -> Log.d(BuildConfig.APPLICATION_ID, "UserService not bound.")
            }
        }
        super.onDestroy()
    }

}