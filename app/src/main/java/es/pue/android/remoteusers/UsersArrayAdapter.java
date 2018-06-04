package es.pue.android.remoteusers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import es.pue.android.remoteusers.model.User;

public class UsersArrayAdapter extends ArrayAdapter {
    ArrayList<User> users;
    private int layoutId;
    private LayoutInflater inflater;

    public UsersArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<User> users) {
        super(context, resource, users);
        this.users = users;
        this.layoutId = resource;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return this.users.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // 1. Load resource
        View view = this.inflater.inflate(this.layoutId, parent, false);

        // 2. Assign to widget the info for each guest
        TextView tvId = view.findViewById(R.id.tvId);
        TextView tvUsername = view.findViewById(R.id.tvUsername);
        TextView tvEmail = view.findViewById(R.id.tvEmail);

        User user = this.users.get(position);
        tvId.setText(""+user.getId());
        tvUsername.setText(user.getUsername());
        tvEmail.setText(user.getEmail());

        // 3. Return view
        return view;
    }
}
