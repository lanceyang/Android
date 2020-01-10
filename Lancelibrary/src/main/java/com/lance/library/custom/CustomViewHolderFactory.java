package com.lance.library.custom;

import android.content.Context;
import android.view.View;

/**
 * Created by Tjcx on 2017/9/20.
 */

public class CustomViewHolderFactory {

    public static CustomViewHolder createViewHolder(Context context, int holderType) {
        CustomViewHolder holder = null;
        View view = null;
        switch (holderType) {
//            case HolderStatisticsDay.TYPE:
//                view = LayoutInflater.from(context).inflate(R.layout.cell_statistics_day, null);
//                holder = new HolderStatisticsDay(view);
//                break;
//            case HolderStatisticsWeek.TYPE:
//                view = LayoutInflater.from(context).inflate(R.layout.cell_statistics_week, null);
//                holder = new HolderStatisticsWeek(view);
//                break;
//            case HolderStatisticsMonth.TYPE:
//                view = LayoutInflater.from(context).inflate(R.layout.cell_statistics_month, null);
//                holder = new HolderStatisticsMonth(view);
//                break;
        }
        return holder;
    }


}
