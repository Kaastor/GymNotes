package pl.edu.wat.gymnotes.network;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ExerciseSyncService extends Service {

    private Logger logger = Logger.getLogger(ExerciseSyncService.class.toString());

    private static final Object sSyncAdapterLock = new Object();
    private static ExerciseSyncAdapter exerciseSyncAdapter = null;

    @Override
    public void onCreate() {
        logger.log(Level.INFO, "onCreate");
        synchronized (sSyncAdapterLock) {
            if (exerciseSyncAdapter == null) {
                exerciseSyncAdapter = new ExerciseSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return exerciseSyncAdapter.getSyncAdapterBinder();
    }

}
