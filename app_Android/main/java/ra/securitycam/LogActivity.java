package ra.securitycam;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class LogActivity extends AppCompatActivity {

    ListView lvLog;
    private ArrayList<LogData> logDataSet;
    private CustomLogListViewAdapter adapter;
    ProgressDialog progressDialog;

    private static final String MY_PREFS_NAME = "SECURITY_CAM_PREFS";
    String ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        lvLog = findViewById(R.id.lvLog);
        logDataSet = new ArrayList<>();


        /*
        logDataSet.add(new LogData("Gokul", "2019-02-22 10:00:10"));
        logDataSet.add(new LogData("Rijo", "2019-02-22 10:01:24"));
        logDataSet.add(new LogData("Unknown", "2019-02-24 18:06:23"));
        logDataSet.add(new LogData("Unknown", "2019-02-24 18:07:00"));
        logDataSet.add(new LogData("Rijo", "2019-02-24 18:07:59"));
        adapter = new CustomLogListViewAdapter(logDataSet, getApplicationContext());
        lvLog.setAdapter(adapter);
        */


        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        ip = prefs.getString("ip", "");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        DownloadFilesTask downloadFilesTask = new DownloadFilesTask();
        downloadFilesTask.execute();

    }

    private class DownloadFilesTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            downloadRemoteTextFileContent();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            adapter = new CustomLogListViewAdapter(logDataSet, getApplicationContext());
            lvLog.setAdapter(adapter);

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    private void downloadRemoteTextFileContent() {
        URL mUrl = null;
        try {
            mUrl = new URL("http://" + ip + "/db.txt");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            assert mUrl != null;
            URLConnection connection = mUrl.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            while ((line = br.readLine()) != null) {
                Log.d("LINE", line);
                String[] parts = line.split(",");
                if (parts.length == 2)
                    logDataSet.add(new LogData(parts[1].substring(0,1).toUpperCase() + parts[1].substring(1), parts[0].substring(0, parts[0].length() - 7)));
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
