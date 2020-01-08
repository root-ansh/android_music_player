package in.curioustools.mplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SubscriptionActivity extends AppCompatActivity {

    public interface  InputArgs{
        String SONG_NAME="song_name";
        String SINGERS="singers";
        String URL="url";
    }


    LinearLayout llSingleSong;
    Button btSingleSong,btSubsMonthly,btSubsYearly;

    TextView tvSingleSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriptions);

        tvSingleSong= findViewById(R.id.tv_single_song_subs);
        llSingleSong=findViewById(R.id.ll_single_song);

        Bundle extras = getIntent().getExtras();
        String song=null,singer=null,url=null; 
        if (extras != null) {
             song = extras.getString(InputArgs.SONG_NAME);
             singer = extras.getString(InputArgs.SINGERS);
             url = extras.getString(InputArgs.URL);
        }

        if(url==null){
            llSingleSong.setVisibility(View.GONE);
        }
        else{
            String text = String.format("%s | %s", song, singer);
            tvSingleSong.setText(text);
        }

        // TODO: 06-11-2019 add logic for subscription model 
        
        




    }
}
