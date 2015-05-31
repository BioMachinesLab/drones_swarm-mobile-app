package pt.iscte.drones_swarm_mobile_app.network;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

import network.server.shared.dataObjects.DroneData;
import network.server.shared.dataObjects.ServerStatusData;
import network.server.shared.messages.DronesInformationRequest;
import network.server.shared.messages.DronesInformationResponse;
import network.server.shared.messages.NetworkMessage;
import network.server.shared.messages.ServerMessage;
import network.server.shared.messages.ServerStatusRequest;
import network.server.shared.messages.ServerStatusResponse;
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
        //TODO
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
