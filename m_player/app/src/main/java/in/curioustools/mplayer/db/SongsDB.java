package in.curioustools.mplayer.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import in.curioustools.mplayer.model.Song;

@Database(entities = Song.class,version = 1,exportSchema = false)
public  abstract class SongsDB extends RoomDatabase{
    abstract SongsTableAccessDao getSongsTableAccessDao();

    private static volatile SongsDB INSTANCE = null;
    private static final  String DB_NAME = "SONGS.db";

    @NonNull
    static synchronized SongsDB getInstance(final Context context, boolean inMemoryDBEnable) {

        if (INSTANCE == null) {
            if (inMemoryDBEnable) {
                INSTANCE = Room
                        .inMemoryDatabaseBuilder(context, SongsDB.class)
                        .build();
            } else {
                INSTANCE = Room
                        .databaseBuilder(context, SongsDB.class, DB_NAME)
                        .build();
            }
        }
        return INSTANCE;
    }


}
