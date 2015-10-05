package com.aspire.dubsmash.siavash;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.aspire.dubsmash.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sia on 10/3/15.
 */
public class AdapterMyDubs extends RecyclerView.Adapter<AdapterMyDubs.VideosViewHolder> {
    private List<String> mVideosPath;

    public AdapterMyDubs(List<String> videosPath) {
        mVideosPath = videosPath;
    }

    @Override
    public VideosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_videos, parent, false);
        return new VideosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideosViewHolder holder, int position) {
        String videoPath = mVideosPath.get(position);
        holder.bindViews(videoPath);
    }

    @Override
    public int getItemCount() {
        return mVideosPath.isEmpty() ? 0 : mVideosPath.size();
    }

    public void addData(List<String> data) {
        mVideosPath.addAll(data);
        notifyDataSetChanged();
    }

    public class VideosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.item_video) VideoView videoItem;

        public VideosViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bindViews(String videoPath) {
//            videoItem.addSubtitleSource(is, null);
//            textView.setText(t);
        }

        @Override
        public void onClick(View v) {
//            Intent intent = new Intent(mContext, ActivityDesign.class);
//            intent.putExtra(Constants.CHOOSED_NAME, mTitle.getText());
//            intent.putExtra(Constants.CHOOSED_NAME, mIds.get(mPosition));
//            mContext.startActivity(intent);
//            if (mMainContext instanceof ActivityMain) {
//                ((ActivityMain) mMainContext).overridePendingTransition(R.anim.slide_in_bottom, 0);
//            }
        }
    }
}
