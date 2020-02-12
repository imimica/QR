package com.example.qrprojectreader;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class HistoryFragment extends Fragment implements HistoryAdapter.RowClick {

    private ArrayList<String> historyArray;
    private RecyclerView recyclerView;
    private HistoryAdapter historyAdapter;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.history_fragment, container, false);
        this.context = rootView.getContext();
        bindView(rootView);
        getActiveHistoryAdapter();
        setupRecyclerView();
        historyAdapter.setData(historyArray);
        return rootView;
    }

    private void bindView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(historyAdapter);
    }

    private void getActiveHistoryAdapter() {
         historyAdapter = new HistoryAdapter(this.getContext(), this);
    }

    public void setData(ArrayList<String> array) {
        this.historyArray = array;
    }

    @Override
    public void onRowClick(int pos) {
        String [] message = historyArray.get(pos).split(" ");
        String url = message[1];
        if(url.startsWith("www.") || url.startsWith("http")) {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "http://" + message[1];
            }
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        }else {
            Toast.makeText(getContext(), "This is not a URL", Toast.LENGTH_LONG).show();
        }
    }
}
