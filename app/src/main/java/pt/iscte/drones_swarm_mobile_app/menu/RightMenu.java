package pt.iscte.drones_swarm_mobile_app.menu;

import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ScrollView;

import pt.iscte.drones_swarm_mobile_app.R;

/**
 * Created by rudiluis on 30/05/15.
 */
public class RightMenu {

    private ActionBarActivity mainActivity;
    //Right Menu
    private DrawerLayout mDrawerLayoutRightMenu;
    private ScrollView mDrawerListRightMenu;
    private ActionBarDrawerToggle mDrawerToggleRightMenu;


    public RightMenu(ActionBarActivity mainActivity,Bundle savedInstanceState) {

        this.mainActivity = mainActivity;

        setRightMenu(savedInstanceState);
    }

    private void setRightMenu(Bundle savedInstanceState) {
        // Initializing

        mDrawerLayoutRightMenu = (DrawerLayout) mainActivity.findViewById(R.id.drawer_layout);
        mDrawerListRightMenu = (ScrollView) mainActivity.findViewById(R.id.scrollView_right_menu);

        mDrawerLayoutRightMenu.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.END);

        mainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mainActivity. getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggleRightMenu = new ActionBarDrawerToggle(mainActivity, mDrawerLayoutRightMenu,
                R.drawable.ic_drawer, R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                mainActivity. invalidateOptionsMenu(); // creates call to
                //onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                mainActivity. invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }
        };
        mDrawerLayoutRightMenu.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);//disable swipe por causa dos seekbars
        mDrawerLayoutRightMenu.setDrawerListener(mDrawerToggleRightMenu);

    }

    public ActionBarDrawerToggle getmDrawerToggleRightMenu(){
        return mDrawerToggleRightMenu;

    }
    public DrawerLayout getmDrawerLayoutRightMenu(){
        return mDrawerLayoutRightMenu;

    }
}
