package pt.iscte.drones_swarm_mobile_app.menu;

import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pt.iscte.drones_swarm_mobile_app.R;

/**
 * Created by rudiluis on 30/05/15.
 */
public class LeftMenu {

    private ActionBarActivity mainActivity;
    //Left Menu
    private DrawerLayout mDrawerLayoutLeftMenu;
    private ScrollView mDrawerListLeftMenu;
    private ActionBarDrawerToggle mDrawerToggleLeftMenu;

    //Bar Title
    private CharSequence mTitle;

    public LeftMenu(ActionBarActivity mainActivity,Bundle savedInstanceState) {

        this.mainActivity = mainActivity;

        setLeftMenu(savedInstanceState);
    }


    private void setLeftMenu(Bundle savedInstanceState) {
        // Initializing

        mDrawerLayoutLeftMenu = (DrawerLayout) mainActivity.findViewById(R.id.drawer_layout);
        mDrawerListLeftMenu = (ScrollView) mainActivity.findViewById(R.id.scrollView_left_menu);

        mDrawerLayoutLeftMenu.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);

        mainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mainActivity.getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggleLeftMenu = new ActionBarDrawerToggle(mainActivity, mDrawerLayoutLeftMenu,
                R.drawable.ic_drawer, R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                mainActivity.invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                mainActivity.invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }
        };

        mDrawerLayoutLeftMenu.setDrawerListener(mDrawerToggleLeftMenu);

    }

    public ActionBarDrawerToggle getmDrawerToggleLeftMenu(){
        return mDrawerToggleLeftMenu;

    }
}
