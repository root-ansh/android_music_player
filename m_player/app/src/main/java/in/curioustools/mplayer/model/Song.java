package in.curioustools.mplayer.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;



//Song : Network response, main model
@Entity(tableName = "songs_table")
public class Song {

    @SerializedName("song")
    private String song;

    @PrimaryKey
    @SerializedName("url")
    @NonNull
    private String url;

    @SerializedName("artists")
    private String artists;

    @SerializedName("cover_image")
    private String coverImage;


    public Song() {
        url="";
    }

    /**
     * @param song
     * @param artists
     * @param coverImage
     * @param url
     */
    @Ignore
    public Song(String song, @NonNull String url, String artists, String coverImage) {
        super();
        this.song = song;
        this.url = url;
        this.artists = artists;
        this.coverImage = coverImage;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }


    @NonNull
    public String getUrl() {
        return url;
    }

    public void setUrl(@NonNull String url) {
        this.url = url;
    }

    public String getArtists() {
        return artists;
    }

    public void setArtists(String artists) {
        this.artists = artists;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    @Override
    public String toString() {
        return "Song{" +
                "song='" + song + '\'' +
                ", url='" + url + '\'' +
                ", artists='" + artists + '\'' +
                ", coverImage='" + coverImage + '\'' +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }
}
