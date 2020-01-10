package com.lance.library.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.lance.library.activity.PickerHolder;

import java.util.ArrayList;
import java.util.List;

import pers.lance.media.base.ResourcePo;
import pers.lance.media.base.ResourceType;

/**
 * Created by ZF_Develop_PC on 2017/11/8.
 */

public class PickerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements PickerHolder.EventCallback {

    private final int TYPE_CAMERA = 0;

    private final int TYPE_CUSTOM = 1;

    private final int DEFAULE_LIMIT_COUNT = 1;

    private int limitCount = DEFAULE_LIMIT_COUNT;

    private List<PickerHolder.PickerBean> selectedArray = new ArrayList<>();

    private int currCheckedCount = 0;

    private List<PickerHolder.PickerBean> dataList = new ArrayList<>(0);

    private OnItemClickListener itemClickListener;

    @Override
    public int getItemViewType(int position) {

        if (position == 0)
            return TYPE_CAMERA;
        else
            return TYPE_CUSTOM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (TYPE_CAMERA == viewType)
            return new CameraHolder(parent);
        return new PickerHolder(parent, this, itemClickListener);
    }

    public PickerAdapter() {
        this(1);
    }

    public PickerAdapter(int limitCount) {
        this.limitCount = limitCount;
    }

    private PickerHolder.PickerBean getData(int position) {
        return dataList.get(position - 1);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position != 0) {
            ((PickerHolder) holder).bindData(getData(position));
            ((PickerHolder) holder).setChecked(-1 != selectedArray.indexOf(getData(position)));
        }
    }

    public ResourcePo[] getSelectedPaths() {
        ResourcePo[] paths = new ResourcePo[selectedArray.size()];
        for (int i = 0; i < paths.length; i++) {

            ResourcePo po = new ResourcePo();

            PickerHolder.PickerBean cur = selectedArray.get(i);
            po.setLocalPath(cur.getPath());
            po.setContent(String.valueOf(cur.getUri()));
            po.setType(ResourceType.PHOTO);
            po.setName(cur.getUri().getLastPathSegment());
            paths[i] = po;
        }
        return paths;
    }

    public void addData(List<PickerHolder.PickerBean> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addData(PickerHolder.PickerBean pickerBean, int position) {
        this.dataList.add(position, pickerBean);
        if (currCheckedCount < limitCount) {
            selectedArray.add(pickerBean);
            currCheckedCount++;
        }
        notifyDataSetChanged();
    }

    public void refreshData(List<PickerHolder.PickerBean> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
        reset();
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private void reset() {
        currCheckedCount = 0;
        selectedArray.clear();
    }

    @Override
    public int getItemCount() {
        return dataList.size() + 1;
    }

    @Override
    public boolean onClickCb(int positon, boolean checkState) {

        if (checkState) {
            currCheckedCount--;
            selectedArray.remove(getData(positon));
            return true;
        } else {
            if (currCheckedCount < limitCount) {
                selectedArray.add(getData(positon));
                currCheckedCount++;
                return true;
            }
        }
        return false;
    }

    private class CameraHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public CameraHolder(ViewGroup view) {
            super(LayoutInflater.from(view.getContext()).inflate(view.getContext().getResources().getIdentifier("cell_camera"
                    , "layout", view.getContext().getPackageName()), view, false));
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(itemView, getLayoutPosition());
            }
        }
    }

    public interface OnItemClickListener {

        void onItemClick(View view, int position);

    }
}
