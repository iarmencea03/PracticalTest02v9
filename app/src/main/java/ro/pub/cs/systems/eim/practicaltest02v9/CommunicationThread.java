package ro.pub.cs.systems.eim.practicaltest02v9;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ro.pub.cs.systems.eim.practicaltest02v9.ServerThread;

public class CommunicationThread extends Thread {
    private ServerThread serverThread;
    private Socket socket;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {
        if (socket == null) return;
        try {
            BufferedReader reader = Utilities.getReader(socket);
            PrintWriter writer = Utilities.getWriter(socket);

            // aici exemplu in care se trimit 2 numere
            String param1 = reader.readLine();
            String param2 = reader.readLine();
            Log.d("CommThread", "Received: " + param1 + ", " + param2);

            String result = "";

            OkHttpClient client = new OkHttpClient();

            String url = "http://www.anagramica.com/all/:" + param1;

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try {
                // 2. EXECUTE THE REQUEST
                Response response = client.newCall(request).execute();

                // 3. CHECK FOR SUCCESS
                if (response.isSuccessful() && response.body() != null) {

                    String responseData = response.body().string();
                    Log.d("OkHttp", "Raw JSON: " + responseData);

                    JSONObject jsonResponse = new JSONObject(responseData);

                    JSONArray allArray = jsonResponse.getJSONArray("all");
                    StringBuilder resultsBuilder = new StringBuilder();
                    for (int i = 0; i < allArray.length(); i++) {
                        String anagram = allArray.getString(i);

                        if (anagram.length() > Integer.parseInt(param2))
                            resultsBuilder.append(anagram).append(", ");
                    }

                    result = resultsBuilder.toString();
                    if (result.endsWith(", ")) {
                        result = result.substring(0, result.length() - 2);
                    }

                    writer.println(result);
                    writer.flush();


                } else {
                    Log.e("OkHttp", "Request failed with code: " + response.code());
                }

                // Always close the response to avoid leaking resources
                response.close();

            } catch (IOException e) {
                Log.e("OkHttp", "Network error: " + e.getMessage());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        } catch (IOException e) {
            Log.e("CommThread", "Error: " + e.getMessage());
        } finally {
            try { socket.close(); } catch (IOException e) { e.printStackTrace(); }
        }
    }
}