package ra.securitycam;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {
    private static final String MY_PREFS_NAME = "SECURITY_CAM_PREFS";
    String ip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final EditText etIP = findViewById(R.id.etIP);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        ip = prefs.getString("ip", "");
        etIP.setText(ip);

        Button bSave = findViewById(R.id.bSave);
        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ip = etIP.getText().toString();
                if (ip.matches(""))
                    return;
                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("ip", ip);
                editor.apply();
                startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}
