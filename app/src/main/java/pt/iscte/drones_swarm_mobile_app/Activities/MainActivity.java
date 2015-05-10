package pt.iscte.drones_swarm_mobile_app.Activities;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import pt.iscte.drones_swarm_mobile_app.Menus.CustomDrawerAdapter;
import pt.iscte.drones_swarm_mobile_app.Menus.DrawerItem;
import pt.iscte.drones_swarm_mobile_app.Menus.FragmentOne;
import pt.iscte.drones_swarm_mobile_app.Menus.FragmentThree;
import pt.iscte.drones_swarm_mobile_app.Menus.FragmentTwo;
import pt.iscte.drones_swarm_mobile_app.R;


public class MainActivity extends ActionBarActivity {

    CustomDrawerAdapter adapterLeftMenu;
    List<DrawerItem> dataListLeftMenu;
    CustomDrawerAdapter adapterRightMenu;
    List<DrawerItem> dataListRightMenu;
    //Bar Title
    private CharSequence mTitle;
    //Left Menu
    private DrawerLayout mDrawerLayoutLeftMenu;
    private ListView mDrawerListLeftMenu;
    private ActionBarDrawerToggle mDrawerToggleLeftMenu;
    private CharSequence mDrawerTitleLeftMenu;
    //Right Menu
    private DrawerLayout mDrawerLayoutRightMenu;
    private ListView mDrawerListRightMenu;
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
        setRightMenu(savedInstanceState);

        actionButtonsControl();


    }

    private void setLeftMenu(Bundle savedInstanceState) {
        // Initializing
        dataListLeftMenu = new ArrayList<DrawerItem>();
        mTitle = mDrawerTitleLeftMenu = getTitle();
        mDrawerLayoutLeftMenu = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerListLeftMenu = (ListView) findViewById(R.id.left_drawer);

        mDrawerLayoutLeftMenu.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);

        // Add Drawer Item to dataList
        dataListLeftMenu.add(new DrawerItem(true)); // adding a spinner to the list

        dataListLeftMenu.add(new DrawerItem("Battery Status")); // adding a header to the list
        dataListLeftMenu.add(new DrawerItem("Battery 1", R.drawable.ic_action_email));
        dataListLeftMenu.add(new DrawerItem("Battery 2", R.drawable.ic_action_good));
        dataListLeftMenu.add(new DrawerItem("Battery 3", R.drawable.ic_action_gamepad));


        dataListLeftMenu.add(new DrawerItem("GPS Data"));// adding a header to the list
        dataListLeftMenu.add(new DrawerItem("Has Fix", R.drawable.ic_action_search));
        dataListLeftMenu.add(new DrawerItem("Latitude", R.drawable.ic_action_cloud));
        dataListLeftMenu.add(new DrawerItem("Longitude", R.drawable.ic_action_camera));
        dataListLeftMenu.add(new DrawerItem("Vel.(Km/h)", R.drawable.ic_action_video));
        dataListLeftMenu.add(new DrawerItem("Time", R.drawable.ic_action_group));
        dataListLeftMenu.add(new DrawerItem("Sat. View", R.drawable.ic_action_import_export));
        dataListLeftMenu.add(new DrawerItem("Sat. Used", R.drawable.ic_action_import_export));
        dataListLeftMenu.add(new DrawerItem("HDOP", R.drawable.ic_action_import_export));
        dataListLeftMenu.add(new DrawerItem("PDOP", R.drawable.ic_action_import_export));
        dataListLeftMenu.add(new DrawerItem("VDOP", R.drawable.ic_action_import_export));

        dataListLeftMenu.add(new DrawerItem("Refresh Rate")); // adding a header to the list
        dataListLeftMenu.add(new DrawerItem("Details", R.drawable.ic_action_about));

        dataListLeftMenu.add(new DrawerItem("Drone Messages")); // adding a header to the list
        dataListLeftMenu.add(new DrawerItem("Falta editar", R.drawable.ic_action_about));


        adapterLeftMenu = new CustomDrawerAdapter(this, R.layout.custom_drawer_item,
                dataListLeftMenu);

        mDrawerListLeftMenu.setAdapter(adapterLeftMenu);

        mDrawerListLeftMenu.setOnItemClickListener(new DrawerItemClickListenerLeftMenu());

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

        if (savedInstanceState == null) {

            if (dataListLeftMenu.get(0).isSpinner()
                    & dataListLeftMenu.get(1).getTitle() != null) {
                SelectItemLeftMenu(2);
            } else if (dataListLeftMenu.get(0).getTitle() != null) {
                SelectItemLeftMenu(1);
            } else {
                SelectItemLeftMenu(0);
            }
        }
    }

    private void setRightMenu(Bundle savedInstanceState) {
        // Initializing
        dataListRightMenu = new ArrayList<DrawerItem>();
        mTitle = mDrawerTitleRightMenu = getTitle();
        mDrawerLayoutRightMenu = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerListRightMenu = (ListView) findViewById(R.id.right_drawer);

        mDrawerLayoutRightMenu.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.END);

        // Add Drawer Item to dataList
        dataListRightMenu.add(new DrawerItem(true)); // adding a spinner to the list

        dataListRightMenu.add(new DrawerItem("Information")); // adding a header to the list
        dataListRightMenu.add(new DrawerItem("Speed Limit", R.drawable.ic_action_email));
        dataListRightMenu.add(new DrawerItem("Motor Offset", R.drawable.ic_action_good));

        dataListRightMenu.add(new DrawerItem("Commands"));// adding a header to the list
        dataListRightMenu.add(new DrawerItem("Falta editar", R.drawable.ic_action_search));
        dataListRightMenu.add(new DrawerItem("Falta editar", R.drawable.ic_action_cloud));

        dataListRightMenu.add(new DrawerItem("Other")); // adding a header to the list
        dataListRightMenu.add(new DrawerItem("Start", R.drawable.ic_action_about));
        dataListRightMenu.add(new DrawerItem("Stop", R.drawable.ic_action_settings));
        dataListRightMenu.add(new DrawerItem("Deploy", R.drawable.ic_action_help));
        dataListRightMenu.add(new DrawerItem("Stop All", R.drawable.ic_action_help));
        dataListRightMenu.add(new DrawerItem("Falta editar", R.drawable.ic_action_help));
        dataListRightMenu.add(new DrawerItem("Send Log", R.drawable.ic_action_help));

        adapterRightMenu = new CustomDrawerAdapter(this, R.layout.custom_drawer_item,
                dataListRightMenu);

        mDrawerListRightMenu.setAdapter(adapterRightMenu);

        mDrawerListRightMenu.setOnItemClickListener(new DrawerItemClickListenerRightMenu());

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


        mDrawerLayoutRightMenu.setDrawerListener(mDrawerToggleRightMenu);

        if (savedInstanceState == null) {

            if (dataListRightMenu.get(0).isSpinner()
                    & dataListRightMenu.get(1).getTitle() != null) {
                SelectItemRightMenu(2);
            } else if (dataListRightMenu.get(0).getTitle() != null) {
                SelectItemRightMenu(1);
            } else {
                SelectItemRightMenu(0);
            }
        }
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
                textView_right_control.setText("Right Control \n"+String.valueOf(value_right_control)+ "%");

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
                textView_right_control.setText("Right Control \n"+String.valueOf(value_right_control) + "%");
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

    public void SelectItemLeftMenu(int possition) {

        Fragment fragment = null;
        Bundle args = new Bundle();
        switch (possition) {

            case 2:
                //fragment = new FragmentThree();
                //args.putString(FragmentThree.ITEM_NAME, dataList.get(possition)
                //		.getItemName());
                //args.putInt(FragmentThree.IMAGE_RESOURCE_ID, dataList
                //		.get(possition).getImgResID());
                break;
            case 3:
                fragment = new FragmentOne();
                args.putString(FragmentOne.ITEM_NAME, dataListLeftMenu.get(possition)
                        .getItemName());
                args.putInt(FragmentOne.IMAGE_RESOURCE_ID, dataListLeftMenu.get(possition)
                        .getImgResID());
                break;
            case 4:
                fragment = new FragmentTwo();
                args.putString(FragmentTwo.ITEM_NAME, dataListLeftMenu.get(possition)
                        .getItemName());
                args.putInt(FragmentTwo.IMAGE_RESOURCE_ID, dataListLeftMenu.get(possition)
                        .getImgResID());
                break;
            case 5:
                fragment = new FragmentThree();
                args.putString(FragmentThree.ITEM_NAME, dataListLeftMenu.get(possition)
                        .getItemName());
                args.putInt(FragmentThree.IMAGE_RESOURCE_ID, dataListLeftMenu
                        .get(possition).getImgResID());
                break;
            case 7:
                fragment = new FragmentTwo();
                args.putString(FragmentTwo.ITEM_NAME, dataListLeftMenu.get(possition)
                        .getItemName());
                args.putInt(FragmentTwo.IMAGE_RESOURCE_ID, dataListLeftMenu.get(possition)
                        .getImgResID());
                break;
            case 8:
                fragment = new FragmentThree();
                args.putString(FragmentThree.ITEM_NAME, dataListLeftMenu.get(possition)
                        .getItemName());
                args.putInt(FragmentThree.IMAGE_RESOURCE_ID, dataListLeftMenu
                        .get(possition).getImgResID());
                break;
            case 9:
                fragment = new FragmentOne();
                args.putString(FragmentOne.ITEM_NAME, dataListLeftMenu.get(possition)
                        .getItemName());
                args.putInt(FragmentOne.IMAGE_RESOURCE_ID, dataListLeftMenu.get(possition)
                        .getImgResID());
                break;
            case 10:
                fragment = new FragmentTwo();
                args.putString(FragmentTwo.ITEM_NAME, dataListLeftMenu.get(possition)
                        .getItemName());
                args.putInt(FragmentTwo.IMAGE_RESOURCE_ID, dataListLeftMenu.get(possition)
                        .getImgResID());
                break;
            case 11:
                fragment = new FragmentThree();
                args.putString(FragmentThree.ITEM_NAME, dataListLeftMenu.get(possition)
                        .getItemName());
                args.putInt(FragmentThree.IMAGE_RESOURCE_ID, dataListLeftMenu
                        .get(possition).getImgResID());
                break;
            case 12:
                fragment = new FragmentOne();
                args.putString(FragmentOne.ITEM_NAME, dataListLeftMenu.get(possition)
                        .getItemName());
                args.putInt(FragmentOne.IMAGE_RESOURCE_ID, dataListLeftMenu.get(possition)
                        .getImgResID());
                break;
            case 14:
                fragment = new FragmentThree();
                args.putString(FragmentThree.ITEM_NAME, dataListLeftMenu.get(possition)
                        .getItemName());
                args.putInt(FragmentThree.IMAGE_RESOURCE_ID, dataListLeftMenu.get(possition)
                        .getImgResID());
                break;
            case 15:
                fragment = new FragmentOne();
                args.putString(FragmentOne.ITEM_NAME, dataListLeftMenu.get(possition)
                        .getItemName());
                args.putInt(FragmentOne.IMAGE_RESOURCE_ID, dataListLeftMenu.get(possition)
                        .getImgResID());
                break;
            case 16:
                fragment = new FragmentTwo();
                args.putString(FragmentTwo.ITEM_NAME, dataListLeftMenu.get(possition)
                        .getItemName());
                args.putInt(FragmentTwo.IMAGE_RESOURCE_ID, dataListLeftMenu.get(possition)
                        .getImgResID());
                break;
            default:
                break;
        }
        if(fragment!=null) {
            fragment.setArguments(args);
            FragmentManager frgManager = getFragmentManager();
            frgManager.beginTransaction().replace(R.id.content_frame, fragment)
                    .commit();
        }
        mDrawerListLeftMenu.setItemChecked(possition, true);
        setTitle(dataListLeftMenu.get(possition).getItemName());
        mDrawerLayoutLeftMenu.closeDrawer(mDrawerListLeftMenu);

    }

    public void SelectItemRightMenu(int possition) {

        Fragment fragment = null;
        Bundle args = new Bundle();
        switch (possition) {

            case 2:
                //fragment = new FragmentThree();
                //args.putString(FragmentThree.ITEM_NAME, dataList.get(possition)
                //		.getItemName());
                //args.putInt(FragmentThree.IMAGE_RESOURCE_ID, dataList
                //		.get(possition).getImgResID());
                break;
            case 3:
                fragment = new FragmentOne();
                args.putString(FragmentOne.ITEM_NAME, dataListRightMenu.get(possition)
                        .getItemName());
                args.putInt(FragmentOne.IMAGE_RESOURCE_ID, dataListRightMenu.get(possition)
                        .getImgResID());
                break;
            case 4:
                fragment = new FragmentTwo();
                args.putString(FragmentTwo.ITEM_NAME, dataListRightMenu.get(possition)
                        .getItemName());
                args.putInt(FragmentTwo.IMAGE_RESOURCE_ID, dataListRightMenu.get(possition)
                        .getImgResID());
                break;
            case 5:
                fragment = new FragmentThree();
                args.putString(FragmentThree.ITEM_NAME, dataListRightMenu.get(possition)
                        .getItemName());
                args.putInt(FragmentThree.IMAGE_RESOURCE_ID, dataListRightMenu
                        .get(possition).getImgResID());
                break;
            case 7:
                fragment = new FragmentTwo();
                args.putString(FragmentTwo.ITEM_NAME, dataListRightMenu.get(possition)
                        .getItemName());
                args.putInt(FragmentTwo.IMAGE_RESOURCE_ID, dataListRightMenu.get(possition)
                        .getImgResID());
                break;
            case 8:
                fragment = new FragmentThree();
                args.putString(FragmentThree.ITEM_NAME, dataListRightMenu.get(possition)
                        .getItemName());
                args.putInt(FragmentThree.IMAGE_RESOURCE_ID, dataListRightMenu
                        .get(possition).getImgResID());
                break;
            case 9:
                fragment = new FragmentOne();
                args.putString(FragmentOne.ITEM_NAME, dataListRightMenu.get(possition)
                        .getItemName());
                args.putInt(FragmentOne.IMAGE_RESOURCE_ID, dataListRightMenu.get(possition)
                        .getImgResID());
                break;
            case 10:
                fragment = new FragmentTwo();
                args.putString(FragmentTwo.ITEM_NAME, dataListRightMenu.get(possition)
                        .getItemName());
                args.putInt(FragmentTwo.IMAGE_RESOURCE_ID, dataListRightMenu.get(possition)
                        .getImgResID());
                break;
            case 11:
                fragment = new FragmentThree();
                args.putString(FragmentThree.ITEM_NAME, dataListRightMenu.get(possition)
                        .getItemName());
                args.putInt(FragmentThree.IMAGE_RESOURCE_ID, dataListRightMenu
                        .get(possition).getImgResID());
                break;
            case 12:
                fragment = new FragmentOne();
                args.putString(FragmentOne.ITEM_NAME, dataListRightMenu.get(possition)
                        .getItemName());
                args.putInt(FragmentOne.IMAGE_RESOURCE_ID, dataListRightMenu.get(possition)
                        .getImgResID());
                break;
            case 14:
                fragment = new FragmentThree();
                args.putString(FragmentThree.ITEM_NAME, dataListRightMenu.get(possition)
                        .getItemName());
                args.putInt(FragmentThree.IMAGE_RESOURCE_ID, dataListRightMenu.get(possition)
                        .getImgResID());
                break;
            case 15:
                fragment = new FragmentOne();
                args.putString(FragmentOne.ITEM_NAME, dataListRightMenu.get(possition)
                        .getItemName());
                args.putInt(FragmentOne.IMAGE_RESOURCE_ID, dataListRightMenu.get(possition)
                        .getImgResID());
                break;
            case 16:
                fragment = new FragmentTwo();
                args.putString(FragmentTwo.ITEM_NAME, dataListRightMenu.get(possition)
                        .getItemName());
                args.putInt(FragmentTwo.IMAGE_RESOURCE_ID, dataListRightMenu.get(possition)
                        .getImgResID());
                break;
            default:
                break;
        }
        if(fragment!=null) {
            fragment.setArguments(args);
            FragmentManager frgManager = getFragmentManager();
            frgManager.beginTransaction().replace(R.id.content_frame, fragment)
                    .commit();
        }
        mDrawerListRightMenu.setItemChecked(possition, true);
        setTitle(dataListRightMenu.get(possition).getItemName());
        mDrawerLayoutRightMenu.closeDrawer(mDrawerListRightMenu);

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

    private class DrawerItemClickListenerLeftMenu implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            if (dataListLeftMenu.get(position).getTitle() == null) {
                SelectItemLeftMenu(position);
            }


        }
    }
    private class DrawerItemClickListenerRightMenu implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            if (dataListRightMenu.get(position).getTitle() == null) {
                SelectItemRightMenu(position);
            }


        }
    }
}
