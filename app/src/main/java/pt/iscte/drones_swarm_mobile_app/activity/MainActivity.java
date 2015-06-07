package pt.iscte.drones_swarm_mobile_app.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import network.server.shared.dataObjects.DroneData;
import pt.iscte.drones_swarm_mobile_app.R;
import pt.iscte.drones_swarm_mobile_app.menu.LeftMenu;
import pt.iscte.drones_swarm_mobile_app.menu.RightMenu;
import pt.iscte.drones_swarm_mobile_app.network.ServerHandler;


public class MainActivity extends ActionBarActivity {

    //Bar Title
    private CharSequence mTitle;
    //Menus
    private LeftMenu leftMenu;
    private RightMenu rightMenu;
    private boolean isOpenRightMenu = false;
    private ServerHandler serverHandler;

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

        connectServer();

        leftMenu = new LeftMenu(this,savedInstanceState);
        rightMenu = new RightMenu(this,savedInstanceState);

        setUpMapIfNeeded();

        actionButtonsControl();
        addListenerOnSpinnerItemSelectionLeft_menu_refreshrate();
    }
        private void connectServer() {
            Log.i("SH-START", "starting");
            serverHandler = new ServerHandler(this);
            new Thread(serverHandler).start();
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
        leftMenu.getmDrawerToggleLeftMenu().syncState();
        rightMenu.getmDrawerToggleRightMenu().syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        leftMenu.getmDrawerToggleLeftMenu().onConfigurationChanged(newConfig);
        rightMenu.getmDrawerToggleRightMenu().onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (leftMenu.getmDrawerToggleLeftMenu().onOptionsItemSelected(item) || rightMenu.getmDrawerToggleRightMenu().onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_right_menu:
                if(!isOpenRightMenu || !rightMenu.getmDrawerLayoutRightMenu().isDrawerVisible(Gravity.END)) {

                    rightMenu.getmDrawerLayoutRightMenu().openDrawer(Gravity.END);
                    isOpenRightMenu = true;
                }
                else if (isOpenRightMenu) {
                    rightMenu.getmDrawerLayoutRightMenu().closeDrawer(Gravity.END);
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
                //setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    public void addMarker(final double latitude, final double longitude, final boolean isSelected) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(isSelected)
                    mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_select)).position(new LatLng(latitude, longitude)).title("Drone"));
                else
                    mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).position(new LatLng(latitude, longitude)).title("Drone"));

            }
        });

    }

    public void centerMap(final double latitude, final double longitude){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                moveToCurrentLocation(new LatLng(latitude, longitude));
            }
        });
    }

    public void clearMarkers(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMap.clear();
            }
        });

    }

    private void moveToCurrentLocation(LatLng currentLocation) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 18));
        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18), 2000, null);
    }

    public void setRightMenuValues(DroneData message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Right Menu
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

                SeekBar seekBar_motor_offset = (SeekBar) findViewById(R.id.seekbar_motor_offset_right_menu);
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
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this,
                                "Button button_start_right_menu",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                Button button_stop_right_menu = (Button) findViewById(R.id.button_stop_right_menu);
                button_stop_right_menu.setOnClickListener(new View.OnClickListener()

                {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this,
                                "Button button_stop_right_menu",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                Button button_deploy_right_menu = (Button) findViewById(R.id.button_deploy_right_menu);
                button_deploy_right_menu.setOnClickListener(new View.OnClickListener()

                {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this,
                                "Button button_deploy_right_menu",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                Button button_stop_all_right_menu = (Button) findViewById(R.id.button_stop_all_right_menu);
                button_stop_all_right_menu.setOnClickListener(new View.OnClickListener()

                {
                    @Override
                    public void onClick(View v) {
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
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this,
                                "Button button_send_log_right_menu",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void addListenerOnSpinnerItemSelectionRight_menu_commands() {
        Spinner spinner_configure = (Spinner) findViewById(R.id.spinner_right_menu_commands);

        List<String> list = new ArrayList<String>();
        list.add("class behaviors.CalibrationCIBehavior");
        list.add("class behaviors.Test");
        list.add("class behaviors.TestTestTest");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MainActivity.this,
                R.layout.spinner_item_commands, list);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner_configure.setAdapter(dataAdapter);
        spinner_configure.setOnItemSelectedListener(new CustomOnItemSelectedListenerRight_menu_commands());
    }

    public class CustomOnItemSelectedListenerRight_menu_commands implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
            serverHandler.setSelectedDroneIndex(pos);
            DroneData data = serverHandler.getDronesData().get(pos);
            setLeftMenuValues(data);
            setRightMenuValues(data);
            clearMarkers();
            for (int i = 0; i < serverHandler.getDronesData().size(); i++) {
                addMarker(serverHandler.getDronesData().get(i).getGPSData().getLatitudeDecimal(), serverHandler.getDronesData().get(i).getGPSData().getLongitudeDecimal(), i == serverHandler.getSelectedDroneIndex());
            }
            Log.i("MENU", "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString());
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {

        }
    }

    public void setLeftMenuValues(final DroneData droneData) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Set Left Menu
                //Drones

                //Battery Status
                TextView textView_percentage1 = (TextView) findViewById(R.id.textView_percentage1);
                textView_percentage1.setText(/*(int)(droneData.getBatteryStatus().getCellsVoltages()[0]/4.7*100) +*/ "%");
                TextView textView_percentage2 = (TextView) findViewById(R.id.textView_percentage2);
                textView_percentage2.setText(/*(int)(droneData.getBatteryStatus().getCellsVoltages()[1]/4.7*100) + */"%");
                TextView textView_percentage3 = (TextView) findViewById(R.id.textView_percentage3);
                textView_percentage3.setText(/*(int)(droneData.getBatteryStatus().getCellsVoltages()[2]/4.7*100) + */"%");

                //Gps Data
                TextView textView_hasfix_details_gps_data = (TextView) findViewById(R.id.textView_hasfix_details_gps_data);
                textView_hasfix_details_gps_data.setText("Has Fix -> " + droneData.getGPSData().getFixType());

                TextView textView_latitude_details_gps_data = (TextView) findViewById(R.id.textView_latitude_details_gps_data);
                textView_latitude_details_gps_data.setText("Latitude -> " + droneData.getGPSData().getLatitude());

                TextView textView_longitude_details_gps_data = (TextView) findViewById(R.id.textView_longitude_details_gps_data);
                textView_longitude_details_gps_data.setText("Longitude -> " + droneData.getGPSData().getLongitude());

                TextView textView_velocity_details_gps_data = (TextView) findViewById(R.id.textView_velocity_details_gps_data);
                textView_velocity_details_gps_data.setText("Vel.(Km/h) -> " + droneData.getGPSData().getGroundSpeedKmh());

                TextView textView_time_details_gps_data = (TextView) findViewById(R.id.textView_time_details_gps_data);
                textView_time_details_gps_data.setText("Time -> " + droneData.getGPSData().getDate());

                TextView textView_satview_details_gps_data = (TextView) findViewById(R.id.textView_satview_details_gps_data);
                textView_satview_details_gps_data.setText("Sat.View -> " + droneData.getGPSData().getNumberOfSatellitesInView());

                TextView textView_satused_details_gps_data = (TextView) findViewById(R.id.textView_satused_details_gps_data);
                textView_satused_details_gps_data.setText("Sat.Used -> " + droneData.getGPSData().getNumberOfSatellitesInUse());

                TextView textView_hdop_details_gps_data = (TextView) findViewById(R.id.textView_hdop_details_gps_data);
                textView_hdop_details_gps_data.setText("HDOP -> " + droneData.getGPSData().getHDOP());

                TextView textView_pdop_details_gps_data = (TextView) findViewById(R.id.textView_pdop_details_gps_data);
                textView_pdop_details_gps_data.setText("PDOP -> " + droneData.getGPSData().getPDOP());

                TextView textView_vdop_details_gps_data = (TextView) findViewById(R.id.textView_vdop_details_gps_data);
                textView_vdop_details_gps_data.setText("VDOP -> " + droneData.getGPSData().getVDOP());

                //Drone Messages
                TextView textView_drone_messages = (TextView) findViewById(R.id.textView_drone_messages);
                textView_drone_messages.setText(droneData.getSystemStatusMessage());
            }
        });
    }

    public void addListenerOnSpinnerItemSelectionLeft_menu_drone(final ArrayList<DroneData> dronesData) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Spinner spinner_configure = (Spinner) findViewById(R.id.spinner_left_menu_drone);

                List<String> list = new ArrayList<String>();
                if(dronesData != null) {
                    for (DroneData droneData : dronesData)
                        if (droneData != null)
                            list.add(droneData.getName());
                        else
                            Log.e("DRONEDATA", "Drone data came with a null name");
                }else
                    Log.e("DRONEDATA", "message cam with null drone data");
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MainActivity.this,
                        android.R.layout.simple_spinner_item, list);
                dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinner_configure.setAdapter(dataAdapter);
                spinner_configure.setOnItemSelectedListener(new CustomOnItemSelectedListenerLeft_menu_drone());
            }
        });
    }
    private class CustomOnItemSelectedListenerLeft_menu_drone implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
            serverHandler.setSelectedDroneIndex(pos);
            Log.i("MENU", "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString());
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {

        }

    }


    private void addListenerOnSpinnerItemSelectionLeft_menu_refreshrate() {
        Spinner spinner_configure = (Spinner) findViewById(R.id.spinner_left_menu_refresh_rate);

        List<String> list = new ArrayList<String>();
        list.add("0.1 Hz");
        list.add("1 Hz");
        list.add("5 Hz");
        list.add("10 Hz");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MainActivity.this,
                R.layout.spinner_item, list);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner_configure.setAdapter(dataAdapter);
        spinner_configure.setOnItemSelectedListener(new CustomOnItemSelectedListenerLeft_menu_refreshrate());
    }

    private class CustomOnItemSelectedListenerLeft_menu_refreshrate implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
            int sleepMillis = -1;
            switch (pos){
                case 0:
                    sleepMillis = 10000;
                    break;
                case 1:
                    sleepMillis = 1000;
                    break;
                case 2:
                    sleepMillis = 200;
                    break;
                case 3:
                    sleepMillis = 100;
                    break;
            }
            serverHandler.setSleepMillis(sleepMillis);
            Log.i("MENU", "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString());
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {

        }

    }

    @Override
    protected void onDestroy() {
        serverHandler.finish();
        super.onDestroy();
    }
}
