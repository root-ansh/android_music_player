package in.curioustools.mplayer.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;

import in.curioustools.mplayer.R;
import in.curioustools.mplayer.SubscriptionActivity;
import in.curioustools.mplayer.model.Song;

public class RvAdapter extends PagedListAdapter<Song, RvAdapter.RvHolder> {
    @Nullable
    private OnRvItemClickListener clickListener;


    private static final DiffUtil.ItemCallback<Song> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Song>() {
                @Override
                public boolean areItemsTheSame(@NonNull Song oldItem, @NonNull Song newItem) {
                    return oldItem.getUrl().equals(newItem.getUrl());
                }

                @Override
                public boolean areContentsTheSame(@NonNull Song oldItem, @NonNull Song newItem) {
                    return oldItem.equals(newItem); // eraze song equals and jus use == here
                }
            };

    public RvAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public RvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.layout_single_song, parent, false);

        return new RvHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull RvHolder holder, int position) {
        holder.bindDataAndListeners(getItem(position), clickListener);
    }


    //-------------extra access methods ----------------------------------------

    @Nullable
    public OnRvItemClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(@Nullable OnRvItemClickListener clickListener) {
        this.clickListener = clickListener;
        notifyDataSetChanged();
    }

    //-----------------------Recycler view holder class --------------------------------------------

    class RvHolder extends RecyclerView.ViewHolder {
        TextView tvSongName, tvSingers;
        ImageView ivThumbnail;
        ImageButton ibtDownload;


        RvHolder(@NonNull View itemView) {
            super(itemView);

            tvSingers = itemView.findViewById(R.id.tv_singer);
            tvSongName = itemView.findViewById(R.id.tv_songname);
            ivThumbnail = itemView.findViewById(R.id.iv_music_thumbnail);
            ibtDownload = itemView.findViewById(R.id.ibt_download);

        }

        void bindDataAndListeners(@Nullable Song song, @Nullable OnRvItemClickListener clickListener) {
            if (song != null) {
                bindData(song);
                ibtDownload.setOnClickListener(v -> startBroadCast(song));
            }
            if (clickListener != null)
                itemView.setOnClickListener(v -> clickListener.onRvItemClick(song));
        }

        private void bindData(Song song) {
            tvSongName.setText(song.getSong());
            tvSingers.setText(song.getArtists());
            Glide.with(itemView.getContext())
                    .load(song.getCoverImage())
                    .error(R.drawable.ic_placeholder)
                    .centerCrop()
                    .placeholder(R.drawable.ic_placeholder)
                    .into(ivThumbnail);
        }

        private void startBroadCast(Song song) {
            // TODO: 06-11-2019 send name to broadcast reciever, which will either
            //  start downloading thread or send the user to subscription page.
            //  currently implementing only the subscription page logic due to time constraint
            Intent i = new Intent(itemView.getContext(), SubscriptionActivity.class);
            i.putExtra(SubscriptionActivity.InputArgs.SONG_NAME, song.getSong());
            i.putExtra(SubscriptionActivity.InputArgs.SINGERS, song.getArtists());
            i.putExtra(SubscriptionActivity.InputArgs.URL, song.getUrl());

            itemView.getContext().startActivity(i);

            //todo make Songs Parcelable( Kinda forgot ^_^!)
        }
    }

    //--------------Rv Item Click Listener Class-------------------------------------------------------
    public interface OnRvItemClickListener {
        void onRvItemClick(@Nullable Song song);
    }
}
