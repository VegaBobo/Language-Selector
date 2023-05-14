package android.app;

import android.os.Parcel;
import android.os.Parcelable;

public class ActivityManager {

    public static class RunningTaskInfo extends TaskInfo implements Parcelable {
        protected RunningTaskInfo(Parcel in) {
        }

        public static final Creator<RunningTaskInfo> CREATOR = new Creator<RunningTaskInfo>() {
            @Override
            public RunningTaskInfo createFromParcel(Parcel in) {
                return new RunningTaskInfo(in);
            }

            @Override
            public RunningTaskInfo[] newArray(int size) {
                return new RunningTaskInfo[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
        }
    }

    public static int getCurrentUser() {
        return -1;
    }

}
