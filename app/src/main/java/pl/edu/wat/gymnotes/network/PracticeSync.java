package pl.edu.wat.gymnotes.network;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import pl.edu.wat.gymnotes.R;
import pl.edu.wat.gymnotes.data.ExerciseContract;
import pl.edu.wat.gymnotes.data.ExerciseDbHelper;


class PracticeSync {

    private static Logger logger = Logger.getLogger(PracticeSync.class.toString());

    private Context context;

    PracticeSync(Context context) {
        this.context = context;
    }


    public void performSync() {
        logger.log(Level.INFO, "Sync started");

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String userJsonStr = null;
        String format = "json";

        try {
            URL url = new URL(context.getResources().getString(R.string.url_get_practice_data));
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                logger.log(Level.INFO, "inputStream == null");
                return;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return;
            }
            userJsonStr = buffer.toString();
            getPracticeDataFromJson(userJsonStr);

        } catch (IOException e) {
            logger.log(Level.INFO, "Error ", e);
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

    private void getPracticeDataFromJson(String practiceJsonStr) throws JSONException {
        System.out.println(practiceJsonStr);

        final String PR_LIST = "practices";
        final String PR_USER = "user";
        final String PR_EXERCISE = "exercise";
        final String PR_DATE = "date";
        final String PR_SERIES = "series";
        final String PR_REPS = "reps";

        JSONObject practiceJson = new JSONObject(practiceJsonStr);
        JSONArray practices = practiceJson.getJSONArray(PR_LIST);

        Vector<ContentValues> cVVector = new Vector<>(practices.length());

        for (int i = 0; i < practices.length(); i++) {
            JSONObject practice = practices.getJSONObject(i);
            String user = practice.getString(PR_USER);
            String reps = practice.getString(PR_REPS);
            String date = practice.getString(PR_DATE);
            String series = practice.getString(PR_SERIES);
            String exercise = practice.getString(PR_EXERCISE);

            ContentValues practiceValues = new ContentValues();
            practiceValues.put(ExerciseContract.PracticeEntry.COLUMN_EX_KEY, exercise);
            practiceValues.put(ExerciseContract.PracticeEntry.COLUMN_USER_KEY, user);
            practiceValues.put(ExerciseContract.PracticeEntry.COLUMN_DATE, date);
            practiceValues.put(ExerciseContract.PracticeEntry.COLUMN_SERIES, series);
            practiceValues.put(ExerciseContract.PracticeEntry.COLUMN_REPS, reps);
            cVVector.add(practiceValues);
        }
        long inserted = 0;
        for( ContentValues contentValues : cVVector) {
            ExerciseDbHelper dbHelper = new ExerciseDbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            inserted = db.insertWithOnConflict(ExerciseContract.PracticeEntry.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        }

        logger.log(Level.INFO, "Inserted: ", inserted);
    }
}