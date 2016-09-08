package service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class serDeviceToServerSync extends Service {
    Context mContext;
    public static boolean isServiceRunning = false;
    boolean isSessionExists;

    public serDeviceToServerSync() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
