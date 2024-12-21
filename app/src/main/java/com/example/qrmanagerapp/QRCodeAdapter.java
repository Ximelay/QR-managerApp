package com.example.qrmanagerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class QRCodeAdapter extends ArrayAdapter<String> {

    public QRCodeAdapter(Context context, List<String> qrCodes) {
        super(context, 0, qrCodes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_qrcode, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.itemText);
        String qrCode = getItem(position);
        textView.setText(qrCode);

        return convertView;
    }

    public void updateData(List<String> newQRCodes) {
        clear();
        addAll(newQRCodes);
        notifyDataSetChanged();
    }
}