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

import pt.iscte.drones_swarm_mobile_app.Menus.LeftMenu;
import pt.iscte.drones_swarm_mobile_app.Menus.RightMenu;
import pt.iscte.drones_swarm_mobile_app.R;


public class MainActivity extends ActionBarActivity {

    //Bar Title
    private CharSequence mTitle;
    //Menus
    private LeftMenu leftMenu;
    private RightMenu rightMenu;
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

        connectServer();

        leftMenu = new LeftMenu(this,savedInstanceState);
        rightMenu = new RightMenu(this,savedInstanceState);

        setUpMapIfNeeded();

        actionButtonsControl();

    }
    private void connectServer(){

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
