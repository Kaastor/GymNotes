package pl.edu.wat.gymnotes.network;

import android.content.Context;

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
import pl.edu.wat.gymnotes.data.ExerciseDbHelper;
import pl.edu.wat.gymnotes.model.User;



class UserSync{

    private static Logger logger = Logger.getLogger(UserSync.class.toString());

    private Context context;


    UserSync(Context context) {
        this.context = context;
    }

    public void performSync() {
        logger.log(Level.INFO, "Sync started");

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String userJsonStr = null;
        String format = "json";

        try {
            URL url = new URL(context.getResources().getString(R.string.url_get_user_data));
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
            getUserDataFromJson(userJsonStr);

        }
        catch (IOException e) {
            logger.log(Level.INFO, "Error ", e);
        }
        catch (JSONException e) {
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

    private void getUserDataFromJson(String userJsonStr) throws JSONException {
        System.out.println(userJsonStr);

        final String US_LIST = "users";
        final String US_NAME = "name";
        final String US_EMAIL = "email";
        final String US_PASS = "password";

        JSONObject userJson = new JSONObject(userJsonStr);
        JSONArray users = userJson.getJSONArray(US_LIST);

        Vector<User> cVVector = new Vector<>(users.length());

        for (int i=0; i<users.length(); i++) {
            JSONObject user = users.getJSONObject(i);
            String name = user.getString(US_NAME);
            String password = user.getString(US_PASS);
            String email = user.getString(US_EMAIL);

            User newUser = new User(name, email, password);
            cVVector.add(newUser);
        }
        long inserted = 0;
        for( User user : cVVector) {
            ExerciseDbHelper dbHelper = new ExerciseDbHelper(context);
            dbHelper.addUser(user);
        }

        logger.log(Level.INFO, "Inserted: ", inserted);
    }
}
