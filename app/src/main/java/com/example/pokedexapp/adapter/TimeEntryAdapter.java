package com.example.pokedexapp.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokedexapp.R;

import java.time.YearMonth;
import java.util.Map;

public class TimeEntryAdapter extends RecyclerView.Adapter<TimeEntryAdapter.TimeEntryHolder> {
    private Map<Integer, Integer> entries;
    private Context context;
    private int year, month;

    public TimeEntryAdapter(Context context, Map<Integer, Integer> entries, int year, int month){
        this.context = context;
        this.entries = entries;
        this.year = year;
        this.month = month;
    }

    @NonNull
    @Override
    public TimeEntryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TimeEntryHolder(LayoutInflater.from(context).inflate(R.layout.item_block, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TimeEntryHolder holder, int position) {
        Integer blocks = entries.get(position + 1);
        if (blocks != null){
            holder.itemView.setBackground(new ColorDrawable(ContextCompat.getColor(holder.itemView.getContext(), getBlockColor(blocks))));
        }
    }

    @ColorRes
    public int getBlockColor(int totalBlock) {
        if (totalBlock <= 0) return R.color.black;
        else if (totalBlock < 4) return R.color.yellow;
        else if (totalBlock <= 6) return R.color.green;
        else if (totalBlock <= 8) return R.color.blue;
        else return R.color.red;
    }


    @Override
    public int getItemCount() {
        return YearMonth.of(year, month).lengthOfMonth();
    }

    public class TimeEntryHolder extends RecyclerView.ViewHolder{

        public TimeEntryHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
