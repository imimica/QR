package com.example.qrprojectreader;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class QRActivity extends AppCompatActivity {

    private static final String HISTORY = "History";

    private HistoryFragment historyFragment;
    private QRFragment qrFragment;
    private final int PERMISSIONS_REQUEST_CODE = 1111;
    private final String CAMERA_SERVICE = Manifest.permission.CAMERA;

    private static Set <String> historySet;
    private SharedPreferences prefs;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    presentFragment(getQrFragment());
                    return true;
                case R.id.nav_history:
                    historyFragment = getHistoryFragment();
                    historySet = getPrefs().getStringSet(HISTORY, getHistorySet());
                    historyFragment.setData(new ArrayList<>(getHistorySet()));
                    presentFragment(historyFragment);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        checkAndRequestPermissions();
        setupNavigationBar();
        if(getIntent().hasExtra("result")) {
            String result = getIntent().getStringExtra("result");
            qrFragment = getQrFragment();
            Bundle b = new Bundle();
            b.putString("result", result);
            qrFragment.setArguments(b);
            presentFragment(qrFragment);
        }
    }

    public void checkAndRequestPermissions() {
        if(ContextCompat.checkSelfPermission(this, CAMERA_SERVICE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{CAMERA_SERVICE}, PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults[0] == PackageManager.PERMISSION_DENIED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, CAMERA_SERVICE)) {
                checkAndRequestPermissions();
            } else {
                Toast.makeText(this, "Molimo omogućite dozvole u postavkama", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private Set<String > getHistorySet() {
        if(historySet == null) {
            historySet = new HashSet<>();
        }
        return historySet;
    }

    private void setupNavigationBar() {
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.nav_home);
    }

    private HistoryFragment getHistoryFragment() {
        if(historyFragment == null) {
            historyFragment = new HistoryFragment();
        }
        return historyFragment;
    }

    private QRFragment getQrFragment() {
        if(qrFragment ==null) {
            qrFragment = new QRFragment();
        }
        return qrFragment;
    }

    private SharedPreferences getPrefs() {
        if(prefs == null) {
            prefs = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        }
        return prefs;
    }

    public void addData(String content) {
        historySet = getHistorySet();
        historySet.add(content);
        writeToPrefs();
    }

    private void writeToPrefs() {
        SharedPreferences.Editor editor = getPrefs().edit();
        editor.clear();
        editor.putStringSet(HISTORY, getHistorySet());
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getQrFragment().getScannerView().startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getQrFragment().getScannerView().stopCamera();
    }

    private void presentFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.qr_view, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}