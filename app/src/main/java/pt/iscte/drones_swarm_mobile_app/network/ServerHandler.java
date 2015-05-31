package pt.iscte.drones_swarm_mobile_app.network;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import network.server.shared.messages.DronesInformationRequest;
import network.server.shared.messages.NetworkMessage;

/**
 * Created by HugoSousa on 30-05-2015.
 */
public final class ServerHandler implements Runnable{
    private final Activity context;
    private static String serverIP = "192.168.3.250";
    private static int port = 10110;

    public ServerHandler(Activity context) {
        this.context = context;
    }

    public static void setServerIP(String serverIP) {
        ServerHandler.serverIP = serverIP;
    }

    public static void setPort(int port) {
        ServerHandler.port = port;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket s = new Socket(serverIP, port);
                ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
                try {
                    String json = (String) ois.readObject();
                    NetworkMessage message = new Gson().fromJson(json, NetworkMessage.class);
                    switch(message.getMsgType()){
                        case DronesInformationRequest:
                            DronesInformationRequest request = (DronesInformationRequest) message.getMessage();
                            Log.i("SH-INFORMATION", request.getMessageType().toString());
                    }
                } catch (IOException e1) {
                    Toast.makeText(context, "Connection to server lost", Toast.LENGTH_SHORT).show();
                    Log.i("SH-TOAST", "mr toasty toast 0");
                    try {
                        Thread.sleep(8000);
                    } catch (InterruptedException e) {

                    }
                } catch (ClassNotFoundException e) {
                    Log.e("SH-NETWORK", "ClassNotFound: " + e.getMessage());
                }
            } catch (IOException e) {
                Toast.makeText(context, "Could not connect to Server", Toast.LENGTH_SHORT).show();
                Log.i("SH-TOAST", "mr toasty toast 0");
                try {
                    Thread.sleep(8000);
                } catch (InterruptedException e1) {

                }
            }
        }
    }

}
