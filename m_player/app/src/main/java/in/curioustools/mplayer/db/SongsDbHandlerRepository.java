package in.curioustools.mplayer.db;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import in.curioustools.mplayer.model.Song;

public class SongsDbHandlerRepository {

    private SongsTableAccessDao songsDao;
    private ExecutorService dbQueryExecutorService;

    public static final int DB_PAGESIZE = 20;


    public SongsDbHandlerRepository(Context appCtx) {
        this.songsDao = SongsDB.getInstance(appCtx, false).getSongsTableAccessDao();
        this.dbQueryExecutorService = Executors.newSingleThreadExecutor();
    }


    public void insertSong(@NonNull final Song... songs) {
        dbQueryExecutorService.execute(() -> songsDao.insertSongs(songs));
    }

    public LiveData<PagedList<Song>> getAllSongs(){
        PagedList.Config pagedListConfigurations =
                new PagedList.Config.Builder()
                        .setEnablePlaceholders(true)
                        .setPrefetchDistance(10)
                        .setPageSize(DB_PAGESIZE)
                        .build();

        return new LivePagedListBuilder<>(
                songsDao.getCachedSongsList(), pagedListConfigurations).build();
    }


    public DataSource.Factory<Integer, Song> getPagedListFactory() {
        return songsDao.getCachedSongsList();
    }


}