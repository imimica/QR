package com.example.qrprojectreader;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRFragment extends Fragment implements ZXingScannerView.ResultHandler {

    private TextView txtTagContent;
    private ZXingScannerView scannerView;
    private Button scanBtn;
    private Activity activity;
    private View view;
    private String result;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(getActivity());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();
    }

    public ZXingScannerView getScannerView() {
        return scannerView;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_qr_write, container, false);
        bindView(rootView);
        this.view = rootView;
        setOnClickListener();
        if(result != null) {
            txtTagContent.setText(result);
        }
        return rootView;
    }

    private void setOnClickListener() {
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.setContentView(scannerView);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    private void bindView(View view) {
        txtTagContent = view.findViewById(R.id.txtTagContent);
        scanBtn = view.findViewById(R.id.scanBtn);
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        if(args != null) {
            result = args.getString("result");
        }
    }

    private StringBuilder connectString(String[] message) {
        StringBuilder content = new StringBuilder();
        if(message.length > 1) {
            for (int i = 1; i<message.length; i++) {
                content.append(message[i]).append(" ");
            }
        }else {
            content = new StringBuilder(message[1]);
        }
        return content;
    }

    @Override
    public void handleResult(Result rawResult) {
        if(rawResult.getText() != null) {
            String[] resutlText = rawResult.getText().split(" ");
            scannerView.stopCamera();
            ((QRWriteActivity) activity).addDataInSet(rawResult.getText());
            if(resutlText[0].equals("Url")) {
                startUrlFragment(connectString(resutlText).toString());
            }else {
                presentActivity(connectString(resutlText).toString());
            }
        }
    }

    private void presentActivity(String result) {
        Intent intent = new Intent(activity, QRWriteActivity.class);

        intent.putExtra("result", result);
        activity.startActivity(intent);
    }

    private void startUrlFragment(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }
}
