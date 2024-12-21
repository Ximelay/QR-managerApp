package com.example.qrmanagerapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_GENERATE = 1;
    private QRCodeAdapter adapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);
        ListView listView = findViewById(R.id.listView);

        ArrayList<String> qrCodes = databaseHelper.getAllQRCodes();
        adapter = new QRCodeAdapter(this, qrCodes);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String qrCodeData = qrCodes.get(position);
            Intent intent = new Intent(this, QRCodeDetailsActivity.class);
            intent.putExtra("qr_code_data", qrCodeData);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_create) {
            Intent intent = new Intent(this, QRCodeGeneratorActivity.class);
            startActivityForResult(intent, REQUEST_CODE_GENERATE);
            return true;
        } else if (itemId == R.id.menu_delete_all) {
            databaseHelper.deleteAllQRCodes();
            adapter.updateData(new ArrayList<>());
            adapter.notifyDataSetChanged();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GENERATE && resultCode == RESULT_OK && data != null) {
            String qrCodeData = data.getStringExtra("qr_code_data");
            if (qrCodeData != null) {
                databaseHelper.insertQRCode(qrCodeData);
                adapter.add(qrCodeData);
                adapter.notifyDataSetChanged();
            }
        }
    }
}