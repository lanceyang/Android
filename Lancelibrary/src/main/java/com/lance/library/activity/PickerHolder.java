package com.lance.library.activity;

import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lance.library.adapter.PickerAdapter;

/**
 * Created by ZF_Develop_PC on 2017/11/7.
 */

public class PickerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

  private ViewGroup cbContainer;

  private CheckBox checkBox;

  private ImageView imageView;

  private PickerBean currBean;

  private EventCallback callback;

  private int containerId = 0;

  private PickerAdapter.OnItemClickListener itemClickListener;

  public PickerHolder(ViewGroup view, EventCallback callback, PickerAdapter.OnItemClickListener itemClickListener) {
    super(LayoutInflater.from(view.getContext()).inflate(view.getContext().getResources().getIdentifier("cell_photo_picker"
      , "layout", view.getContext().getPackageName()), view, false));
    this.callback = callback;
    this.itemClickListener = itemClickListener;
    initView();
  }

  private void initView() {

    containerId = itemView.getContext().getResources().getIdentifier("cell_photo_picker_cb_container"
      , "id", itemView.getContext().getPackageName());

    cbContainer = (ViewGroup) itemView.findViewById(containerId);


    int cbId = itemView.getContext().getResources().getIdentifier("cell_photo_picker_cb"
      , "id", itemView.getContext().getPackageName());

    checkBox = (CheckBox) itemView.findViewById(cbId);


    int imId = itemView.getContext().getResources().getIdentifier("cell_photo_picker_iv"
      , "id", itemView.getContext().getPackageName());

    imageView = (ImageView) itemView.findViewById(imId);

    cbContainer.setOnClickListener(this);
    itemView.setOnClickListener(this);

  }

  public void bindData(PickerBean data) {
    currBean = data;

    RequestOptions options = new RequestOptions();
    options.override(imageView.getWidth(), imageView.getHeight());
    Glide.with(imageView).load(data.getPath()).into(imageView);
  }

  public void setChecked(boolean checked) {
    checkBox.setChecked(checked);
  }

  @Override
  public void onClick(View v) {
    if (v.getId() == containerId) {
      if (callback.onClickCb(getLayoutPosition(), checkBox.isChecked())) {
        checkBox.setChecked(!checkBox.isChecked());
      }
    } else if (itemClickListener != null) {
      itemClickListener.onItemClick(itemView, getAdapterPosition());
    }
  }

  public static class PickerBean {
    private Uri uri;
    private String path;

    public PickerBean(Uri uri, String path) {
      this.uri = uri;
      this.path = path;
    }

    @Override
    public boolean equals(Object obj) {
      if (!(obj instanceof PickerBean) || TextUtils.isEmpty(path))
        return false;
      return path.equals(((PickerBean) obj).getPath());
    }

    public Uri getUri() {
      return uri;
    }

    public void setUri(Uri uri) {
      this.uri = uri;
    }

    public String getPath() {
      return path;
    }

    public void setPath(String path) {
      this.path = path;
    }
  }

  public interface EventCallback {

    //返回值确定当前是否超过可选择数目最大值,是否是有效点击
    boolean onClickCb(int positon, boolean checkState);

  }
}

