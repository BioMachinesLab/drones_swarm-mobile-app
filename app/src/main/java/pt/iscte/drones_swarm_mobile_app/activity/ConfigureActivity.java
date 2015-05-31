package pt.iscte.drones_swarm_mobile_app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pt.iscte.drones_swarm_mobile_app.R;
import pt.iscte.drones_swarm_mobile_app.network.ServerHandler;

public class ConfigureActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ----------------FULLSCREEN WITH LAYOUT(START)-----------------//
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_configure);
        // ----------------FULLSCREEN WITH LAYOUT(END)-----------------//

        interactions();
    }


    private void interactions(){

        final EditText editTextIP = (EditText) findViewById(R.id.editText_ip_configure_activity);
        editTextIP.setText("192.168.3.250");

        editTextIP.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                validateIP(editTextIP, s);
            }
        });


        final EditText editTextPort = (EditText) findViewById(R.id.editText_port_configure_activity);
        editTextPort.setText("10110");

        editTextPort.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }


            @Override
            public void afterTextChanged(Editable s) {
                validatePort(editTextPort, s);
            }
        });


        Button button_update = (Button) findViewById(R.id.button_update_configure_activity);

        button_update.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                    if (validateIP(editTextIP, editTextIP.getText()) && validatePort(editTextPort, editTextPort.getText())) {
                        Toast.makeText(ConfigureActivity.this, "Data updated!", Toast.LENGTH_SHORT).show();
                        ServerHandler.setServerIP(editTextIP.getText().toString());
                        ServerHandler.setPort(Integer.parseInt(editTextPort.getText().toString()));
                        finish();
                    }
                    else
                        Toast.makeText(ConfigureActivity.this, "Verify the input values!", Toast.LENGTH_SHORT).show();

            }
        });

    }
    private boolean validateIP(EditText editTextIP, Editable s){
        if (Patterns.IP_ADDRESS.matcher(s).matches()) {
            editTextIP.setBackgroundDrawable(getResources().getDrawable(R.drawable.borders_about_configure_activity));
            return true;
        } else {
            editTextIP.setBackgroundDrawable(getResources().getDrawable(R.drawable.fields_invalid_configure_activity));
            return false;
        }
    }
    private boolean validatePort(EditText editTextPort,Editable s){
        if (s.length() == 0 || Integer.valueOf(s.toString()) > 65535) {
            editTextPort.setBackgroundDrawable(getResources().getDrawable(R.drawable.fields_invalid_configure_activity));
            return  false;
        } else {
            editTextPort.setBackgroundDrawable(getResources().getDrawable(R.drawable.borders_about_configure_activity));
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}