package pt.iscte.drones_swarm_mobile_app.network;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import network.server.shared.dataObjects.DroneData;
import network.server.shared.dataObjects.ServerStatusData;
import network.server.shared.messages.DronesInformationRequest;
import network.server.shared.messages.DronesInformationResponse;
import network.server.shared.messages.NetworkMessage;
import network.server.shared.messages.ServerMessage;
import network.server.shared.messages.ServerStatusRequest;
import network.server.shared.messages.ServerStatusResponse;
import pt.iscte.drones_swarm_mobile_app.R;
import pt.iscte.drones_swarm_mobile_app.activity.MainActivity;

/**
 * Created by HugoSousa on 30-05-2015.
 */
public final class ServerHandler implements Runnable {
    private static String serverIP = "192.168.3.250";
    private static int port = 10110;

    private boolean finished = false;
    private MainActivity context;
    private ObjectOutputStream oos;
    private int sleepMillis = 10000;
    private ArrayList<DroneData> dronesData;
    private boolean requestAllDrones = false;
    private String selectedDroneIdentification = null;

    public ServerHandler(MainActivity context) {
        this.context = context;
        addListenerOnSpinnerItemSelectionLeft_menu_refreshrate();
    }

    public static void setServerIP(String serverIP) {
        ServerHandler.serverIP = serverIP;
    }

    public void setSleepMillis(int sleepMillis) {
        this.sleepMillis = sleepMillis;
    }

    public static void setPort(int port) {
        ServerHandler.port = port;
    }

    @Override
    public void run() {
        Log.i("SH-START", "started");
        try {
            try {
                Socket s = new Socket();
                s.connect(new InetSocketAddress(serverIP, port), 3000);
                oos = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

                try {
                    //Fazer pedidos inicias ao servidor
                    ServerStatusRequest serverStatusRequest = new ServerStatusRequest();
                    sendMessage(serverStatusRequest);

                    NetworkMessage ssr = getNextRecievedMessage(ois);
                    ServerStatusData serverStatusData = null;
                    switch(ssr.getMsgType()){
                        case ServerStatusResponse:
                            serverStatusData = ((ServerStatusResponse) ssr.getMessage()).getServerStatusData();
                    }

                    DronesInformationRequest dronesInformationRequest = new DronesInformationRequest();
                    sendMessage(dronesInformationRequest);

                    NetworkMessage dir = getNextRecievedMessage(ois);
                    switch(dir.getMsgType()){
                        case DronesInformationResponse:
                            dronesData = ((DronesInformationResponse)ssr.getMessage()).dronesData;
                    }
                    //Thead que fica à escuta de mensagens que vem do servidor
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                while(!finished) {
                                    NetworkMessage networkMessage = getNextRecievedMessage(ois);
                                    switch (networkMessage.getMsgType()){
                                        case DronesInformationResponse:
                                            handleDronesInformationResponse((DronesInformationResponse)networkMessage.getMessage());
                                        case ServerStatusResponse:
                                            handleServerStatusResponse((ServerStatusResponse) networkMessage.getMessage());
                                    }
                                }
                            } catch (IOException | ClassNotFoundException e) {}
                        }
                    }).start();
                    // Pedir regularmente informação dos drones
                    while (!finished) {
                        try {
                            Thread.sleep(sleepMillis);
                        } catch (InterruptedException e) {}
                        if(requestAllDrones){
                            requestAllDrones = false;
                            sendMessage(new DronesInformationRequest());
                        }else {
                            DronesInformationRequest droneReq = new DronesInformationRequest();
                            droneReq.addDroneIdentification(selectedDroneIdentification);
                            sendMessage(droneReq);
                        }
                        sendMessage(new ServerStatusRequest());

                    }
                } catch (IOException e1) {
                    makeShortToastAndQuit("Connection to server lost");
                    Log.i("SH-TOAST", "mr toasty toast 0");
                } catch (ClassNotFoundException e) {
                    Log.e("SH-NETWORK", "ClassNotFound: " + e.getMessage());
                }
            } catch (IOException e) {
                makeShortToastAndQuit("Could not connect to Server");
                Log.i("SH-TOAST", "mr toasty toast 1");
            }

        } catch (Throwable e) {
            Log.i("SH-EXCEPTION", e.getMessage());
            throw e;
        }
    }

    private NetworkMessage getNextRecievedMessage(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        String json = (String) ois.readObject();
        NetworkMessage message = new Gson().fromJson(json, NetworkMessage.class);
        return message;
    }

    private void handleServerStatusResponse(ServerStatusResponse message) {
        if(message.getServerStatusData().getConnectedClientsQty() != dronesData.size()){
            requestAllDrones = true;
        }
    }

    private void handleDronesInformationResponse(DronesInformationResponse message) {
        if(message.dronesData.size() != 1){

            //mudar a combo box da lista de drones para os drones que vem na message
            //update
        }
        //set informações drone selecionado
        setLeftMenuValues(message);
        setRightMenuValues(message);

        context.addMarker(message.dronesData.get(0).getRobotLocation().getLatitude(), message.dronesData.get(0).getRobotLocation().getLongitude());
    }

    private void setRightMenuValues(DronesInformationResponse message) {
        //Right Menu
        //Seekbars speed limit and motor offset

        SeekBar seekBar_speed_limit = (SeekBar)context.findViewById(R.id.seekbar_speed_limit_right_menu);
        final TextView speed_limit_value = (TextView) context.findViewById(R.id.textView_speed_limit_value);

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

        SeekBar seekBar_motor_offset = (SeekBar)context.findViewById(R.id.seekbar_motor_offset_right_menu);
        final TextView motor_offset_value = (TextView) context.findViewById(R.id.textView_motor_offset_value);

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
        EditText editText_commands = (EditText) context.findViewById(R.id.editText_commands);
        editText_commands.setText("Estou escrevendo aqui mas posso alterar para enviar comandos!");

        //Buttons
        Button button_start_right_menu = (Button) context.findViewById(R.id.button_start_right_menu);
        button_start_right_menu.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,
                        "Button button_start_right_menu",
                        Toast.LENGTH_SHORT).show();
            }
        });

        Button button_stop_right_menu = (Button) context.findViewById(R.id.button_stop_right_menu);
        button_stop_right_menu.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,
                        "Button button_stop_right_menu",
                        Toast.LENGTH_SHORT).show();
            }
        });

        Button button_deploy_right_menu = (Button) context.findViewById(R.id.button_deploy_right_menu);
        button_deploy_right_menu.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,
                        "Button button_deploy_right_menu",
                        Toast.LENGTH_SHORT).show();
            }
        });

        Button button_stop_all_right_menu = (Button) context.findViewById(R.id.button_stop_all_right_menu);
        button_stop_all_right_menu.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,
                        "Button button_stop_all_right_menu",
                        Toast.LENGTH_SHORT).show();
            }
        });
        EditText editText_buttons = (EditText) context.findViewById(R.id.editText_buttons);
        editText_buttons.setText("Teste Teste Teste");
        Button button_send_log_right_menu = (Button) context.findViewById(R.id.button_send_log_right_menu);
        button_send_log_right_menu.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,
                        "Button button_send_log_right_menu",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addListenerOnSpinnerItemSelectionRight_menu_commands() {
        Spinner spinner_configure = (Spinner) context.findViewById(R.id.spinner_right_menu_commands);

        List<String> list = new ArrayList<String>();
        list.add("class behaviors.CalibrationCIBehavior");
        list.add("class behaviors.Test");
        list.add("class behaviors.TestTestTest");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                R.layout.spinner_item_commands, list);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner_configure.setAdapter(dataAdapter);
        spinner_configure.setOnItemSelectedListener(new CustomOnItemSelectedListenerRight_menu_commands());
    }

    public class CustomOnItemSelectedListenerRight_menu_commands implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
            Log.i("MENU", "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString());
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {

        }

    }

    private void setLeftMenuValues(DronesInformationResponse message) {
        // Set Left Menu
        //Drones
        addListenerOnSpinnerItemSelectionLeft_menu_drone(message);

        //Battery Status
        TextView textView_percentage1 = (TextView) context.findViewById(R.id.textView_percentage1);
        textView_percentage1.setText("100%");
        TextView textView_percentage2 = (TextView) context.findViewById(R.id.textView_percentage2);
        textView_percentage2.setText("95%");
        TextView textView_percentage3 = (TextView) context.findViewById(R.id.textView_percentage3);
        textView_percentage3.setText("3%");

        //Gps Data
        TextView textView_hasfix_details_gps_data = (TextView) context.findViewById(R.id.textView_hasfix_details_gps_data);
        textView_hasfix_details_gps_data.setText("Has Fix -> " + message.dronesData.get(0).getGPSData().getFixType());

        TextView textView_latitude_details_gps_data = (TextView) context.findViewById(R.id.textView_latitude_details_gps_data);
        textView_latitude_details_gps_data.setText("Latitude -> " + message.dronesData.get(0).getGPSData().getLatitude());

        TextView textView_longitude_details_gps_data = (TextView) context.findViewById(R.id.textView_longitude_details_gps_data);
        textView_longitude_details_gps_data.setText("Longitude -> " + message.dronesData.get(0).getGPSData().getLongitude());

        TextView textView_velocity_details_gps_data = (TextView) context.findViewById(R.id.textView_velocity_details_gps_data);
        textView_velocity_details_gps_data.setText("Vel.(Km/h) -> " + message.dronesData.get(0).getGPSData().getGroundSpeedKmh());

        TextView textView_time_details_gps_data = (TextView) context.findViewById(R.id.textView_time_details_gps_data);
        textView_time_details_gps_data.setText("Time -> " + message.dronesData.get(0).getGPSData().getDate());

        TextView textView_satview_details_gps_data = (TextView) context.findViewById(R.id.textView_satview_details_gps_data);
        textView_satview_details_gps_data.setText("Sat.View -> " + message.dronesData.get(0).getGPSData().getNumberOfSatellitesInView());

        TextView textView_satused_details_gps_data = (TextView) context.findViewById(R.id.textView_satused_details_gps_data);
        textView_satused_details_gps_data.setText("Sat.Used -> " + message.dronesData.get(0).getGPSData().getNumberOfSatellitesInUse());

        TextView textView_hdop_details_gps_data = (TextView) context.findViewById(R.id.textView_hdop_details_gps_data);
        textView_hdop_details_gps_data.setText("HDOP -> " + message.dronesData.get(0).getGPSData().getHDOP());

        TextView textView_pdop_details_gps_data = (TextView) context.findViewById(R.id.textView_pdop_details_gps_data);
        textView_pdop_details_gps_data.setText("PDOP -> " + message.dronesData.get(0).getGPSData().getPDOP());

        TextView textView_vdop_details_gps_data = (TextView) context.findViewById(R.id.textView_vdop_details_gps_data);
        textView_vdop_details_gps_data.setText("VDOP -> " + message.dronesData.get(0).getGPSData().getVDOP());

        //Drone Messages
        TextView textView_drone_messages = (TextView) context.findViewById(R.id.textView_drone_messages);
        textView_drone_messages.setText(message.dronesData.get(0).getSystemStatusMessage());
    }

    public void addListenerOnSpinnerItemSelectionLeft_menu_drone(DronesInformationResponse message) {
        Spinner spinner_configure = (Spinner) context.findViewById(R.id.spinner_left_menu_drone);

        List<String> list = new ArrayList<String>();
        for (DroneData droneData : message.dronesData) {
            list.add(droneData.getName());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner_configure.setAdapter(dataAdapter);
        spinner_configure.setOnItemSelectedListener(new CustomOnItemSelectedListenerLeft_menu_drone());
    }
    public class CustomOnItemSelectedListenerLeft_menu_drone implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
            setSelectedDroneIdentification(dronesData.get(pos).getName());
            Log.i("MENU", "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString());
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {

        }

    }

    public void setSelectedDroneIdentification(String selectedDroneIdentification) {
        this.selectedDroneIdentification = selectedDroneIdentification;
    }

    public void addListenerOnSpinnerItemSelectionLeft_menu_refreshrate() {
        Spinner spinner_configure = (Spinner) context.findViewById(R.id.spinner_left_menu_refresh_rate);

        List<String> list = new ArrayList<String>();
        list.add("0.1 Hz");
        list.add("0.2 Hz");
        list.add("0.3 Hz");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                R.layout.spinner_item, list);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner_configure.setAdapter(dataAdapter);
        spinner_configure.setOnItemSelectedListener(new CustomOnItemSelectedListenerLeft_menu_refreshrate());
    }

    public class CustomOnItemSelectedListenerLeft_menu_refreshrate implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
            int sleepMillis = -1;
            switch (pos){
                case 0:
                    sleepMillis = 10000;
                    break;
                case 1:
                    sleepMillis = 20000;
                    break;
                case 2:
                    sleepMillis = 30000;
                    break;
            }
            setSleepMillis(sleepMillis);
            Log.i("MENU", "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString());
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {

        }

    }
    private void makeShortToastAndQuit(final String text) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                finished = true;
                context.finish();
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendMessage(ServerMessage msg) {
        NetworkMessage networkMessage = new NetworkMessage();
        networkMessage.setMessage(msg);
        String json = new Gson().toJson(networkMessage, NetworkMessage.class);
        try {
            oos.writeObject(json);
        } catch (IOException e) {
            makeShortToastAndQuit("Failed sending message to server");
        }
    }
}
