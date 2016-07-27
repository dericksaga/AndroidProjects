package android.dominando.pointscounter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.suitebuilder.TestMethod;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String SHARED_PREFERENCES_NAME = "configurações";
    public static final String SHARED_PREFERENCES_USERNAME = "UserName";
    public static final String SHARED_PREFERENCES_USERIMAGEID = "UserImageId";

    Button btNewMatch;
    Button cfgUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btNewMatch = (Button)findViewById(R.id.bt_newMatch);
        cfgUser = (Button)findViewById(R.id.bt_configUser);
        btNewMatch.setOnClickListener(this);
        cfgUser.setOnClickListener(this);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_newMatch:
                Intent it = new Intent(this,NewMatchActivity.class);
                startActivity(it);
                break;
            case R.id.bt_configUser:
                Intent it1 = new Intent(this, ConfigPlayerActivity.class);
                it1.putExtra(ConfigPlayerActivity.INTENT_ORIGIN, ConfigPlayerActivity.ORIGIN_CONFIGURE_USER);
                startActivity(it1);
                break;
        }
    }
}
