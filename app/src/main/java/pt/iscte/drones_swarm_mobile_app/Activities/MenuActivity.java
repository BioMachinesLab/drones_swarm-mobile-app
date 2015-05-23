package pt.iscte.drones_swarm_mobile_app.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import pt.iscte.drones_swarm_mobile_app.R;


public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ----------------FULLSCREEN WITH LAYOUT(START)-----------------//
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_menu);
        // ----------------FULLSCREEN WITH LAYOUT(END)-----------------//

        Button buttonStart = (Button) findViewById(R.id.button_start_second_activity);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent main = new Intent(MenuActivity.this, MainActivity.class);
                startActivity(main);
            }
        });

        Button buttonConfigure = (Button) findViewById(R.id.button_configure_second_activity);

        buttonConfigure.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){

                Intent main = new Intent(MenuActivity.this, ConfigureActivity.class);
                startActivity(main);
            }
        });

        Button buttonAbout = (Button) findViewById(R.id.button_about_second_activity);

        buttonAbout.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){

                Intent main = new Intent(MenuActivity.this, AboutActivity.class);
                startActivity(main);
            }
        });
    }
}
