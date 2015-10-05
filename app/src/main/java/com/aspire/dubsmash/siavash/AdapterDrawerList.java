package com.aspire.dubsmash.siavash;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspire.dubsmash.R;
import com.aspire.dubsmash.hojjat.ActivityAddSound;
import com.aspire.dubsmash.hojjat.ActivityMainTokhomV;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sia on 9/30/15.
 */
public class AdapterDrawerList extends RecyclerView.Adapter<AdapterDrawerList.DrawersViewHolder> {

    private List<String> mItems;
    private Context mContext;

    public AdapterDrawerList(Context context, List<String> items) {
        mItems = items;
        mContext = context;
    }

    @Override
    public DrawersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drawer_list, parent, false);
        return new DrawersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DrawersViewHolder holder, int position) {
        String title = mItems.get(position);
        holder.bindViews(title, position);
    }

    @Override
    public int getItemCount() {
        return mItems.isEmpty() ? 1 : mItems.size();
    }

    public class DrawersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.item_title) TextView mTitle;
        @Bind(R.id.item_icon) ImageButton mIcon;
        @Bind(R.id.item_drawer_layout) RelativeLayout mItemDrawerLayout;

        private int position;

        public DrawersViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bindViews(String title, int position) {
            mTitle.setText(title);
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (position == 0) {
            } else if (position == 1){
            } else if (position == 2){
                mContext.startActivity(new Intent(mContext, ActivityAddSound.class));
            } else if (position ==3){

            }
        }
    }
}
