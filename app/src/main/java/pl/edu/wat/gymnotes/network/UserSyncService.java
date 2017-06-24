package pl.edu.wat.gymnotes.network;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.logging.Level;
import java.util.logging.Logger;

public class UserSyncService extends Service {

    private Logger logger = Logger.getLogger(UserSyncService.class.toString());

    private static final Object sSyncAdapterLock = new Object();
    private static UserSyncAdapter userSyncAdapter = null;

    @Override
    public void onCreate() {
        logger.log(Level.INFO, "onCreate");
        synchronized (sSyncAdapterLock) {
            if (userSyncAdapter == null) {
                userSyncAdapter = new UserSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return userSyncAdapter.getSyncAdapterBinder();
    }
}
