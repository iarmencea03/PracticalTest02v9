package ro.pub.cs.systems.eim.practicaltest02v9;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PracticalTest02v9MainActivity extends AppCompatActivity {

    private ServerThread serverThread = null;
    private EditText serverPortEditText;
    private EditText cuvantEditText, litereEditText;
    private Button startServerButton, executeButton;

    private TextView output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_practical_test0v9_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        startServerButton = (Button)findViewById(R.id.connect_button);
        serverPortEditText = (EditText)findViewById(R.id.server_port_edit_text);
        cuvantEditText = (EditText)findViewById(R.id.client_cuvant_edit_text);
        litereEditText = (EditText)findViewById(R.id.client_litere_edit_text);
        executeButton = (Button)findViewById(R.id.execute_button);
        output = (TextView)findViewById(R.id.anagram_text_view);



        startServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String portStr = serverPortEditText.getText().toString();
                int port = 2000;
                if (!portStr.isEmpty()) {
                    try {
                        port = Integer.parseInt(portStr);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }

                serverThread = new ServerThread(port);
                serverThread.start();
                Log.d("MainThread", "Started Server on port " + portStr);
            }
        });

        executeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String port = serverPortEditText.getText().toString();
                String cuvant = cuvantEditText.getText().toString();
                String litere = litereEditText.getText().toString();
                if (!cuvant.isEmpty() || !litere.isEmpty()) {
                    new ClientThread(Integer.parseInt(port.trim()),
                            cuvant.trim(), litere.trim(), output).start();
                }
            }
        });
    }

    }