package ra.securitycam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.longdo.mjpegviewer.MjpegView;

import me.pushy.sdk.Pushy;


public class MainActivity extends AppCompatActivity {
    private static final String MY_PREFS_NAME = "SECURITY_CAM_PREFS";
    MjpegView viewer;
    private boolean started = false;
    String ip = "";
    String url_on, url_off;
    String url_tilt_up, url_tilt_down, url_pan_left, url_pan_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        if (!Pushy.isRegistered(getApplicationContext())) {
            new RegisterForPushNotificationsAsync().execute();
        } else {
            Pushy.listen(this);
        }

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        ip = prefs.getString("ip", "");

        url_off = "http://" + ip + "/test_gpio_off.php";
        url_on = "http://" + ip + "/test_gpio_on.php";

        url_tilt_up = "http://" + ip + "/tilt_up.php";
        url_tilt_down = "http://" + ip + "/tilt_down.php";
        url_pan_left = "http://" + ip + "/pan_left.php";
        url_pan_right = "http://" + ip + "/pan_right.php";


        viewer = findViewById(R.id.mjpegview);
        viewer.setMode(MjpegView.MODE_FIT_WIDTH);
        viewer.setAdjustHeight(true);
        viewer.setUrl("http://" + ip + ":8080/stream/video.mjpeg");

        viewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (started) {
                    viewer.stopStream();
                    started = false;
                } else {
                    viewer.startStream();
                    started = true;
                }
            }
        });

        Button bLock = findViewById(R.id.bLock);
        bLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeGetRequest(url_off);
            }
        });

        Button bUnlock = findViewById(R.id.bUnlock);
        bUnlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeGetRequest(url_on);
            }
        });

        ImageView ivJoystick = findViewById(R.id.ivJoystick);
        ivJoystick.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float w = view.getWidth();
                float h = view.getHeight();
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    float x = motionEvent.getX() * 2 / w - 1;
                    float y = motionEvent.getY() * 2 / h - 1;
                    if (x > -0.5 && x < 0.5 && y > -1 && y < -0.5) {
                        makeGetRequest(url_tilt_up);
                    } else if (x > -1 && x < -0.5 && y > -0.5 && y < 0.5) {
                        makeGetRequest(url_pan_left);
                    } else if (x > 0.5 && x < 1 && y > -0.5 && y < 0.5) {
                        makeGetRequest(url_pan_right);
                    } else if (x > -0.5 && x < 0.5 && y > 0.5 && y < 1) {
                        makeGetRequest(url_tilt_down);
                    }
                }
                return false;
            }
        });
    }

    private void makeGetRequest(String url) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewer.startStream();
        started = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewer.stopStream();
        started = false;
    }

    private class RegisterForPushNotificationsAsync extends AsyncTask<Void, Void, Exception> {
        ProgressDialog mLoading;

        public RegisterForPushNotificationsAsync() {
            // Create progress dialog and set it up
            mLoading = new ProgressDialog(MainActivity.this);
            mLoading.setMessage(getString(R.string.registeringDevice));
            mLoading.setCancelable(false);

            // Show it
            mLoading.show();
        }

        protected Exception doInBackground(Void... params) {
            try {
                // Assign a unique token to this device
                String deviceToken = Pushy.register(getApplicationContext());

                // Log it for debugging purposes
                Log.d("MyApp", "Pushy device token: " + deviceToken);
            } catch (Exception exc) {
                // Return exc to onPostExecute
                return exc;
            }

            // Success
            return null;
        }

        @Override
        protected void onPostExecute(Exception exc) {
            // Hide progress bar
            mLoading.dismiss();

            // Failed?
            if (exc != null) {
                // Show error as toast message
                Toast.makeText(getApplicationContext(), exc.toString(), Toast.LENGTH_LONG).show();
                return;
            }

            // Succeeded
            new SubscribeForPushNotificationsAsync().execute();
            Pushy.listen(getApplicationContext());
        }
    }

    private class SubscribeForPushNotificationsAsync extends AsyncTask<Void, Void, Exception> {
        ProgressDialog mLoading;

        public SubscribeForPushNotificationsAsync() {
            // Create progress dialog and set it up
            mLoading = new ProgressDialog(MainActivity.this);
            mLoading.setMessage(getString(R.string.subscribingDevice));
            mLoading.setCancelable(false);

            // Show it
            mLoading.show();
        }

        protected Exception doInBackground(Void... params) {
            try {
                Pushy.subscribe("broadcast", getApplicationContext());
            } catch (Exception exc) {
                return exc;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Exception exc) {
            // Hide progress bar
            mLoading.dismiss();

            if (exc != null) {
                Toast.makeText(getApplicationContext(), exc.toString(), Toast.LENGTH_LONG).show();
                return;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                finish();
                return true;
            case R.id.action_log:
                startActivity(new Intent(MainActivity.this, LogActivity.class));
                finish();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
