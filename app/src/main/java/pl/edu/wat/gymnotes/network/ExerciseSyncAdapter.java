package pl.edu.wat.gymnotes.network;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import pl.edu.wat.gymnotes.R;


public class ExerciseSyncAdapter extends AbstractThreadedSyncAdapter{

    private static Logger logger = Logger.getLogger(ExerciseSyncAdapter.class.toString());

    public static final int SYNC_INTERVAL = 6; //seconds
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;

    ExerciseSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        logger.log(Level.INFO, "Sync started");

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String exercisesJsonStr = null;
        String format = "json";

        try {
            // Construct the URL for the spring query
            Uri builtUri = Uri.parse("http://localhost:8080/rest/exercise/all");
            URL url = new URL(builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return;
            }
            exercisesJsonStr = buffer.toString();
            getExerciseDataFromJson(exercisesJsonStr);

        }
        catch (IOException e) {
            logger.log(Level.INFO, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
        } catch (JSONException e) {
            logger.log(Level.INFO, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    logger.log(Level.INFO, "Error closing stream", e);
                }
            }
        }
    }

    private void getExerciseDataFromJson(String exerciseJsonStr) throws JSONException {
        System.out.println(exerciseJsonStr);
    }

        /**
         * Helper method to get the fake account to be used with SyncAdapter, or make a new one
         * if the fake account doesn't exist yet.  If we make a new account, we call the
         * onAccountCreated method so we can initialize things.
         *
         * @param context The context used to access the account service
         * @return a fake account.
         */
    public static Account getSyncAccount(Context context) {

        logger.log(Level.INFO, "getSyncAccount");
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        Account[] accounts = accountManager.getAccounts();
        logger.log(Level.INFO, "accountManager.getAccounts()1 " + accounts[0].toString());
        onAccountCreated(accounts[0], context);
        return accounts[0];
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Account account, Context context, int syncInterval, int flexTime) {
        logger.log(Level.INFO, "configurePeriodicSync");
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        logger.log(Level.INFO, "onAccountCreated");
        /*
         * Since we've created an account
         */
        ExerciseSyncAdapter.configurePeriodicSync(newAccount, context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(newAccount, context);
    }
    public static void initializeSyncAdapter(Context context) {
        logger.log(Level.INFO, "initializeSyncAdapter");
        getSyncAccount(context);
    }

    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Account account, Context context) {
        logger.log(Level.INFO, "syncImmediately");
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(account,
                context.getString(R.string.content_authority), bundle);
    }

}
