package com.lance.library.custom;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * Created by Tjcx on 2017/9/20.
 */

public abstract class CustomViewHolder<T extends CustomBean> extends RecyclerView.ViewHolder {

    public CustomViewHolder(View itemView) {
        super(itemView);
        initView();
    }

    public abstract void initView();

    public abstract void onBind(T data);
}
