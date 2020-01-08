package in.curioustools.mplayer;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import in.curioustools.mplayer.adapter.RvAdapter;
import in.curioustools.mplayer.model.Song;
import in.curioustools.mplayer.network.NetworkResponseDownloader;

public class MainActivity extends AppCompatActivity {
    RecyclerView rv;
    LinearLayout llCurrentSong;
    TextView tvCurrentSong;
    TextView tvCurrentSongAuthors;
    ImageView ivCurrentSongThumbnail;
    ImageButton ibtPlayPause;

    RvAdapter adp;

    MainActivityViewModel viewModel;
    MediaPlayer mediaPlayer;
    CountDownTimer timer;


    private static final String TAG = "MainAct>>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initOtherStuff();
        initUI();
    }
    private void initOtherStuff() {
        HttpURLConnection.setFollowRedirects(true);
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mediaPlayer = new MediaPlayer();
    }
    private void initUI() {
        llCurrentSong = findViewById(R.id.ll_current_song);
        tvCurrentSong = findViewById(R.id.tv_current_songname);
        tvCurrentSongAuthors = findViewById(R.id.tv_current_singer); //todo  replace this layout with bottom sheetlayout
        ivCurrentSongThumbnail = findViewById(R.id.iv_currentsong_thumbnail);
        ibtPlayPause = findViewById(R.id.ibt_play_pause);

        rv = findViewById(R.id.rv_main);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
        adp = new RvAdapter();
        rv.setAdapter(adp);
        llCurrentSong.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        updateSongList(); // everytime the app loads, the database refreshes itself
        attachLiveDataToRecyclerView();

        //attach an onclick listener to rv
        adp.setClickListener(song -> {
            Toast.makeText(MainActivity.this, "loading  song " + song.getSong() + "...", Toast.LENGTH_SHORT).show();
            updateCurrentPlayerLayout(song);
            attachPlayPauseListener();
            try {
                startPlayingMediaAndTimer(song);
            } catch (Exception e) {
                Log.e(TAG, "onStart: error occurrerd:" + e.getMessage());
                e.printStackTrace();
            }
        });


    }

    private void updateSongList() {
        Toast.makeText(this, "Updating Songs List", Toast.LENGTH_SHORT).show();

        NetworkResponseDownloader.loadAdDetails(
                "http://starlord.hackerearth.com/studio",
                this,
                responseJsonString -> {
                    responseJsonString = responseJsonString == null ? "" : responseJsonString;

                    Log.e(TAG, "loadAdDetails:Response is: " + responseJsonString);

                    Type listType = new TypeToken<ArrayList<Song>>() {
                    }.getType();
                    List<Song> songList = new Gson().fromJson(responseJsonString, listType);

                    Log.e(TAG, "onResponse: songlist =" + songList);
                    viewModel.insertSong(songList.toArray(new Song[]{}));

                }
        );
    }

    private void attachLiveDataToRecyclerView() {
        LiveData<PagedList<Song>> liveSongList = viewModel.getAllSongsList();

        if (liveSongList != null) {
            liveSongList.observe(this, new Observer<PagedList<Song>>() {
                @Override
                public void onChanged(PagedList<Song> pagedList) {
                    adp.submitList(pagedList);
                    adp.notifyDataSetChanged();
                }
            });
        }

    }

    private void updateCurrentPlayerLayout(Song song) {
        llCurrentSong.setVisibility(View.INVISIBLE);
        tvCurrentSong.setText(song.getSong());
        tvCurrentSongAuthors.setText(song.getArtists());
        Glide
                .with(this)
                .load(song.getCoverImage())
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder_error)
                .into(ivCurrentSongThumbnail);

        ibtPlayPause.setImageResource(R.drawable.ic_play_black);

    }

    private void attachPlayPauseListener() {
        ibtPlayPause.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                ibtPlayPause.setImageResource(R.drawable.ic_play_black);
            } else {
                mediaPlayer.start();
                ibtPlayPause.setImageResource(R.drawable.ic_pause_black);
            }
        });
    }

    private void startPlayingMediaAndTimer(Song song) throws Exception {
        resetPlayerIfRunning();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(this, Uri.parse(song.getUrl()));
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(mp -> {
                    timer = new CountDownTimer(30000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            Log.e(TAG, "onTick: seconds left="+millisUntilFinished/1000 );
                        }

                        public void onFinish() {
                            resetPlayerIfRunning();
                            llCurrentSong.setVisibility(View.GONE);
                        }
                    };
                    mediaPlayer.start();
                    timer.start();
                    ibtPlayPause.setImageResource(R.drawable.ic_pause_black);
                    llCurrentSong.setVisibility(View.VISIBLE);
                }
        );

    }

    private void resetPlayerIfRunning() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_item_refresh) {
            updateSongList();
        } else if (item.getItemId() == R.id.menu_item_subscribe) {
            showSubscribeScreen();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSubscribeScreen() {
        Intent i = new Intent(this, SubscriptionActivity.class);
        i.putExtra(SubscriptionActivity.InputArgs.URL, (String) null);

        startActivity(i);

    }

}


