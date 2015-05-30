package pt.iscte.drones_swarm_mobile_app.Menus;

import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
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

import java.util.ArrayList;
import java.util.List;

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
        setValuesRightMenu();
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
    private void setValuesRightMenu(){
        //Seekbars speed limit and motor offset

        SeekBar seekBar_speed_limit = (SeekBar)mainActivity.findViewById(R.id.seekbar_speed_limit_right_menu);
        final TextView speed_limit_value = (TextView) mainActivity.findViewById(R.id.textView_speed_limit_value);

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

        SeekBar seekBar_motor_offset = (SeekBar)mainActivity.findViewById(R.id.seekbar_motor_offset_right_menu);
        final TextView motor_offset_value = (TextView) mainActivity.findViewById(R.id.textView_motor_offset_value);

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
        EditText editText_commands = (EditText) mainActivity.findViewById(R.id.editText_commands);
        editText_commands.setText("Estou escrevendo aqui mas posso alterar para enviar comandos!");

        //Buttons
        Button button_start_right_menu = (Button) mainActivity.findViewById(R.id.button_start_right_menu);
        button_start_right_menu.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
                Toast.makeText(mainActivity,
                        "Button button_start_right_menu",
                        Toast.LENGTH_SHORT).show();
            }
        });

        Button button_stop_right_menu = (Button) mainActivity.findViewById(R.id.button_stop_right_menu);
        button_stop_right_menu.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
                Toast.makeText(mainActivity,
                        "Button button_stop_right_menu",
                        Toast.LENGTH_SHORT).show();
            }
        });

        Button button_deploy_right_menu = (Button) mainActivity.findViewById(R.id.button_deploy_right_menu);
        button_deploy_right_menu.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
                Toast.makeText(mainActivity,
                        "Button button_deploy_right_menu",
                        Toast.LENGTH_SHORT).show();
            }
        });

        Button button_stop_all_right_menu = (Button) mainActivity.findViewById(R.id.button_stop_all_right_menu);
        button_stop_all_right_menu.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
                Toast.makeText(mainActivity,
                        "Button button_stop_all_right_menu",
                        Toast.LENGTH_SHORT).show();
            }
        });

        EditText editText_buttons = (EditText) mainActivity.findViewById(R.id.editText_buttons);
        editText_buttons.setText("Teste Teste Teste");
        Button button_send_log_right_menu = (Button) mainActivity.findViewById(R.id.button_send_log_right_menu);
        button_send_log_right_menu.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
                Toast.makeText(mainActivity,
                        "Button button_send_log_right_menu",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void addListenerOnSpinnerItemSelectionRight_menu_commands() {
        Spinner spinner_configure = (Spinner) mainActivity.findViewById(R.id.spinner_right_menu_commands);

        List<String> list = new ArrayList<String>();
        list.add("class behaviors.CalibrationCIBehavior");
        list.add("class behaviors.Test");
        list.add("class behaviors.TestTestTest");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mainActivity,
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


    public ActionBarDrawerToggle getmDrawerToggleRightMenu(){
        return mDrawerToggleRightMenu;

    }
    public DrawerLayout getmDrawerLayoutRightMenu(){
        return mDrawerLayoutRightMenu;

    }
}
