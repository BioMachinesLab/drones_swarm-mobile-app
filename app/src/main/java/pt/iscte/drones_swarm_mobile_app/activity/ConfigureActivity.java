package pt.iscte.drones_swarm_mobile_app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pt.iscte.drones_swarm_mobile_app.R;

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
        EditText editTextIP = (EditText) findViewById(R.id.editText_ip_configure_activity);
        editTextIP.setText( "192.168.2.1");

        Button button_update = (Button) findViewById(R.id.button_update_configure_activity);

        button_update.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                Toast.makeText(ConfigureActivity.this, "Entrou no botao update!", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}