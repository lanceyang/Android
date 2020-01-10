package pers.lance.file.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;

import pers.lance.file.viewholder.FileViewHolder;

public class FileAdapter extends RecyclerView.Adapter<FileViewHolder> implements FileViewHolder.OnClickListener {

    private File[] datas;

    private OnItemClickListener clickListener;

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FileViewHolder fileViewHolder = new FileViewHolder(parent.getContext());
        fileViewHolder.setListener(this);
        return fileViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        holder.onBind(datas[position]);
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.length;
    }

    public void setDatas(File[] datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        this.clickListener = listener;

    }

    @Override
    public void onClick(int position, File content) {
        if (clickListener != null)
            clickListener.OnClick(position, content);
    }


    public interface OnItemClickListener {
        void OnClick(int position, File content);
    }
}
