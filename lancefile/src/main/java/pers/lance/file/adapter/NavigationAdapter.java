package pers.lance.file.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pers.lance.file.viewholder.NavigationViewHolder;

public class NavigationAdapter extends RecyclerView.Adapter<NavigationViewHolder> implements NavigationViewHolder.OnClickListener {

    private List<String> dataList = new ArrayList<>();

    private OnItemClickListener clickListener;

    @NonNull
    @Override
    public NavigationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NavigationViewHolder viewHolder = new NavigationViewHolder(parent.getContext());
        viewHolder.setListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NavigationViewHolder holder, int position) {
        holder.onBind(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setDataList(List<String> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onClick(int position, String content) {
        if (clickListener != null)
            clickListener.OnClick(position, content);
    }

    public interface OnItemClickListener {
        void OnClick(int position, String content);
    }
}
