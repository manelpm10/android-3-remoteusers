package es.pue.android.remoteusers;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * We will use AsyncTask to Application Not Responding (ANR) error that happens when
 * Java main thread is waiting for >5s.
 */
public class UsersTask extends AsyncTask<String, Void, String> {

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
}
