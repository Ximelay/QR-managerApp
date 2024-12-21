package com.example.qrmanagerapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class QRCodeDetailsActivity extends AppCompatActivity {

    private Bitmap qrCodeBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_details);

        ImageView qrCodeImageView = findViewById(R.id.qrCodeImageView);
        Button shareButton = findViewById(R.id.shareButton);

        String qrCodeData = getIntent().getStringExtra("qr_code_data");
        if (qrCodeData != null) {
            qrCodeBitmap = generateQRCode(qrCodeData);
            qrCodeImageView.setImageBitmap(qrCodeBitmap);

            shareButton.setOnClickListener(v -> shareQRCode(qrCodeData));
        }
    }

    private Bitmap generateQRCode(String text) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }

            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void shareQRCode(String qrCodeData) {
        if (qrCodeBitmap == null) {
            return;
        }

        try {
            // Сохраняем QR-код во временный файл
            File cachePath = new File(getCacheDir(), "images");
            cachePath.mkdirs();
            File file = new File(cachePath, "qr_code.png");
            FileOutputStream stream = new FileOutputStream(file);
            qrCodeBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

            // Получаем URI с помощью FileProvider
            Uri fileUri = androidx.core.content.FileProvider.getUriForFile(
                    this,
                    getApplicationContext().getPackageName() + ".fileprovider",
                    file
            );

            // Создаем Intent для отправки
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/png");
            shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "QR-код: " + qrCodeData);

            // Предоставляем временные разрешения получателю на доступ к URI
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivity(Intent.createChooser(shareIntent, "Поделиться QR-кодом через"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
