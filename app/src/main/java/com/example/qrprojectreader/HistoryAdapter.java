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

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHodler> {

    private ArrayList<String> historyList = new ArrayList<>();
    private HistoryViewHodler holder;
    private Context context;
    private RowClick rowClick;

    public HistoryAdapter(Context context, RowClick rowClick) {
        this.context = context;
        this.rowClick = rowClick;
    }

    @NonNull
    @Override
    public HistoryViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row, parent, false);
        HistoryViewHodler holder = new HistoryViewHodler(view, rowClick);
        this.holder = holder;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHodler holder, int position) {
        String [] message = historyList.get(position).split(" ");
        StringBuilder content = new StringBuilder();
        if(message.length > 1) {
            for (int i = 1; i < message.length; i++) {
                content.append(message[i]).append(" ");
            }
        }
        Drawable img =context.getResources().getDrawable(R.drawable.ic_accessible_black_24dp);
        holder.imageDesc.setImageDrawable(img);
        holder.description.setText(content.toString());
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

    class HistoryViewHodler extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView description;
        ImageView imageDesc;
        RowClick rowClick;

        public HistoryViewHodler(@NonNull View itemView, RowClick rowClick) {
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
