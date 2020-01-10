package pers.lance.file.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.lang.ref.WeakReference;

import pers.lance.file.R;

public class FileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private OnClickListener listener;

    private WeakReference<Context> context;

    private TextView dirName;

    private File content;

    public FileViewHolder(Context context) {
        super(LayoutInflater.from(context).inflate(R.layout.cell_file, null, false));
        this.context = new WeakReference<>(context);
        dirName = itemView.findViewById(R.id.dirName);
        itemView.setOnClickListener(this);
    }

    public void onBind(File dir) {
        content = dir;
        if (dir != null)
            dirName.setText(dir.getName());
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(getAdapterPosition(), content);
        }
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    public interface OnClickListener {
        void onClick(int position, File content);
    }

}
