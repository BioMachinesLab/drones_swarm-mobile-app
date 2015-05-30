package pt.iscte.drones_swarm_mobile_app.Menus;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
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
        setValuesLeftMenu();
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
    private void setValuesLeftMenu(){
        //Drones
        addListenerOnSpinnerItemSelectionLeft_menu_drone();

        //Battery Status
        TextView textView_percentage1 = (TextView) mainActivity.findViewById(R.id.textView_percentage1);
        textView_percentage1.setText("100%");
        TextView textView_percentage2 = (TextView) mainActivity.findViewById(R.id.textView_percentage2);
        textView_percentage2.setText("95%");
        TextView textView_percentage3 = (TextView) mainActivity.findViewById(R.id.textView_percentage3);
        textView_percentage3.setText("3%");

        //Gps Data
        TextView textView_hasfix_details_gps_data = (TextView) mainActivity.findViewById(R.id.textView_hasfix_details_gps_data);
        TextView textView_latitude_details_gps_data = (TextView) mainActivity.findViewById(R.id.textView_latitude_details_gps_data);
        TextView textView_longitude_details_gps_data = (TextView) mainActivity.findViewById(R.id.textView_longitude_details_gps_data);
        TextView textView_velocity_details_gps_data = (TextView) mainActivity.findViewById(R.id.textView_velocity_details_gps_data);
        TextView textView_time_details_gps_data = (TextView) mainActivity.findViewById(R.id.textView_time_details_gps_data);
        TextView textView_satview_details_gps_data = (TextView) mainActivity.findViewById(R.id.textView_satview_details_gps_data);
        TextView textView_satused_details_gps_data = (TextView) mainActivity.findViewById(R.id.textView_satused_details_gps_data);
        TextView textView_hdop_details_gps_data = (TextView) mainActivity.findViewById(R.id.textView_hdop_details_gps_data);
        TextView textView_pdop_details_gps_data = (TextView) mainActivity.findViewById(R.id.textView_pdop_details_gps_data);
        TextView textView_vdop_details_gps_data = (TextView) mainActivity.findViewById(R.id.textView_vdop_details_gps_data);

        //Refresh Rate
        addListenerOnSpinnerItemSelectionLeft_menu_refreshrate();

        //Drone Messages
        TextView textView_drone_messages = (TextView) mainActivity.findViewById(R.id.textView_drone_messages);
        textView_drone_messages.setText("Esta mensagem serve só para testar o comprimento da caixa para ver adapta-se no layout!!");

    }
    public void addListenerOnSpinnerItemSelectionLeft_menu_drone() {
        Spinner spinner_configure = (Spinner) mainActivity.findViewById(R.id.spinner_left_menu_drone);

        List<String> list = new ArrayList<String>();
        list.add("Robot 1");
        list.add("Robot 2");
        list.add("Robot 3");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mainActivity,
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
        Spinner spinner_configure = (Spinner) mainActivity.findViewById(R.id.spinner_left_menu_refresh_rate);

        List<String> list = new ArrayList<String>();
        list.add("0.1 Hz");
        list.add("0.2 Hz");
        list.add("0.3 Hz");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mainActivity,
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


    public ActionBarDrawerToggle getmDrawerToggleLeftMenu(){
        return mDrawerToggleLeftMenu;

    }
}
