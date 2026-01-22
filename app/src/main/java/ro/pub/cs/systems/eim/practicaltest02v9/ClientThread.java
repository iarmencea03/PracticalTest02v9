package ro.pub.cs.systems.eim.practicaltest02v9;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import ro.pub.cs.systems.eim.practicaltest02v9.Utilities;

public class ClientThread extends Thread {
    private String address;
    private int port;
    private String cuvant;

    private String litere;
    private TextView outputTextView;

    public ClientThread(int port, String cuvant, String litere, TextView outputTextView) {
        this.port = port;
        this.cuvant = cuvant;
        this.litere = litere;
        this.outputTextView = outputTextView;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(address, port);

            // Utilities GASESC IN EXEMPLU COLOCVIU 2
            BufferedReader reader = Utilities.getReader(socket);
            PrintWriter writer = Utilities.getWriter(socket);

            // --- 1. SEND DATA ---
            // TODO: Write your parameters to the server
            writer.println(cuvant);
            writer.println(litere);
            writer.flush();

            // --- 2. READ RESPONSE ---
            String line;
            while ((line = reader.readLine()) != null) {
                Log.d("ClientThread", "Response from server: " + line);
                final String text = line;
                outputTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        outputTextView.append(text + "\n");
                    }
                });
            }
            socket.close();
        } catch (IOException e) {
            Log.e("ClientThread", "Error: " + e.getMessage());
        }
    }
}