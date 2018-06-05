package es.pue.android.remoteusers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
        /**
         * ViewHolder pattern save memory because reuse the views you see in the screen.
         */
        ViewHolder viewHolder;
        //View view = null;
        if (null == convertView) {
            // 1. Load resource
            convertView = this.inflater.inflate(this.layoutId, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 2. Assign to widget the info for each guest
        /*
        TextView tvId = view.findViewById(R.id.tvId);
        TextView tvUsername = view.findViewById(R.id.tvUsername);
        TextView tvEmail = view.findViewById(R.id.tvEmail);

        tvId.setText(""+user.getId());
        tvUsername.setText(user.getUsername());
        tvEmail.setText(user.getEmail());
        */

        User user = this.users.get(position);
        viewHolder.tvId.setText(""+user.getId());
        viewHolder.tvUsername.setText(user.getUsername());
        viewHolder.tvEmail.setText(user.getEmail());

        // 3. Return view
        return convertView;
    }

    private class ViewHolder {
        final ImageView ivAvatar;
        final TextView tvId;
        final TextView tvUsername;
        final TextView tvEmail;

        public ViewHolder(View v) {
            this.ivAvatar = v.findViewById(R.id.ivAvatar);
            this.tvId = v.findViewById(R.id.tvId);
            this.tvUsername = v.findViewById(R.id.tvUsername);
            this.tvEmail = v.findViewById(R.id.tvEmail);
        }
    }
}
