package android.dominando.pointscounter;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class MatchActivityModular extends AppCompatActivity {
    ArrayList<Player> plist;
    ArrayList<PlayerViewMatch_complete> fl;
    int matchType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null){
            Intent it = getIntent();
            plist = it.getParcelableArrayListExtra(NewMatchActivity.PLAYERSLIST);
            if (plist != null) {
                matchType = plist.size();
            }
        }else{
            matchType = savedInstanceState.getInt(PlayerViewMatch_complete.MATCH_TYPE);
        }
        switch (matchType){
            case 2:
                setContentView(R.layout.activity_match_activity_modular);
                break;
            case 3:
                setContentView(R.layout.activity_match_3players);
                break;
            case 4:
                setContentView(R.layout.activity_match_4players);
                break;
        }
        if(savedInstanceState == null) {
            //pega a lista de players enviada pela activity: NewMatchActivity
            fl = new ArrayList<>();
            for (int i = 0; i < matchType; i++) {
                fl.add(PlayerViewMatch_complete.newInstance(matchType, plist.get(i)));
            }
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            switch (matchType) {
                case 2:
                    ft.replace(R.id.fragP1, fl.get(0), "FragP1");
                    ft.replace(R.id.fragP2, fl.get(1), "FragP2");
                    ft.commit();
                    break;
                case 3:
                    ft.replace(R.id.fragP1, fl.get(0), "FragP1");
                    ft.replace(R.id.fragP2, fl.get(1), "FragP2");
                    ft.replace(R.id.fragP3, fl.get(2), "FragP3");
                    ft.commit();
                    break;
                case 4:
                    ft.replace(R.id.fragP1, fl.get(0), "FragP1");
                    ft.replace(R.id.fragP2, fl.get(1), "FragP2");
                    ft.replace(R.id.fragP3, fl.get(2), "FragP3");
                    ft.replace(R.id.fragP4, fl.get(3), "FragP4");
                    ft.commit();
                    break;
            }
        }
    }

    public void diceIconClick(View v) {
       /* Intent it = new Intent(this, DicesActivity.class);
        startActivity(it);*/
        //cria o fragment
        DicesActivityFragment fragment = DicesActivityFragment.newInstance(DicesActivityFragment.DICE_TYPE);
        fragment.show(getSupportFragmentManager(),"DiceFragment");
    }
    public void dice20IconClick(View v) {
       /* Intent it = new Intent(this, DicesActivity.class);
        startActivity(it);*/
        //cria o fragment
        DicesActivityFragment fragment = DicesActivityFragment.newInstance(DicesActivityFragment.DICE20_TYPE);
        fragment.show(getSupportFragmentManager(),"DiceFragment");
    }
    public void coinIconClick(View v) {
       /* Intent it = new Intent(this, DicesActivity.class);
        startActivity(it);*/
        //cria o fragment
        DicesActivityFragment fragment = DicesActivityFragment.newInstance(DicesActivityFragment.COIN_TYPE);
        fragment.show(getSupportFragmentManager(),"DiceFragment");
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        state.putInt(PlayerViewMatch_complete.MATCH_TYPE, matchType);
        super.onSaveInstanceState(state);
    }
}