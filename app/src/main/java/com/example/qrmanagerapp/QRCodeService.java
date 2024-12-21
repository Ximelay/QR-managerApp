package com.example.qrmanagerapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.ArrayList;

public class QRCodeService extends Service {

    private DatabaseHelper databaseHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        databaseHelper = new DatabaseHelper(this); // Инициализация базы данных
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Запускаем процесс архивирования QR-кодов
        archiveQRCodes();
        return START_STICKY;
    }

    private void archiveQRCodes() {
        // Извлечение всех QR-кодов из базы данных
        ArrayList<String> qrCodes = databaseHelper.getAllQRCodes();

        if (qrCodes.isEmpty()) {
            System.out.println("Нет QR-кодов для архивирования.");
            return;
        }

        for (String qrCode : qrCodes) {
            // Логирование или другие действия с QR-кодами
            System.out.println("Архивируем QR-код: " + qrCode);

            // Если необходимо удалить QR-код из базы данных после обработки:
            // databaseHelper.deleteQRCode(qrCode);
        }

        System.out.println("Все QR-коды успешно архивированы.");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null; // Сервис не поддерживает привязку
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            databaseHelper.close(); // Закрытие базы данных при уничтожении сервиса
        }
    }
}