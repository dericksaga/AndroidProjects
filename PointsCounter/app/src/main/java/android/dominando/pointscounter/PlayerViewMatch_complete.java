package android.dominando.pointscounter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

/**
 * Created by Derick on 14/06/2016.
 */
public class PlayerViewMatch_complete extends Fragment implements View.OnClickListener, EnterAmountFragment.OnFragmentResultListener{
    public static final String PLAYER = "player";
    public static final String MATCH_TYPE = "matchType";
    //Constantes para gerenciar os audios
    public static final int COUNTER_SOUND = 0;
    public static final int COUNT_RATE1 = 10;
    public static final int COUNT_RATE2 = 100;

    TextView playerName;
    TextView lifePoints;
    Bitmap playerPhoto;
    ImageView photo;
    int matchType;
    String sinal;
    Handler mHandler;//Handler que escalona a execução de uma thread Runnable
    int aux1;
    int aux2;
    int cont, rate;
    SoundPool soundPool;
    SparseIntArray soundMap;
    int streamID; //retornado pela função play
    ImageView surrender;
    Player player;
    View layout;


    //é necessário criar um método construtor da classe
    //caso se queira passar parametros usa-se um construtor estático
    //eh preciso passar o tipo de view que o fragment precisa carregar por causa da quantidade de players
    //da partida
    public static PlayerViewMatch_complete newInstance(int match, Player p){
        Bundle params = new Bundle();
        params.putInt(MATCH_TYPE,match);
        params.putSerializable(PLAYER, p);
        PlayerViewMatch_complete fragment = new PlayerViewMatch_complete();
        fragment.setArguments(params);
        return fragment;
    }
    //sobreescrever o metodo onCreate passando o Bundle
    //q o fragment usa para armazenar parametros internos
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null) {
            matchType = getArguments().getInt(MATCH_TYPE);
            player = (Player) getArguments().getSerializable(PLAYER);
        }else{
            matchType = savedInstanceState.getInt(MATCH_TYPE);
            player = (Player)savedInstanceState.getSerializable(PLAYER);
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
        soundMap.put(COUNTER_SOUND,soundPool.load(getContext(),R.raw.pointcountersound,1));//insere o soundpool no soundmap
    }
    //sobreescrever o metodo onCreateView
    //onde a view do fragment é criada para ser colocada na tela
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        super.onCreateView(inflater,viewGroup,savedInstanceState);
        switch(matchType){
            case 2:
                layout = inflater.inflate(R.layout.player_view_match, viewGroup, false);
                break;
            case 3:
                layout = inflater.inflate(R.layout.player_view_match_3p, viewGroup, false);
                break;
            case 4:
                layout = inflater.inflate(R.layout.player_view_match_4p,viewGroup,false);
        }
        playerName = (TextView)layout.findViewById(R.id.playername);
        lifePoints = (TextView)layout.findViewById(R.id.lifepoints);
        lifePoints.setOnClickListener(this);
        photo = (ImageView)layout.findViewById(R.id.playerphoto);
        int imgId = player.getIndexImage();
        if(imgId != R.id.reservedIdToImage) {
            playerPhoto = BitmapFactory.decodeResource(getResources(), imgId);
            photo.setImageBitmap(playerPhoto);
        }else{
            String aux = Utilidades.getFotoSalva(getContext());
            if(aux != null){
                CarregarImagemTask mtask = new CarregarImagemTask();
                mtask.execute(aux);
            }
        }
        playerName.setText(player.getPlayerName());
        lifePoints.setText(String.valueOf(player.getLifePoints()));
        surrender = (ImageView)layout.findViewById(R.id.surrender);
        surrender.setOnClickListener(this);
        layout.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.fragPlayerMatch));
        return layout;
    }
    //objeto Runnable, reponsavel por atualizar o TextView dos contadores de pontos
    private Runnable myTimeTask = new Runnable() {
        @Override
        public void run() {
            if(cont == 0) {
                streamID = soundPool.play(soundMap.get(COUNTER_SOUND), 1, 1, 1, -1, 1f);
                //this.setBackgroundColor(getColor(R.color.damageColor));
                layout.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.damageColor));
            }
                if(sinal.equals("+")){
                    if(cont<aux2){
                        cont+=rate;
                        aux1+=rate;
                    }
                }else{
                    if(cont<aux2){
                        cont+=rate;
                        aux1-=rate;
                        aux1 = (aux1 < 0)? 0 : aux1;
                    }
                }//atualiza o valor de aux1
                lifePoints.setText(String.valueOf(aux1));
                if(cont<aux2 && aux1 != 0){//verifica se a contagem ja foi finalizada
                    mHandler.postDelayed(myTimeTask,10);//se não, continua chamando esse método

                }else{
                    //Atualiza os lifepoints do player
                    player.setLifePoints(aux1);
                    mHandler.removeCallbacks(myTimeTask);//se já, remove o callback desse metodo do objeto handler
                    layout.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.fragPlayerMatch));
                    soundPool.stop(streamID);
                }
            }


    };

    @Override
    public void onClick(View v) {
       // System.out.println("Clicou no elemento com id: "+ v.getId());
        switch(v.getId()){
            case R.id.surrender:
                aux1 = Integer.parseInt(lifePoints.getText().toString());
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
               // myTimeTask.run();
                mHandler.post(myTimeTask);
                break;
            case R.id.lifepoints:
                //vou chamar outro fragment
                EnterAmountFragment fragment = new EnterAmountFragment();
                //defino o fragmente atual como target do novo fragment
                fragment.setTargetFragment(this,1);
                //chamo o fragment
                fragment.show(getFragmentManager(),"dialog");
                //o fragment que foi chamado vai retornar os dados para o targetfragment que foi definido acima
                break;
        }
    }

    @Override
    public void onGetFragmentResult(String amount, String sinal) {
        aux1 = Integer.parseInt(lifePoints.getText().toString());
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
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        state.putSerializable(PLAYER, player);
        state.putInt(MATCH_TYPE, matchType);
        super.onSaveInstanceState(state);

    }
    class CarregarImagemTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            File newfile = new File(strings[0]);
            return Utilidades.carregarImagem(newfile, 90, 90);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                playerPhoto = bitmap;
                photo.setImageBitmap(playerPhoto);

            }
        }
    }
}
