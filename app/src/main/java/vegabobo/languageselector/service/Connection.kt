package vegabobo.languageselector.service

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import vegabobo.languageselector.IUserService

class Connection : ServiceConnection {

    var SERVICE: IUserService? = null
    fun set(service: IUserService?) {
        if (SERVICE == null) {
            SERVICE = service
        }
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        set(IUserService.Stub.asInterface(service))
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        SERVICE = null
    }
}