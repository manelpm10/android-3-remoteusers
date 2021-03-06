package es.pue.android.remoteusers;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import es.pue.android.remoteusers.model.User;

public class UsersActivity extends AppCompatActivity {

    private final String USERS_URL = "http://jsonplaceholder.typicode.com/users";
    private List<User> users;
    private ListView lvUsers;
    private UsersTask task;
    private UsersArrayAdapter usersArrayAdaptersAdapter;

    // Volley variables.
    private RequestQueue queue;
    private StringRequest stringRequest = new StringRequest(Request.Method.GET, USERS_URL,
                new Response.Listener<String>() {
        @Override
        public void onResponse(String usersJsonFromQueue) {
            parseDataGson(usersJsonFromQueue);
            lvUsers.setAdapter(usersArrayAdaptersAdapter);
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(UsersActivity.this, R.string.litsErrorLoadingUsers, Toast.LENGTH_SHORT).show();
        }
    });

    // Declare the Receiver, and the IntentFilter with the action. We will register in onResume.
    private DataReceiver dataReceiver = new DataReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            String usersJsonFromService = intent.getStringExtra("userJson");
            parseDataGson(usersJsonFromService);
            lvUsers.setAdapter(usersArrayAdaptersAdapter);
        }
    };
    private IntentFilter iFilter = new IntentFilter("es.pue.android.remoteusers.JSON");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        /**
         * Instantiate AsyncTask who will manage the asynchronous load of users.
         * We can override the onPostExecute method when is instantiated.
         */
        task = new UsersTask() {
            @Override
            protected void onPostExecute(String usersJsonString) {
                parseDataGson(usersJsonString);
                lvUsers.setAdapter(usersArrayAdaptersAdapter);
            }
        };

        users = new ArrayList<>();
        usersArrayAdaptersAdapter = new UsersArrayAdapter(this, R.layout.user_item, users);

        lvUsers = findViewById(R.id.lvUsers);
        lvUsers.setOnItemClickListener(getOnItemEmailListener());

        // Volley library integration.
        queue = Volley.newRequestQueue(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        /**
         * Register the receiver and join with the action we spect.
         */
        registerReceiver(dataReceiver, iFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        /**
         * We unregister to don't use resources when activity is not visible.
         */
        unregisterReceiver(dataReceiver);
    }

    @Override
    /**
     * Link menu to activity.
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int idItem = item.getItemId();
        switch (idItem) {
            case R.id.opLoadUsers:
                //task.execute(USERS_URL);
                queue.add(stringRequest);
                break;
            case R.id.opLoadTasks:
                Intent i = new Intent(this, UserService.class);
                i.putExtra("url", USERS_URL);
                startService(i);
                break;
        }
        return true;
    }

    private void parseDataGson(String usersJsonString) {
        List<User> parsedUsers = new Gson().fromJson(usersJsonString, new TypeToken<ArrayList<User>>(){}.getType());
        users.addAll(parsedUsers);
    }

    /**
     * Not needed after add GSON library
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
     */

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
}
