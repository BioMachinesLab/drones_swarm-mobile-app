package pt.iscte.drones_swarm_mobile_app.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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

        addListenerOnSpinnerItemSelection();
        interactions();
    }

    public void addListenerOnSpinnerItemSelection() {
        Spinner spinner_configure = (Spinner) findViewById(R.id.spinner_configure);

        List<String> list = new ArrayList<String>();
        list.add("list 1");
        list.add("list 2");
        list.add("list 3");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_configure.setAdapter(dataAdapter);
        spinner_configure.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    private void interactions(){
        ImageButton button1 = (ImageButton) findViewById(R.id.button_1);

        button1.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                Toast.makeText(ConfigureActivity.this, "Entrou botao 1", Toast.LENGTH_SHORT).show();

            }
        });

        EditText editTextIP = (EditText) findViewById(R.id.textView_ip_configure_activity);
        editTextIP.setText( "192.168.2.1");

    }
    public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
            Toast.makeText(parent.getContext(),
                    "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
