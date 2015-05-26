package pt.iscte.drones_swarm_mobile_app.Activities;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import pt.iscte.drones_swarm_mobile_app.R;


public class MainActivity extends ActionBarActivity {


    //Bar Title
    private CharSequence mTitle;
    //Left Menu
    private DrawerLayout mDrawerLayoutLeftMenu;
    private ScrollView mDrawerListLeftMenu;
    private ActionBarDrawerToggle mDrawerToggleLeftMenu;
    private CharSequence mDrawerTitleLeftMenu;
    //Right Menu
    private DrawerLayout mDrawerLayoutRightMenu;
    private ScrollView mDrawerListRightMenu;
    private ActionBarDrawerToggle mDrawerToggleRightMenu;
    private CharSequence mDrawerTitleRightMenu;
    private boolean isOpenRightMenu = false;
    //Google Maps
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    //Control
    private int value_left_control,value_right_control = 0;
    private SeekBar seekBar_left_control1,seekBar_left_control2,seekBar_right_control1,seekBar_right_control2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setUpMapIfNeeded();

        setLeftMenu(savedInstanceState);
        setValuesLeftMenu();

        setRightMenu(savedInstanceState);
        setValuesRightMenu();

        actionButtonsControl();

    }

    private void setLeftMenu(Bundle savedInstanceState) {
        // Initializing

        mTitle = mDrawerTitleLeftMenu = getTitle();
        mDrawerLayoutLeftMenu = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerListLeftMenu = (ScrollView) findViewById(R.id.scrollView_left_menu);

        mDrawerLayoutLeftMenu.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggleLeftMenu = new ActionBarDrawerToggle(this, mDrawerLayoutLeftMenu,
                R.drawable.ic_drawer, R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitleLeftMenu);
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }
        };

        mDrawerLayoutLeftMenu.setDrawerListener(mDrawerToggleLeftMenu);

    }
    private void setValuesLeftMenu(){
        //Drones
        addListenerOnSpinnerItemSelectionLeft_menu_drone();

        //Battery Status
        TextView textView_percentage1 = (TextView) findViewById(R.id.textView_percentage1);
        textView_percentage1.setText("100%");
        TextView textView_percentage2 = (TextView) findViewById(R.id.textView_percentage2);
        textView_percentage2.setText("95%");
        TextView textView_percentage3 = (TextView) findViewById(R.id.textView_percentage3);
        textView_percentage3.setText("3%");

        //Gps Data
        TextView textView_hasfix_details_gps_data = (TextView) findViewById(R.id.textView_hasfix_details_gps_data);
        TextView textView_latitude_details_gps_data = (TextView) findViewById(R.id.textView_latitude_details_gps_data);
        TextView textView_longitude_details_gps_data = (TextView) findViewById(R.id.textView_longitude_details_gps_data);
        TextView textView_velocity_details_gps_data = (TextView) findViewById(R.id.textView_velocity_details_gps_data);
        TextView textView_time_details_gps_data = (TextView) findViewById(R.id.textView_time_details_gps_data);
        TextView textView_satview_details_gps_data = (TextView) findViewById(R.id.textView_satview_details_gps_data);
        TextView textView_satused_details_gps_data = (TextView) findViewById(R.id.textView_satused_details_gps_data);
        TextView textView_hdop_details_gps_data = (TextView) findViewById(R.id.textView_hdop_details_gps_data);
        TextView textView_pdop_details_gps_data = (TextView) findViewById(R.id.textView_pdop_details_gps_data);
        TextView textView_vdop_details_gps_data = (TextView) findViewById(R.id.textView_vdop_details_gps_data);

        //Refresh Rate
        addListenerOnSpinnerItemSelectionLeft_menu_refreshrate();

        //Drone Messages
        TextView textView_drone_messages = (TextView) findViewById(R.id.textView_drone_messages);
        textView_drone_messages.setText("Esta mensagem serve só para testar o comprimento da caixa para ver adapta-se no layout!!");

    }
    public void addListenerOnSpinnerItemSelectionLeft_menu_drone() {
        Spinner spinner_configure = (Spinner) findViewById(R.id.spinner_left_menu_drone);

        List<String> list = new ArrayList<String>();
        list.add("Robot 1");
        list.add("Robot 2");
        list.add("Robot 3");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner_configure.setAdapter(dataAdapter);
        spinner_configure.setOnItemSelectedListener(new CustomOnItemSelectedListenerLeft_menu_drone());
    }
    public class CustomOnItemSelectedListenerLeft_menu_drone implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
            Toast.makeText(parent.getContext(),
                    "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {

        }

    }

    public void addListenerOnSpinnerItemSelectionLeft_menu_refreshrate() {
        Spinner spinner_configure = (Spinner) findViewById(R.id.spinner_left_menu_refresh_rate);

        List<String> list = new ArrayList<String>();
        list.add("0.1 Hz");
        list.add("0.2 Hz");
        list.add("0.3 Hz");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, list);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner_configure.setAdapter(dataAdapter);
        spinner_configure.setOnItemSelectedListener(new CustomOnItemSelectedListenerLeft_menu_refreshrate());
    }

    public class CustomOnItemSelectedListenerLeft_menu_refreshrate implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
            Toast.makeText(parent.getContext(),
                    "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {

        }

    }

    private void setRightMenu(Bundle savedInstanceState) {
        // Initializing

        mTitle = mDrawerTitleRightMenu = getTitle();
        mDrawerLayoutRightMenu = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerListRightMenu = (ScrollView) findViewById(R.id.scrollView_right_menu);

        mDrawerLayoutRightMenu.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.END);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggleRightMenu = new ActionBarDrawerToggle(this, mDrawerLayoutRightMenu,
                R.drawable.ic_drawer, R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to
                //onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitleRightMenu);
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }
        };
        mDrawerLayoutRightMenu.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);//disable swipe por causa dos seekbars
        mDrawerLayoutRightMenu.setDrawerListener(mDrawerToggleRightMenu);

    }
    private void setValuesRightMenu(){
        //Seekbars speed limit and motor offset

        SeekBar seekBar_speed_limit = (SeekBar)findViewById(R.id.seekbar_speed_limit_right_menu);
        final TextView speed_limit_value = (TextView) findViewById(R.id.textView_speed_limit_value);

        seekBar_speed_limit.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {

                speed_limit_value.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekBar.setSecondaryProgress(seekBar.getProgress());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        SeekBar seekBar_motor_offset = (SeekBar)findViewById(R.id.seekbar_motor_offset_right_menu);
        final TextView motor_offset_value = (TextView) findViewById(R.id.textView_motor_offset_value);

        seekBar_motor_offset.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {

                motor_offset_value.setText(String.valueOf(progress - 100));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekBar.setSecondaryProgress(seekBar.getProgress());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    private void actionButtonsControl(){

        //LEFT CONTROL
        seekBar_left_control1 = (SeekBar) findViewById(R.id.seekbar_left_control1);
        seekBar_left_control2 = (SeekBar) findViewById(R.id.seekbar_left_control2);

        final TextView textView_left_control = (TextView)findViewById(R.id.textview_left_control1);
        textView_left_control.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        seekBar_left_control1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                seekBar_left_control2.setProgress(0);
                value_left_control = progress - 100;
                textView_left_control.setText("Left Control \n"+String.valueOf(value_left_control)+ "%");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar_left_control2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                seekBar_left_control1.setProgress(100);
                value_left_control = progress;
                textView_left_control.setText("Left Control \n"+String.valueOf(value_left_control) + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //RIGHT CONTROL

        seekBar_right_control1 = (SeekBar) findViewById(R.id.seekbar_right_control1);
        seekBar_right_control2 = (SeekBar) findViewById(R.id.seekbar_right_control2);

        final TextView textView_right_control = (TextView)findViewById(R.id.textview_right_control1);
        textView_right_control.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        seekBar_right_control1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                seekBar_right_control2.setProgress(0);
                value_right_control = progress - 100;
                textView_right_control.setText("Right Control \n" + String.valueOf(value_right_control) + "%");

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekBar_right_control2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                seekBar_right_control1.setProgress(100);
                value_right_control = progress;
                textView_right_control.setText("Right Control \n" + String.valueOf(value_right_control) + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Commands
        addListenerOnSpinnerItemSelectionRight_menu_commands();

        //Commands Caixa de texto
        EditText editText_commands = (EditText) findViewById(R.id.editText_commands);
        editText_commands.setText("Estou escrevendo aqui mas posso alterar para enviar comandos!");

        //Buttons
        Button button_start_right_menu = (Button) findViewById(R.id.button_start_right_menu);
        button_start_right_menu.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
                Toast.makeText(MainActivity.this,
                        "Button button_start_right_menu",
                        Toast.LENGTH_SHORT).show();
            }
        });

        Button button_stop_right_menu = (Button) findViewById(R.id.button_stop_right_menu);
        button_stop_right_menu.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
                Toast.makeText(MainActivity.this,
                        "Button button_stop_right_menu",
                        Toast.LENGTH_SHORT).show();
            }
        });

        Button button_deploy_right_menu = (Button) findViewById(R.id.button_deploy_right_menu);
        button_deploy_right_menu.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
                Toast.makeText(MainActivity.this,
                        "Button button_deploy_right_menu",
                        Toast.LENGTH_SHORT).show();
            }
        });

        Button button_stop_all_right_menu = (Button) findViewById(R.id.button_stop_all_right_menu);
        button_stop_all_right_menu.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
                Toast.makeText(MainActivity.this,
                        "Button button_stop_all_right_menu",
                        Toast.LENGTH_SHORT).show();
            }
        });

        EditText editText_buttons = (EditText) findViewById(R.id.editText_buttons);
        editText_buttons.setText("Teste Teste Teste");
        Button button_send_log_right_menu = (Button) findViewById(R.id.button_send_log_right_menu);
        button_send_log_right_menu.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
                Toast.makeText(MainActivity.this,
                        "Button button_send_log_right_menu",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void addListenerOnSpinnerItemSelectionRight_menu_commands() {
        Spinner spinner_configure = (Spinner) findViewById(R.id.spinner_right_menu_commands);

        List<String> list = new ArrayList<String>();
        list.add("class behaviors.CalibrationCIBehavior");
        list.add("class behaviors.Test");
        list.add("class behaviors.TestTestTest");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item_commands, list);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner_configure.setAdapter(dataAdapter);
        spinner_configure.setOnItemSelectedListener(new CustomOnItemSelectedListenerRight_menu_commands());
    }

    public class CustomOnItemSelectedListenerRight_menu_commands implements AdapterView.OnItemSelectedListener {

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        return true;
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggleLeftMenu.syncState();
        mDrawerToggleRightMenu.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggleLeftMenu.onConfigurationChanged(newConfig);
        mDrawerToggleRightMenu.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggleLeftMenu.onOptionsItemSelected(item) || mDrawerToggleRightMenu.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_right_menu:
                if(!isOpenRightMenu || !mDrawerLayoutRightMenu.isDrawerVisible(Gravity.END)) {

                    mDrawerLayoutRightMenu.openDrawer(Gravity.END);
                    isOpenRightMenu = true;
                }
                else if (isOpenRightMenu ){
                    mDrawerLayoutRightMenu.closeDrawer(Gravity.END);
                    isOpenRightMenu = false;
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }


    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(android.os.Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(38.723827300000000000, -9.139769999999999000)).title("Teste ^^,"));
    }

}
