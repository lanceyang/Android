package com.lance.library.custom;

import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tjcx on 2017/9/20.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomViewHolder> {

    private List<CustomBean> dataList = new ArrayList<>(0);

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         return CustomViewHolderFactory.createViewHolder(parent.getContext(), viewType);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        holder.onBind(dataList.get(position));
    }

    public void refreshData(List<? extends CustomBean> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clearData() {
        this.dataList.clear();
    }

    public void addData(List<? extends CustomBean> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).getHolderType();
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
