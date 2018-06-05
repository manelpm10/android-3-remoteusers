package es.pue.android.remoteusers;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
    private ArrayList<User> users = null;
    private ListView lvUsers;
    private UsersTask task;
    private UsersArrayAdapter usersArrayAdaptersAdapter;

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
            lvUsers.setAdapter(usersArrayAdaptersAdapter);
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
        Button btLoadUsers = findViewById(R.id.btnLoadUsers);
        btLoadUsers.setOnClickListener(getClickLoadUsersListener());

        usersArrayAdaptersAdapter = new UsersArrayAdapter(this, R.layout.user_item, users);

        lvUsers.setOnItemClickListener(getOnItemEmailListener());
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

    @NonNull
    private AdapterView.OnItemClickListener getOnItemEmailListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/html");
                i.putExtra(Intent.EXTRA_EMAIL, users.get(position).getEmail());
                startActivity(i);
            }
        };
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
