package com.example.qrprojectreader;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private ArrayList<String> historyList = new ArrayList<>();
    private HistoryViewHolder holder;
    private Context context;
    private RowClick rowClick;

    public HistoryAdapter(Context context, RowClick rowClick) {
        this.context = context;
        this.rowClick = rowClick;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row, parent, false);
        HistoryViewHolder holder = new HistoryViewHolder(view, rowClick);
        this.holder = holder;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        Drawable img =context.getResources().getDrawable(R.drawable.ic_accessible_black_24dp);
        holder.imageDesc.setImageDrawable(img);
        holder.description.setText(historyList.get(position));
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public void setData(ArrayList<String> historyList) {
        if(this.historyList.size() == 0) {
            this.historyList = historyList;
        }else {
            this.historyList.addAll(historyList);
        }
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView description;
        ImageView imageDesc;
        RowClick rowClick;

        public HistoryViewHolder(@NonNull View itemView, RowClick rowClick) {
            super(itemView);
            description = itemView.findViewById(R.id.desc_text);
            imageDesc = itemView.findViewById(R.id.image_desc);
            itemView.setOnClickListener(this);
            this.rowClick = rowClick;
        }

        @Override
        public void onClick(View view) {
            this.rowClick.onRowClick(getAdapterPosition());
        }
    }

    public interface RowClick {
        void onRowClick(int pos);
    }

}
