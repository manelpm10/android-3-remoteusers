package es.pue.android.remoteusers;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.ExecutionException;

public class UserService extends Service {
    public UserService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Not implemented because we don't want external applications can bind to this service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        UsersTask usersTask = new UsersTask();
        String url = intent.getStringExtra("url");
        try {
            String userJson = usersTask.execute(url).get();
            Intent i = new Intent("es.pue.android.remoteusers.JSON");
            i.putExtra("userJson", userJson);
            sendBroadcast(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return START_NOT_STICKY;
    }
}
