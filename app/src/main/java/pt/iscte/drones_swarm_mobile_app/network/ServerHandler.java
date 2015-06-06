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
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
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
            Socket s = null;
            try {
                s = new Socket();
                s.connect(new InetSocketAddress(serverIP, port), 3000);
                oos = new ObjectOutputStream(s.getOutputStream());
                final ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

                try {
                    oos.writeObject(InetAddress.getLocalHost().getHostName());
                    //Fazer pedidos inicias ao servidor
                    ServerStatusRequest serverStatusRequest = new ServerStatusRequest();
                    sendMessage(serverStatusRequest);

                    NetworkMessage ssr = getNextRecievedMessage(ois);
                    ServerStatusData serverStatusData = null;
                    DronesInformationRequest dronesInformationRequest = new DronesInformationRequest();
                    sendMessage(dronesInformationRequest);
                    //Thead que fica a escuta de mensagens que vem do servidor
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                while(!finished) {
                                    NetworkMessage networkMessage = getNextRecievedMessage(ois);
                                    switch (networkMessage.getMsgType()){
                                        case DronesInformationResponse:
                                            handleDronesInformationResponse((DronesInformationResponse)networkMessage.getMessage());
                                            break;
                                        case ServerStatusResponse:
                                            handleServerStatusResponse((ServerStatusResponse) networkMessage.getMessage());
                                            break;
                                    }
                                }
                            } catch (IOException | ClassNotFoundException e) {
                                Log.i("SH-EXCEPTION"+e.getClass().getName(), "message: " + e.getMessage());
                            }
                        }
                    }).start();
                    // Pedir regularmente informacao dos drones
                    while (!finished) {
                        try {
                            Thread.sleep(sleepMillis);
                        } catch (InterruptedException e) {}
                        if(requestAllDrones) {
                            requestAllDrones = false;
                            sendMessage(new DronesInformationRequest());
                        }else if(selectedDroneIdentification == null || selectedDroneIdentification.isEmpty()){

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
                } finally {
                    s.close();
                    ois.close();
                    oos.close();
                }
            } catch (IOException e) {
                makeShortToastAndQuit("Could not connect to Server");
                Log.e("SH-SERVERCON-ERROR", e.getMessage());
            } finally{
                if(s!=null)
                    try {
                        s.close();
                    }catch(IOException e){}
                if(oos != null)
                    try {
                        oos.close();
                    }catch(IOException e){}
            }

        } catch (Throwable e) {
            Log.i("SH-EXCEPTION-" + e.getClass().getName(), "message: " + e.getMessage());
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

    private void handleDronesInformationResponse(final DronesInformationResponse message) {
        if(message.dronesData.size() != 1){
            dronesData = message.dronesData;
            //Drones
            context.addListenerOnSpinnerItemSelectionLeft_menu_drone(message);

        }
        if(!message.dronesData.isEmpty()) {
            //set informacoes drone selecionado
            context.setLeftMenuValues(message);
            context.setRightMenuValues(message);
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    context.clearMarkers();
                    context.addMarker(message.dronesData.get(0).getRobotLocation().getLatitude(), message.dronesData.get(0).getRobotLocation().getLongitude());
                }
            });
        }

    }


    public void setSelectedDroneIdentification(String selectedDroneIdentification) {
        this.selectedDroneIdentification = selectedDroneIdentification;
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
        try {
            networkMessage.setMessage(msg);
        } catch (ClassNotFoundException e) {
        }
        String json = new Gson().toJson(networkMessage, NetworkMessage.class);
        try {
            oos.writeObject(json);
        } catch (IOException e) {
            makeShortToastAndQuit("Failed sending message to server");
        }
    }

    public ArrayList<DroneData> getDronesData() {
        return dronesData;
    }
}
