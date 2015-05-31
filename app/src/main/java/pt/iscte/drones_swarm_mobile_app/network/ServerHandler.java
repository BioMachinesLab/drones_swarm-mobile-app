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

import network.server.shared.messages.DronesInformationRequest;
import network.server.shared.messages.NetworkMessage;
import pt.iscte.drones_swarm_mobile_app.activity.MainActivity;

/**
 * Created by HugoSousa on 30-05-2015.
 */
public final class ServerHandler implements Runnable{
    private Activity context;
    private static String serverIP = "192.168.3.250";
    private static int port = 10110;
    private boolean finished = false;

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
        Log.i("SH-START", "started");
        try {
            while (!finished) {
                try {
                    Socket s = new Socket();
                    s.connect(new InetSocketAddress(serverIP, port), 3000);
                    ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                    ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
                    try {
                        String json = (String) ois.readObject();
                        NetworkMessage message = new Gson().fromJson(json, NetworkMessage.class);
                        switch (message.getMsgType()) {
                            case DronesInformationRequest:
                                DronesInformationRequest request = (DronesInformationRequest) message.getMessage();
                                Log.i("SH-INFORMATION", request.getMessageType().toString());
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
            }
        }catch(Throwable e){
            Log.i("SH-EXCEPTION",e.getMessage());
            throw e;
        }
    }

    private void makeShortToastAndQuit(final String text){
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                finished = true;
                context.finish();
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
