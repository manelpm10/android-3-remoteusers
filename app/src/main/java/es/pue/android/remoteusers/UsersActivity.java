package es.pue.android.remoteusers;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import es.pue.android.remoteusers.model.User;

public class UsersActivity extends AppCompatActivity {

    private List<User> users = null;
    private ListView lvUsers;
    private Button btLoadUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        lvUsers = findViewById(R.id.lvUsers);
        btLoadUsers = findViewById(R.id.btnLoadUsers);
        btLoadUsers.setOnClickListener(getClickLoadUsersListener());
    }

    @NonNull
    private View.OnClickListener getClickLoadUsersListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
    }
}
