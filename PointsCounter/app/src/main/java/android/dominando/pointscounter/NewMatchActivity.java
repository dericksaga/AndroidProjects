package android.dominando.pointscounter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;

public class NewMatchActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int P1_REQUEST_CODE = 1;
    public static final int P2_REQUEST_CODE = 2;
    public static final int P3_REQUEST_CODE = 3;
    public static final int P4_REQUEST_CODE = 4;
    public static final int MAX_PLAYERS = 4;
    public static final String IMAGE_INDEX = "ImageIndex";
    public static final String PLAYER_NAME = "PlayerName";
    public static final String PLAYERSLIST = "PlayersList";

    private int[] imgs ={R.drawable.player, R.drawable.playera, R.drawable.playerb};
    Button b1, bt_newP, bt_removeP;
    TableLayout tableLayout;
    PlayerSelectionView[] playerSelectionViews;
    EditText lpoints;
    int numberOfPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_match);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        b1 = (Button)findViewById(R.id.bt_iniciar);
        b1.setOnClickListener(this);
        bt_newP = (Button)findViewById(R.id.bt_addPlayer);
        bt_newP.setOnClickListener(this);
        bt_removeP = (Button)findViewById(R.id.bt_RemovePlayer);
        bt_removeP.setOnClickListener(this);
        tableLayout = (TableLayout)findViewById(R.id.tableSelection);
        playerSelectionViews = new PlayerSelectionView[4];
        playerSelectionViews[0] = (PlayerSelectionView) findViewById(R.id.p1Select);
        playerSelectionViews[0].setOnClickListener(this);
        playerSelectionViews[1] = (PlayerSelectionView) findViewById(R.id.p2Select);
        playerSelectionViews[1].setOnClickListener(this);
        playerSelectionViews[2] = (PlayerSelectionView) findViewById(R.id.p3Select);
        playerSelectionViews[2].setOnClickListener(this);
        playerSelectionViews[3] = (PlayerSelectionView) findViewById(R.id.p4Select);
        playerSelectionViews[3].setOnClickListener(this);
        lpoints = (EditText)findViewById(R.id.lpoints_edit);
        lpoints.setOnClickListener(this);
        //recupera o usuario armazenado nos shared preferences
        SharedPreferences prefs = getSharedPreferences(MainActivity.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        playerSelectionViews[0].setpName(prefs.getString(MainActivity.SHARED_PREFERENCES_USERNAME,"Player"));
   //     System.out.println("Image ID = " + prefs.getInt(MainActivity.SHARED_PREFERENCES_USERIMAGEID,0));
        int imgId = prefs.getInt(MainActivity.SHARED_PREFERENCES_USERIMAGEID,R.drawable.player);
        if(imgId != R.id.reservedIdToImage)
            playerSelectionViews[0].setBitImage(imgId);
        else {
            String aux = Utilidades.getFotoSalva(this);
            if(aux != null)
                playerSelectionViews[0].setBitImage(aux);
        }
        if(numberOfPlayers == 0)
            numberOfPlayers = 1;
        for (int i=0; i<MAX_PLAYERS;i++) {
            if(i<numberOfPlayers)
                playerSelectionViews[i].setVisibility(View.VISIBLE);
            else
                playerSelectionViews[i].setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                Intent it = NavUtils.getParentActivityIntent(this);
                it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                NavUtils.navigateUpTo(this,it);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v){
        System.out.println("Script  " + String.valueOf(v.getId()));
        switch (v.getId()){
            case R.id.bt_addPlayer:
                if(numberOfPlayers < 4){
                    numberOfPlayers +=1;
                }
                for (int i=0; i<MAX_PLAYERS;i++) {
                    if(i<numberOfPlayers)
                        playerSelectionViews[i].setVisibility(View.VISIBLE);
                    else
                        playerSelectionViews[i].setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.bt_RemovePlayer:
                if(numberOfPlayers>1){
                    numberOfPlayers-= 1;
                }
                for (int i=0; i<MAX_PLAYERS;i++) {
                    if(i<numberOfPlayers)
                        playerSelectionViews[i].setVisibility(View.VISIBLE);
                    else
                        playerSelectionViews[i].setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.bt_iniciar:
                //Inicia a activity MatchActivity passando os players
                Intent it = new Intent(this, MatchActivityModular.class);
                int pontos = Integer.parseInt(lpoints.getText().toString());
                ArrayList<Player> plist = new ArrayList<>();
                for(int i=0; i<numberOfPlayers;i++) {
                    plist.add(i, new Player(pontos, playerSelectionViews[i].getpName(), playerSelectionViews[i].getImagId()));
                }
               it.putExtra(PLAYERSLIST, plist);
                startActivity(it);

                break;
            case R.id.lpoints_edit:
                lpoints.setText("");
                break;
            case R.id.p1Select:
                Intent it1 = new Intent(this, ConfigPlayerActivity.class);
                it1.putExtra(ConfigPlayerActivity.INTENT_ORIGIN, ConfigPlayerActivity.ORIGIN_NEW_MATCH_ACTIVITY);
                startActivityForResult(it1,P1_REQUEST_CODE);
                break;
            case R.id.p2Select:
                Intent it2 = new Intent(this, ConfigPlayerActivity.class);
                it2.putExtra(ConfigPlayerActivity.INTENT_ORIGIN, ConfigPlayerActivity.ORIGIN_NEW_MATCH_ACTIVITY);
                startActivityForResult(it2,P2_REQUEST_CODE);
                break;
            case R.id.p3Select:
                Intent it3 = new Intent(this, ConfigPlayerActivity.class);
                it3.putExtra(ConfigPlayerActivity.INTENT_ORIGIN, ConfigPlayerActivity.ORIGIN_NEW_MATCH_ACTIVITY);
                startActivityForResult(it3,P3_REQUEST_CODE);
                break;
            case R.id.p4Select:
                Intent it4 = new Intent(this, ConfigPlayerActivity.class);
                it4.putExtra(ConfigPlayerActivity.INTENT_ORIGIN, ConfigPlayerActivity.ORIGIN_NEW_MATCH_ACTIVITY);
                startActivityForResult(it4,P4_REQUEST_CODE);
                break;

        }

    }
    //pega o resultado da activity ConfigPlayerActivity lançada a clicar na playerSelectionView
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data){
            if(resultCode == RESULT_OK){
                int index;
                index = data.getIntExtra(IMAGE_INDEX,0);
                switch(reqCode){
                    case P1_REQUEST_CODE:
                        playerSelectionViews[0].setpName(data.getStringExtra(PLAYER_NAME));
                        playerSelectionViews[0].setBitImage(imgs[index]);
                        break;
                    case P2_REQUEST_CODE:
                        playerSelectionViews[1].setpName(data.getStringExtra(PLAYER_NAME));
                        playerSelectionViews[1].setBitImage(imgs[index]);
                        break;
                    case P3_REQUEST_CODE:
                        playerSelectionViews[2].setpName(data.getStringExtra(PLAYER_NAME));
                        playerSelectionViews[2].setBitImage(imgs[index]);
                        break;
                    case P4_REQUEST_CODE:
                        playerSelectionViews[3].setpName(data.getStringExtra(PLAYER_NAME));
                        playerSelectionViews[3].setBitImage(imgs[index]);
                        break;
                }
            }
    }
}
