package in.curioustools.mplayer;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import in.curioustools.mplayer.db.SongsDbHandlerRepository;
import in.curioustools.mplayer.model.Song;

public class MainActivityViewModel extends AndroidViewModel {
    private SongsDbHandlerRepository repo;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        repo = new SongsDbHandlerRepository(application.getApplicationContext());

    }

    void insertSong(@NonNull Song... entries) {
        repo.insertSong(entries);

    }



    @Nullable
    LiveData<PagedList<Song>> getAllSongsList() {
        return  repo.getAllSongs();

    }


}
