package com.example.qrmanagerapp;

import static android.graphics.Bitmap.createBitmap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;

public class QRCodeGeneratorActivity extends AppCompatActivity {

    private EditText inputEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_generator);

        inputEditText = findViewById(R.id.inputEditText);
        Button generateButton = findViewById(R.id.generateButton);

        generateButton.setOnClickListener(v -> {
            String input = inputEditText.getText().toString();
            if (input.isEmpty()) {
                Toast.makeText(this, "Введите текст или URL", Toast.LENGTH_SHORT).show();
                return;
            }

            // Возвращаем текст результата в MainActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("qr_code_data", input);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}