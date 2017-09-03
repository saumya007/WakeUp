package com.samorgs;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.saumya.wakeup.R;

/**
 * Created by saumya on 17-08-2016.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_child,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(final View itemView) {
            super(itemView);
        }
    }
}
