package vegabobo.languageselector.service

import android.content.Intent
import android.os.IBinder
import com.topjohnwu.superuser.ipc.RootService

class RootUserService : RootService() {
    override fun onBind(intent: Intent): IBinder {
        return UserService()
    }
}