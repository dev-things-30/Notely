package com.ankit.notely.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ankit.notely.R;
import com.ankit.notely.entity.DrawerEntity;

import java.util.List;

/**
 * Created by user on 23-01-2018.
 */

public class DrawerRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DrawerEntity> mDrawerItemList = null;
    private Context mContext;

    public DrawerRecyclerAdapter(@NonNull Context context, @NonNull List<DrawerEntity> drawerItemList) {
        this.mContext = context;
        this.mDrawerItemList = drawerItemList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drawer, parent, false);
        final DrawerViewHolder viewHolder = new DrawerViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (viewHolder.getAdapterPosition()) {
                    case 0:
                        for (DrawerEntity drawerEntity : mDrawerItemList) {
                            drawerEntity.setTempIsSelected(false);
                            drawerEntity.setSelected(false);
                        }
                        notifyDataSetChanged();
                        break;
                    default:
                        DrawerEntity drawerEntity = mDrawerItemList.get(viewHolder.getAdapterPosition());
                        drawerEntity.setTempIsSelected(!drawerEntity.isTempIsSelected());
                        notifyDataSetChanged();
                        break;
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DrawerViewHolder) {
            DrawerViewHolder viewHolder = (DrawerViewHolder) holder;
            DrawerEntity drawerEntity = mDrawerItemList.get(position);
            viewHolder.tvTitle.setText(drawerEntity.getTitle());
            viewHolder.ivImage.setImageResource(drawerEntity.getImage());
            if (drawerEntity.isHeader()) {
                viewHolder.tvTitle.setTextColor(ContextCompat.getColor(mContext, android.R.color.white));
                viewHolder.ivImage.setColorFilter(ContextCompat.getColor( mContext, android.R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            } else if (drawerEntity.isTempIsSelected()){
                viewHolder.tvTitle.setTextColor(ContextCompat.getColor(mContext, R.color.filter_active));
                viewHolder.ivImage.setColorFilter(ContextCompat.getColor( mContext, R.color.filter_active), android.graphics.PorterDuff.Mode.SRC_IN);
            } else {
                viewHolder.tvTitle.setTextColor(ContextCompat.getColor(mContext, R.color.filter_inactive_text));
                viewHolder.ivImage.setColorFilter(ContextCompat.getColor( mContext, R.color.filter_inactive), android.graphics.PorterDuff.Mode.SRC_IN);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDrawerItemList.size();
    }

    public static class DrawerViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private ImageView ivImage;

        public DrawerViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            ivImage = itemView.findViewById(R.id.iv_image);
        }
    }

}
