package android.dominando.pointscounter;

import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class MatchActivity extends AppCompatActivity implements PlayerViewMatch.FragmentActions, EnterAmountFragment.OnFragmentResultListener{
    public static final int AMOUNT_REQUEST_CODE1 = 1;
    public static final int AMOUNT_REQUEST_CODE2 = 2;
    public static final int AMOUNT_REQUEST_CODE3 = 3;
    public static final int AMOUNT_REQUEST_CODE4 = 4;
    public static final int COUNT_RATE1 = 10;
    public static final int COUNT_RATE2 = 100;
    public static final String PARAM1 = "quantidade";
    public static final String PARAM2 = "operacao";
    //Constantes para gerenciar os audios
    public static final int COUNTER_SOUND = 0;

    TextView lifepoint;
    TextView lifepoint2, lifepoint3;
    int aux1;
    int aux2;
    int cont, rate;
    String sinal;
    Handler mHandler;//Handler que escalona a execução de uma thread Runnable
    int reqCode;//pega o codigo de requisiçao do intent retornado pela activity EnterAmount
    SoundPool soundPool;
    SparseIntArray soundMap;
    int streamID; //retornado pela função play
    View fragp1, fragp2, fragp3;
    ImageView imgDice;
    int matchType;
    ArrayList<PlayerViewMatch> f1;
    ArrayList<Player> plist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //pega a lista de players enviada pela activity: NewMatchActivity
        Intent it = getIntent();
        f1 = new ArrayList<>();
        plist = it.getParcelableArrayListExtra(NewMatchActivity.PLAYERSLIST);
        if(plist != null){
            matchType = plist.size();
            switch(matchType){
                case 2:
                    setContentView(R.layout.activity_match);
                    break;
                case 3:
                    setContentView(R.layout.activity_match_3players);
                    break;
                case 4:
                    setContentView(R.layout.activity_match);
                    break;
            }
        }else {
            setContentView(R.layout.activity_match);
        }
        for(int i =0; i<matchType; i++){
            f1.add(PlayerViewMatch.newInstance(matchType));
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch(matchType){
            case 2:
                ft.replace(R.id.fragP1, f1.get(0),"FragP1");
                ft.replace(R.id.fragP2, f1.get(1),"FragP2");
                ft.commit();
                break;
            case 3:
                ft.replace(R.id.fragP1, f1.get(0),"FragP1");
                ft.replace(R.id.fragP2, f1.get(1),"FragP2");
                ft.replace(R.id.fragP3, f1.get(2),"FragP3");
                ft.commit();
                break;
            case 4:
                break;
        }
        mHandler = new Handler();
        //carrega arquivo de audio
        //o construtor do soundpool foi depreciado a partir da versao do lollipop
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);//cria um soundpool
        }else{
            AudioAttributes attrs = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build();
            soundPool = new SoundPool.Builder().setMaxStreams(1).setAudioAttributes(attrs).build();
        }
        soundMap = new SparseIntArray(1);//cria o sound map - map of soundIDs
        soundMap.put(COUNTER_SOUND,soundPool.load(this,R.raw.pointcountersound,1));//insere o soundpool no soundmap
    }

    @Override
    public void onStart(){
        super.onStart();
        fragp1 = findViewById(R.id.fragP1);//pega a referencia da view do fragment do player 1
        fragp1.setBackgroundColor(getColor(R.color.fragPlayerMatch));
        fragp2 = findViewById(R.id.fragP2);//pega a referencia da view do fragment do player 2
        fragp2.setBackgroundColor(getColor(R.color.fragPlayerMatch));
        lifepoint = (TextView)(fragp1.findViewById(R.id.lifepoints));
        lifepoint2 = (TextView)(fragp2.findViewById(R.id.lifepoints));
        if(matchType == 3){
            fragp3 = findViewById(R.id.fragP3);//pega a referencia da view do fragment do player 2
            lifepoint3 = (TextView)(fragp3.findViewById(R.id.lifepoints));
            fragp3.setBackgroundColor(getColor(R.color.fragPlayerMatch));
        }
        imgDice = (ImageView)findViewById(R.id.diceBtnMin);
        if(plist != null){
            for(int i =0; i<matchType; i++) {
                this.f1.get(i).setPlayerName(plist.get(i).getPlayerName());
                this.f1.get(i).setImgPlayer(plist.get(i).getIndexImage());
                this.f1.get(i).setLifePoints(plist.get(i).getLifePoints());
            }
        }
    }

    public void diceIconClick(View v){
        Intent it = new Intent(this, DicesActivity.class);
        startActivity(it);
    }

    public void lifePointsClick(View v){
       // Intent intent = new Intent(this, EnterAmount.class);
        //como a activity que calcula a quantidade a ser somada ou diminuida
        //retorna esse valor, deve usar o método startActivityForResult
        TextView aux = (TextView)v;
        int id = ((View)(((View)((View)v.getParent()).getParent()).getParent())).getId();
        switch(id){
            case R.id.fragP1:
                //startActivityForResult(intent,AMOUNT_REQUEST_CODE1);
              //  EnterAmountFragment amountFragment = new EnterAmountFragment();
                reqCode = AMOUNT_REQUEST_CODE1;
                if(getSupportFragmentManager().findFragmentByTag("Dialog") == null){
                    new EnterAmountFragment().show(this.getSupportFragmentManager(),"Dialog");
                }
                break;
            case R.id.fragP2:
                reqCode = AMOUNT_REQUEST_CODE2;
                if(getSupportFragmentManager().findFragmentByTag("Dialog") == null){
                    new EnterAmountFragment().show(getSupportFragmentManager(),"Dialog");
                }
                break;
            case R.id.fragP3:
                reqCode = AMOUNT_REQUEST_CODE3;
                if(getSupportFragmentManager().findFragmentByTag("Dialog") == null){
                    new EnterAmountFragment().show(getSupportFragmentManager(),"Dialog");
                }
                // startActivityForResult(intent,AMOUNT_REQUEST_CODE3);
                break;
        }
    }
    public void surrenderClick(View v){
        ImageView img = (ImageView)v;
        int id = ((View)(((View)((View)v.getParent()).getParent()).getParent())).getId();
        switch (id){
            case R.id.fragP1:
                aux1 = Integer.parseInt(lifepoint.getText().toString());
                reqCode = AMOUNT_REQUEST_CODE1;
                break;
            case R.id.fragP2:
                aux1 = Integer.parseInt(lifepoint2.getText().toString());
                reqCode = AMOUNT_REQUEST_CODE2;
                break;
            case R.id.fragP3:
                aux1 = Integer.parseInt(lifepoint3.getText().toString());
                reqCode = AMOUNT_REQUEST_CODE3;
        }
        aux2 = aux1;
        sinal = "-";
        cont = 0;
        if(aux2 >= 1000){
            rate = COUNT_RATE2;
        }else{
            rate = COUNT_RATE1;
        }
        //remove os callbacks da thread runnable MyTimeTask
        mHandler.removeCallbacks(myTimeTask);
        //manda a thread executar (metodo run()) após o delay especificado
        mHandler.postDelayed(myTimeTask,10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK ){
            reqCode = requestCode;
            switch (requestCode){
                case AMOUNT_REQUEST_CODE1:
                    //esse código de requisição é do fragment do player1
                    aux1 = Integer.parseInt(lifepoint.getText().toString());
                    break;
                case AMOUNT_REQUEST_CODE2:
                    aux1 = Integer.parseInt(lifepoint2.getText().toString());
                    break;
                case AMOUNT_REQUEST_CODE3:
                    aux1 = Integer.parseInt(lifepoint3.getText().toString());
                    break;
            }
            aux2 = Integer.parseInt(data.getStringExtra(PARAM1));
            //PARAM1 é o valor a ser somado ou subtraido
            sinal = data.getStringExtra(PARAM2);
            //PARAM2 é o sinal da operação, pode ser soma - "+" ou subtração - "-"
            cont = 0;
            if(aux2 >= 1000){
                rate = COUNT_RATE2;
            }else{
                rate = COUNT_RATE1;
            }
            //remove os callbacks da thread runnable MyTimeTask
            mHandler.removeCallbacks(myTimeTask);
            //manda a thread executar (metodo run()) após o delay especificado
            mHandler.postDelayed(myTimeTask,10);
            //streamID = soundPool.play(soundMap.get(COUNTER_SOUND),1,1,1,0,1f);
        }
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        soundPool.release();
    }
    //objeto Runnable, reponsavel por atualizar o TextView dos contadores de pontos
    private Runnable myTimeTask = new Runnable() {
        @Override
        public void run() {
            if(cont == 0){
                streamID = soundPool.play(soundMap.get(COUNTER_SOUND),1,1,1,-1,1f);
                // System.out.println(String.valueOf(streamID));
                //soundPool.setLoop(streamID,-1);
                switch(reqCode){
                    case  AMOUNT_REQUEST_CODE1:
                        fragp1.setBackgroundColor(getColor(R.color.damageColor));
                        break;
                    case AMOUNT_REQUEST_CODE2:
                        fragp2.setBackgroundColor(getColor(R.color.damageColor));
                        break;
                    case AMOUNT_REQUEST_CODE3:
                        fragp3.setBackgroundColor(getColor(R.color.damageColor));
                }
            }
            switch(sinal){
                case "+":
                    if(cont<aux2){
                        cont+=rate;
                        aux1+=rate;
                    }
                    break;
                case "-":
                    if(cont<aux2){
                        cont+=rate;
                        aux1-=rate;
                        aux1 = (aux1 < 0)? 0 : aux1;
                    }
                    break;
                case "=":
                    cont = aux2;
                    break;
            }
            //atualiza o valor de aux1
            switch (reqCode){//chega o codigo de requisição retornado pelo intent da EnterAmount
                case AMOUNT_REQUEST_CODE1://se veio do fragment do player 1
                    lifepoint.setText(String.valueOf(aux1));
                    break;
                case AMOUNT_REQUEST_CODE2://se veio do fragment do player 2
                    lifepoint2.setText(String.valueOf(aux1));
                    break;
                case AMOUNT_REQUEST_CODE3://se veio do fragment do player 2
                    lifepoint3.setText(String.valueOf(aux1));
                    break;
            }
            if(cont<aux2 && aux1 != 0){//verifica se a contagem ja foi finalizada
                mHandler.postDelayed(this,10);//se não, continua chamando esse método
                //    System.out.println("Cont = " + String.valueOf(cont));
            }else{
                soundPool.stop(streamID);
                fragp1.setBackgroundColor(getColor(R.color.fragPlayerMatch));
                fragp2.setBackgroundColor(getColor(R.color.fragPlayerMatch));
                if(matchType ==3){
                    fragp3.setBackgroundColor(getColor(R.color.fragPlayerMatch));
                }
                //atualiza os pontos de vida dos jogadores
                switch (reqCode){
                    case AMOUNT_REQUEST_CODE1://se veio do fragment do player 1
                        plist.get(0).setLifePoints(aux1);
                        break;
                    case AMOUNT_REQUEST_CODE2://se veio do fragment do player 2
                        plist.get(1).setLifePoints(aux1);
                        break;
                    case AMOUNT_REQUEST_CODE3:
                        plist.get(2).setLifePoints(aux1);
                        break;
                }
                mHandler.removeCallbacks(this);//se já, remove o callback desse metodo do objeto handler
            }
        }
    };
    //Métodos da Action Bar
    //Sobreescreve o metodo que cria os itens de menu na barra de ações
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_match,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.dicesMenu:
                Intent it = new Intent(this, DicesActivity.class);
                startActivity(it);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //metoodo para salvar o estato da activity ao rotacionar o aparelho
    @Override
    public void onSaveInstanceState(Bundle actState){
        super.onSaveInstanceState(actState);
        actState.putInt("p1LP", Integer.parseInt(lifepoint.getText().toString()));
        actState.putInt("p2LP", Integer.parseInt(lifepoint2.getText().toString()));
        if(matchType == 3)
            actState.putInt("p3LP", Integer.parseInt(lifepoint3.getText().toString()));
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstance){
        super.onRestoreInstanceState(savedInstance);
        if(savedInstance != null){
            lifepoint.setText(String.valueOf(savedInstance.getInt("p1LP")));
            lifepoint2.setText(String.valueOf(savedInstance.getInt("p2LP")));
            if(matchType == 3)
                lifepoint3.setText(String.valueOf(savedInstance.getInt("p3LP")));
        }
    }

    @Override
    public void onGetFragmentResult(String amount, String sinal) {
            switch (reqCode){
                case AMOUNT_REQUEST_CODE1:
                    //esse código de requisição é do fragment do player1
                    aux1 = Integer.parseInt(lifepoint.getText().toString());
                    break;
                case AMOUNT_REQUEST_CODE2:
                    aux1 = Integer.parseInt(lifepoint2.getText().toString());
                    break;
                case AMOUNT_REQUEST_CODE3:
                    aux1 = Integer.parseInt(lifepoint3.getText().toString());
                    break;
            }
            aux2 = Integer.parseInt(amount);
            //PARAM1 é o valor a ser somado ou subtraido
            this.sinal = sinal;
            //PARAM2 é o sinal da operação, pode ser soma - "+" ou subtração - "-"
            cont = 0;
            if(aux2 >= 1000){
                rate = COUNT_RATE2;
            }else{
                rate = COUNT_RATE1;
            }
            //remove os callbacks da thread runnable MyTimeTask
            mHandler.removeCallbacks(myTimeTask);
            //manda a thread executar (metodo run()) após o delay especificado
            mHandler.postDelayed(myTimeTask,10);
            //streamID = soundPool.play(soundMap.get(COUNTER_SOUND),1,1,1,0,1f);
    }
}
