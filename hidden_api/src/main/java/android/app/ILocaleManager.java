package android.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.LocaleList;

public interface ILocaleManager extends IInterface {

    // 33
    void setApplicationLocales(String packageName, int userId, LocaleList locales);

    // U
    void setApplicationLocales(String packageName, int userId, LocaleList locales, boolean fromDelegate);
    LocaleList getApplicationLocales(String packageName, int userId);
    LocaleList getSystemLocales();

    abstract class Stub extends Binder implements ILocaleManager {

        public static ILocaleManager asInterface(IBinder obj) {
            throw new UnsupportedOperationException();
        }

    }
}
