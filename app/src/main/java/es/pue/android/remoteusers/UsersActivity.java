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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

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

                String result = "";
                int data = isr.read();
                while (data != -1) {
                    char current = (char) data;
                    result = result + current;
                    data = isr.read();
                }

                Log.i("JSON", result);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "Remote users data";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("ASYNCH DATA", s);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        // Instantiate AsyncTask who will manage the asynchronous load of users.
        task = new UsersTask();

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
