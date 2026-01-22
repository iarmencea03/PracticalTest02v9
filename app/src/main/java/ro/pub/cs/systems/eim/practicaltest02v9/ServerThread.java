package ro.pub.cs.systems.eim.practicaltest02v9;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ServerThread extends Thread {
    private ServerSocket serverSocket;

    public ServerThread(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            Log.e("ServerThread", "Error creating socket: " + e.getMessage());
        }
    }


    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Log.d("ServerThread", "Waiting for clients...");
                Socket socket = serverSocket.accept();
                Log.d("ServerThread", "Client connected!");

                // Start the communication thread for this client
                new CommunicationThread(this, socket).start();
            }
        } catch (IOException e) {
            Log.e("ServerThread", "Error accepting connection: " + e.getMessage());
        }
    }

    public void stopThread() {
        interrupt();
        if (serverSocket != null) {
            try { serverSocket.close(); } catch (IOException e) { e.printStackTrace(); }
        }
    }
}