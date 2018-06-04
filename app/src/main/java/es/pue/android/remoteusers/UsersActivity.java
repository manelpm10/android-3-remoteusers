package es.pue.android.remoteusers;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import es.pue.android.remoteusers.model.User;

public class UsersActivity extends AppCompatActivity {

    private final String USERS_URL = "http://jsonplaceholder.typicode.com/users";
    private List<User> users = null;
    private ListView lvUsers;
    private Button btLoadUsers;
    private UsersTask task;

    /**
     * We will use AsyncTask to Application Not Responding (ANR) error that happens when
     * Java main thread is waiting for >5s.
     */

    private class UsersTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                InputStream is = con.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                String usersJsonString = "";
                int data = isr.read();
                while (data != -1) {
                    char current = (char) data;
                    usersJsonString = usersJsonString + current;
                    data = isr.read();
                }

                return usersJsonString;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String usersJsonString) {
            parseData(usersJsonString);
        }
    }

    private void parseData(String usersJsonString) {
        try {
            JSONArray usersJson = new JSONArray(usersJsonString);
            for (int i=0; i < usersJson.length(); i++) {
                JSONObject userJson = usersJson.getJSONObject(i);
                if (null != userJson) {
                    users.add(new User(
                            userJson.getInt("id"),
                            userJson.getString("username"),
                            userJson.getString("email")
                    ));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        // Instantiate AsyncTask who will manage the asynchronous load of users.
        task = new UsersTask();
        users = new ArrayList<>();

        lvUsers = findViewById(R.id.lvUsers);
        btLoadUsers = findViewById(R.id.btnLoadUsers);
        btLoadUsers.setOnClickListener(getClickLoadUsersListener());
    }

    @NonNull
    private View.OnClickListener getClickLoadUsersListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task.execute(USERS_URL);
            }
        };
    }
}
