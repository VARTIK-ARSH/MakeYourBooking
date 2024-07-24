package com.example.apifetching;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button loginBtn;
    private EditText usernameEdt, passwordEdt;
    private TextView forgetpass;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBtn = findViewById(R.id.login);
        usernameEdt = findViewById(R.id.username);
        passwordEdt = findViewById(R.id.password);
        forgetpass = findViewById(R.id.forgetpassword);
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        if (isLoggedIn()) {
            String response = getUserResponse();
            Log.d(TAG, "User already logged in with response: " + response);
            navigateToMainActivity2(response);
        }

        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Activity2.class);
                startActivity(intent);
                finish();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usernameEdt.getText().toString().isEmpty() || passwordEdt.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("username", usernameEdt.getText().toString());
                        jsonObject.put("password", passwordEdt.getText().toString());
                        String jsonString = jsonObject.toString();
                        new PostData().execute(jsonString);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "JSON Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private boolean isLoggedIn() {
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }

    private String getUserResponse() {
        return sharedPreferences.getString("userResponse", "");
    }

    private void saveLoginState(boolean isLoggedIn, String userResponse) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", isLoggedIn);
        editor.putString("userResponse", userResponse);
        editor.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    class PostData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String responseString = null;
            HttpURLConnection client = null;
            try {
                URL url = new URL("http://52.66.75.230:8080/uows/validate");
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");
                client.setRequestProperty("Content-Type", "application/json");
                client.setRequestProperty("Accept", "application/json");
                client.setDoOutput(true);

                try (OutputStream os = client.getOutputStream()) {
                    byte[] input = strings[0].getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int responseCode = client.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream(), "utf-8"))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                        responseString = response.toString();
                    }
                } else {
                    responseString = "HTTP error code: " + responseCode;
                }

            } catch (Exception e) {
                e.printStackTrace();
                responseString = "Exception: " + e.getMessage();
            } finally {
                if (client != null) {
                    client.disconnect();
                }
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject responseJson = new JSONObject(result);
                boolean success = responseJson.getJSONObject("status").getBoolean("success");
                if (success) {
                    Log.d(TAG, "Login successful, saving user to SharedPreferences");
                    saveLoginState(true, result);
                    navigateToMainActivity2(result);
                } else {
                    Toast.makeText(MainActivity.this, "Login failed: " + responseJson.getJSONObject("status").getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void navigateToMainActivity2(String response) {
        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
        intent.putExtra("response", response);
        startActivity(intent);
        finish();
    }
}
