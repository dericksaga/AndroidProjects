package android.dominando.pointscounter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Inflater;

/**
 * Created by Derick on 23/06/2016.
 */
public class DicesActivityFragment extends DialogFragment{
    public static final int MAX_TIMES = 7;//for dice animation
    public static final int MAX_TIMES_COIN = 8;
    public static final int DICE_TYPE = 6;
    public static final int COIN_TYPE = 2;
    public static final int DICE20_TYPE = 20;
    public static final String NFACES = "NFacesObj";
    public static final int NFRAMES_COIN = 10;
    public static final int SCALE_FACTOR = 2;
    private static final int HEAD = 1;
    private static final int TAIL = 2;
    RandomObj obj;
    TextView diceResult;
    ImageView imgAnimation;
    Handler handler;
    int cont; //contador para animação dos dados
    Bitmap imgSpritesCoin;
    Bitmap[] sprites;
    int typeObj;
    AnimationDrawable animation;

    public static DicesActivityFragment newInstance(int nfaces){
        Bundle params = new Bundle();
        params.putInt(NFACES, nfaces);
        DicesActivityFragment fragment = new DicesActivityFragment();
        fragment.setArguments(params);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        handler = new Handler();
        imgSpritesCoin = getBitmapFromAssets(getActivity(),"moeda_spin.png");
        typeObj = getArguments().getInt(NFACES);
        //cria o objeto randomico para chamar o metodo roll() que retorna uma das faces
        obj = new RandomObj(typeObj);
        switch (typeObj){
            case COIN_TYPE:
                if(imgSpritesCoin != null){
                    int coinW = imgSpritesCoin.getWidth()/NFRAMES_COIN;
                    int coinH = imgSpritesCoin.getHeight();
                    sprites = new Bitmap[NFRAMES_COIN];
                    //carrega os sprites no vetor de bitmaps
                    for(int i=0; i< NFRAMES_COIN; i++){
                        sprites[i] = Bitmap.createBitmap(imgSpritesCoin,i*coinW, 0, coinW, coinH);
                        sprites[i] = Bitmap.createScaledBitmap(sprites[i],coinW*SCALE_FACTOR, coinH*SCALE_FACTOR, false);
                    }
                    //cria a animação
                    animation = createAnimation(sprites,MAX_TIMES_COIN,50);
                    Bitmap finalbmp;
                    switch(obj.roll()){
                        case HEAD:
                            finalbmp = getBitmapFromAssets(getActivity(),"coin_cara.png");
                            finalbmp = Bitmap.createScaledBitmap(finalbmp,coinW*SCALE_FACTOR,coinH*SCALE_FACTOR, false);
                            animation.addFrame(new BitmapDrawable(getResources(),finalbmp),50);
                            break;
                        case TAIL:
                            finalbmp = getBitmapFromAssets(getActivity(),"coin_coroa.png");
                            finalbmp = Bitmap.createScaledBitmap(finalbmp,coinW*SCALE_FACTOR,coinH*SCALE_FACTOR, false);
                            animation.addFrame(new BitmapDrawable(getResources(),finalbmp),50);
                            break;
                    }

                    //ela será carregada na imagem no método onCreateView()
                }
                break;
        }

    }

    public Bitmap getBitmapFromAssets(Activity activity, String filePath){
        InputStream inputStream = null;
        Bitmap spritesImage = null;
        try{
            inputStream = activity.getAssets().open(filePath);
            spritesImage = BitmapFactory.decodeStream(inputStream);
        }catch (IOException e){

        }finally {
            if(inputStream != null){
                try{
                    inputStream.close();
                }catch (IOException e2){

                }
            }
        }
        return spritesImage;
    }

    public AnimationDrawable createAnimation(Bitmap[] sprites, int nrepeats, int frameInitalDuration ){
        //cria a animação
        AnimationDrawable animation = new AnimationDrawable();
        animation.setOneShot(true);
        int nframes = sprites.length;
        int t = 1;
        for(int j=0; j<nrepeats; j++){
            for(int i=0; i<nframes; i++){
                if(j > nrepeats/2){
                    t=2;
                    if(j>nrepeats/2+ nrepeats/4){
                        t = 4;
                    }
                }
                animation.addFrame(new BitmapDrawable(getResources(),sprites[i]),frameInitalDuration*t);
            }
        }

        return animation;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View layout = inflater.inflate(R.layout.dices_activity_fragment, container);
        imgAnimation = (ImageView) layout.findViewById(R.id.imageDiceAnimation);
        diceResult = (TextView)layout.findViewById(R.id.diceValue);
        imgAnimation.setVisibility(View.INVISIBLE);
        diceResult.setVisibility(View.INVISIBLE);
        if(Build.VERSION.SDK_INT < 16){
            imgAnimation.setBackgroundDrawable(animation);
        }else{
            imgAnimation.setBackground(animation);
        }
        cont = 0;
        handler.removeCallbacks(myTaskAnimation);
        handler.post(myTaskAnimation);
        return layout;
    }

    private Runnable myTaskAnimation = new Runnable() {
        @Override
        public void run() {
            switch (typeObj) {
                case COIN_TYPE:
                    imgAnimation.setVisibility(View.VISIBLE);
                    animation.start();
                    break;
                case DICE_TYPE:
                    diceResult.setVisibility(View.VISIBLE);
                    diceResult.setText(String.valueOf(obj.roll()));
                    if(cont<MAX_TIMES){
                        cont++;
                        handler.postDelayed(myTaskAnimation,400);
                    }else{
                        diceResult.setTextColor(Color.BLUE);
                        handler.removeCallbacks(myTaskAnimation);
                    }
                    break;
                case DICE20_TYPE:
                    diceResult.setVisibility(View.VISIBLE);
                    diceResult.setText(String.valueOf(obj.roll()));
                    if(cont<MAX_TIMES){
                        cont++;
                        handler.postDelayed(myTaskAnimation,400);
                    }else{
                        diceResult.setTextColor(Color.BLUE);
                        handler.removeCallbacks(myTaskAnimation);
                    }
                    break;
            }
        }
    };
}
