package com.example.apifetching;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Activity4 extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST = 1;
    private Uri fileUri;
    private Button chooseFileButton;
    private Button saveButton;
    private ImageView arrowback;
    private EditText transactionDateEditText;
    private EditText voucherDateEditText;

    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> adapteritem;
    private List<String> itemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4);

        // Bank account start
        autoCompleteTextView = findViewById(R.id.bank_account);
        adapteritem = new ArrayAdapter<>(this, R.layout.bankaccountlist, itemList);
        autoCompleteTextView.setAdapter(adapteritem);
        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            String item = parent.getItemAtPosition(position).toString();
            Toast.makeText(Activity4.this, "Item: " + item, Toast.LENGTH_SHORT).show();
        });

        // Arrow back start
        arrowback = findViewById(R.id.arrowback1);
        arrowback.setOnClickListener(v -> {
            Intent intent = new Intent(Activity4.this, MainActivity2.class);
            startActivity(intent);
            finish();
        });

        // Initialize Spinner
        Spinner paymentModeSpinner = findViewById(R.id.payment_mode);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.payment_modes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        paymentModeSpinner.setAdapter(adapter);

        // Initialize buttons
        chooseFileButton = findViewById(R.id.choose_file);
        saveButton = findViewById(R.id.save);

        // Set onClick listener for file selection
        chooseFileButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(intent, PICK_FILE_REQUEST);
        });

        // Set onClick listener for save button
        saveButton.setOnClickListener(v -> {
            if (fileUri != null) {
                String fileName = getFileName(fileUri);
                chooseFileButton.setText(fileName);
            } else {
                Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
            }
        });

        // Initialize date EditTexts
        transactionDateEditText = findViewById(R.id.transaction_date);
        voucherDateEditText = findViewById(R.id.voucher_date);

        // Set up onClick listeners to show DatePickerDialog
        transactionDateEditText.setOnClickListener(v -> showDatePickerDialog(transactionDateEditText));
        voucherDateEditText.setOnClickListener(v -> showDatePickerDialog(voucherDateEditText));

        // Fetch data from API
        fetchDataFromApi();
    }

    private void showDatePickerDialog(final EditText dateEditText) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    String selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, month1 + 1, year1);
                    dateEditText.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            fileUri = data.getData();
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        result = cursor.getString(nameIndex);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void fetchDataFromApi() {
        new Thread(() -> {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {

                URL url = new URL("http://localhost:8080/api/accounts");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                String response = result.toString();
                Log.d("API Response", response);


                Gson gson = new Gson();
                TypeToken<List<String>> token = new TypeToken<List<String>>() {};
                List<String> fetchedItems = gson.fromJson(response, token.getType());

                runOnUiThread(() -> {
                    itemList.clear();
                    if (fetchedItems != null) {
                        itemList.addAll(fetchedItems);
                    }
                    adapteritem.notifyDataSetChanged();
                });

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("API Error", "Failed to fetch data", e);
                runOnUiThread(() -> Toast.makeText(Activity4.this, "Failed to fetch data", Toast.LENGTH_SHORT).show());
            } finally {
                try {
                    if (reader != null) reader.close();
                    if (connection != null) connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
