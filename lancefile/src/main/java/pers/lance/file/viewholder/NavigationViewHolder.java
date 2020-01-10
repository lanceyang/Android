package pers.lance.file.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;

import pers.lance.file.R;

public class NavigationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private OnClickListener listener;

    private WeakReference<Context> context;

    private TextView fileName;

    private View preV;

    private String content;

    public NavigationViewHolder(Context context) {
        super(LayoutInflater.from(context).inflate(R.layout.cell_navigation, null, false));
        this.context = new WeakReference<>(context);
        fileName = itemView.findViewById(R.id.fileName);
        preV = itemView.findViewById(R.id.lt);
        itemView.setOnClickListener(this);
    }

    public void onBind(String dir) {
        if (getAdapterPosition() > 0)
            preV.setVisibility(View.VISIBLE);
        else
            preV.setVisibility(View.GONE);
        content = dir;
        fileName.setText(dir);
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
        void onClick(int position, String content);
    }
}
