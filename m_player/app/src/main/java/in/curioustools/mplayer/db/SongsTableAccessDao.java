package in.curioustools.mplayer.db;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import in.curioustools.mplayer.model.Song;

@Dao
public interface SongsTableAccessDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSongs(Song... responses);

    @Query("SELECT * FROM songs_table")
    DataSource.Factory<Integer, Song> getCachedSongsList();

}

